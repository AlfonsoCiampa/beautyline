package com.beautyline.polimi.service.consumer;

import com.beautyline.polimi.configuration.security.jwt.JWTService;
import com.beautyline.polimi.dto.BookingDTO;
import com.beautyline.polimi.entity.*;
import com.beautyline.polimi.mapper.BookingMapper;
import com.beautyline.polimi.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class BookingService {

	private final AccountRepository accountRepository;
	private final BookingRepository bookingRepository;
	private final ConsumerRepository consumerRepository;
	private final TreatmentRepository treatmentRepository;
	private final BookingConfigurationRepository bookingConfigurationRepository;
	@Value("${beautyline.user.active-booking.max}")
	private String userMaxActiveBooking;

	private ConsumerEntity getConsumer() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null) {
			User user = ((User) authentication.getPrincipal());

			if (!user.getAuthorities().contains(new SimpleGrantedAuthority(AccountEntity.Type.CONSUMER.name()))) {
				throw new JWTService.TokenVerificationException();
			}

			AccountEntity account = accountRepository.findByReferenceTypeAndEmail(AccountEntity.Type.CONSUMER, user.getUsername())
				.orElseThrow(JWTService.TokenVerificationException::new);
			return consumerRepository.findById(account.getReferenceId())
				.orElseThrow(JWTService.TokenVerificationException::new);
		}
		throw new JWTService.TokenVerificationException();
	}

	public BookingDTO getBooking(Long id) {
		ConsumerEntity consumer = getConsumer();
		BookingEntity bookingEntity = bookingRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid ID"));
		if (!Objects.equals(bookingEntity.getConsumerId(), consumer.getId())) {
			throw new IllegalArgumentException("Invalid owner");
		}
		return BookingMapper.entityToDto(bookingEntity);
	}

	public Page<BookingDTO> getBookings(Integer page, Integer size) {
		return BookingMapper.entitiesToDtoPage(bookingRepository.findAllByConsumerId(
			getConsumer().getId(),
			PageRequest.of(Math.max(0, Optional.ofNullable(page).orElse(0)), Math.max(20, Optional.ofNullable(size).orElse(20)))
		));
	}

	public BookingDTO create(BookingDTO bookingDTO) {
		bookingDTOControls(bookingDTO, AccountEntity.Type.CONSUMER, true);
		TreatmentEntity treatment = treatmentRepository.findById(bookingDTO.getTreatmentId())
			.orElseThrow(() -> new IllegalArgumentException("Invalid treatment ID"));

		BookingEntity bookingEntity = BookingMapper.dtoToEntity(bookingDTO);
		bookingEntity.setEndTime(bookingEntity.getStartTime().plus(Duration.ofSeconds(treatment.getDuration())));
		bookingRepository.save(bookingEntity);
		return bookingDTO;

	}

	public void delete(Long id) {
		// id validation
		if (id == null || bookingRepository.findById(id).isEmpty()) {
			throw new IllegalArgumentException("Invalid id");
		}
		// owner validation
		if (!Objects.equals(bookingRepository.findById(id)
			.map(BookingEntity::getConsumerId)
			.orElse(null), getConsumer().getId())) {
			throw new IllegalArgumentException("Invalid owner");
		}

		bookingRepository.deleteById(id);
	}

	public void bookingDTOControls(BookingDTO bookingDTO, AccountEntity.Type accountType, boolean create) {

		// id validation
		if (!create && (bookingDTO.getId() == null || bookingRepository.findById(bookingDTO.getId()).isEmpty())) {
			throw new IllegalArgumentException("Invalid ID");
		}

		// consumerId validation
		if (bookingDTO.getConsumerId() == null
			|| consumerRepository.findById(bookingDTO.getConsumerId()).isEmpty()
			|| (accountType == AccountEntity.Type.CONSUMER && !Objects.equals(bookingDTO.getConsumerId(), getConsumer().getId()))) {
			throw new IllegalArgumentException("Invalid consumer ID");
		}

		// treatmentId validation
		if (bookingDTO.getTreatmentId() == null) {
			throw new IllegalArgumentException("Invalid treatment ID");
		}

		TreatmentEntity treatment = treatmentRepository.findById(bookingDTO.getTreatmentId())
			.orElseThrow(() -> new IllegalArgumentException("Invalid treatment ID"));

		// owner validation
		if (!create && accountType == AccountEntity.Type.CONSUMER && !Objects.equals(bookingRepository.findById(bookingDTO.getId())
			.map(BookingEntity::getConsumerId)
			.orElse(null), getConsumer().getId())) {
			throw new IllegalArgumentException("Invalid owner");
		}

		// booking period validation
		LocalDateTime bookingStart = bookingDTO.getStartTime();
		LocalDateTime bookingEnd = bookingDTO.getStartTime().plus(Duration.ofSeconds(treatment.getDuration()));

		if (create && accountType == AccountEntity.Type.CONSUMER
			&& bookingRepository.countByActiveAndConsumerId(bookingStart, bookingDTO.getConsumerId()) >= Integer.parseInt(userMaxActiveBooking)) {
			throw new IllegalStateException("Max booking reached");
		}

		List<BookingEntity> activeOnPeriod = bookingRepository.findByPeriod(bookingStart, bookingEnd);
		if (!activeOnPeriod.isEmpty() && (create || activeOnPeriod.size() > 1 || !activeOnPeriod.get(0).getId().equals(bookingDTO.getId()))) {
			throw new IllegalArgumentException("Booking period conflict");
		}

		LocalDate endDate = bookingEnd.toLocalDate();
		for (LocalDateTime cursor = bookingStart; cursor.toLocalDate().compareTo(endDate) <= 0;
			 cursor = cursor.toLocalDate().plusDays(1).atStartOfDay()) {
			List<BookingConfigurationEntity> bookingConfigurations = bookingConfigurationRepository
				.findAllByDateOrderByStartTime(cursor.toLocalDate().format(DateTimeFormatter.ISO_DATE));
			if (bookingConfigurations.isEmpty()) {
				bookingConfigurations = bookingConfigurationRepository.findAllByDayOfWeekOrderByStartTime(cursor.getDayOfWeek());
			}

			LocalTime endTime = cursor.toLocalDate().plusDays(1).isBefore(endDate) ? LocalTime.MIDNIGHT : bookingEnd.toLocalTime();
			if (!bookingConfigurationCheck(bookingConfigurations, cursor.toLocalTime(), endTime)) {
				throw new IllegalArgumentException("Invalid booking period");
			}
		}
	}

	private boolean bookingConfigurationCheck(List<BookingConfigurationEntity> bookingConfigurations, LocalTime startTime, LocalTime endTime) {
		LocalTime cursor = startTime;
		for (BookingConfigurationEntity bookingConfiguration : bookingConfigurations) {
			if (bookingConfiguration.getStartTime().isAfter(cursor)) {
				return false;
			}

			if (bookingConfiguration.getEndTime().isAfter(cursor)) {
				if (bookingConfiguration.getEndTime().compareTo(endTime) >= 0) {
					return true;
				} else {
					cursor = bookingConfiguration.getEndTime();
				}
			}
		}
		return false;
	}
}
