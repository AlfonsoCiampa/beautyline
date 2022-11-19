package com.beautyline.polimi.service.admin;

import com.beautyline.polimi.dto.BookingDTO;
import com.beautyline.polimi.entity.AccountEntity;
import com.beautyline.polimi.entity.BookingEntity;
import com.beautyline.polimi.entity.TreatmentEntity;
import com.beautyline.polimi.mapper.BookingMapper;
import com.beautyline.polimi.repository.BookingRepository;
import com.beautyline.polimi.repository.TreatmentRepository;
import com.beautyline.polimi.service.consumer.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Duration;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class AdminBookingService {

	private final BookingService bookingService;
	private final BookingRepository bookingRepository;
	private final TreatmentRepository treatmentRepository;

	public BookingDTO getBooking(Long id) {
		return BookingMapper.entityToDto(bookingRepository.findById(id).orElse(null));
	}

	public Page<BookingDTO> getBookings(Integer page, Integer size) {
		return BookingMapper.entitiesToDtoPage(bookingRepository.findAll(
			PageRequest.of(Math.max(0, Optional.ofNullable(page).orElse(0)), Math.max(20, Optional.ofNullable(size).orElse(20)))
		));
	}

	public BookingDTO create(BookingDTO bookingDTO) {
		bookingService.bookingDTOControls(bookingDTO, AccountEntity.Type.ADMIN, true);
		TreatmentEntity treatment = treatmentRepository.findById(bookingDTO.getTreatmentId())
			.orElseThrow(() -> new IllegalArgumentException("Invalid treatment ID"));

		BookingEntity bookingEntity = BookingMapper.dtoToEntity(bookingDTO);
		bookingEntity.setEndTime(bookingEntity.getStartTime().plus(Duration.ofSeconds(treatment.getDuration())));
		bookingRepository.save(bookingEntity);
		return bookingDTO;
	}

	public BookingDTO update(BookingDTO bookingDTO) {
		bookingService.bookingDTOControls(bookingDTO, AccountEntity.Type.ADMIN, false);
		TreatmentEntity treatment = treatmentRepository.findById(bookingDTO.getTreatmentId())
			.orElseThrow(() -> new IllegalArgumentException("Invalid treatment ID"));

		BookingEntity bookingEntity = BookingMapper.dtoToEntity(bookingDTO);
		bookingEntity.setEndTime(bookingEntity.getStartTime().plus(Duration.ofSeconds(treatment.getDuration())));
		bookingRepository.save(bookingEntity);
		return bookingDTO;

	}

	public void delete(Long id) {
		if (id == null || bookingRepository.findById(id).isEmpty()) {
			throw new IllegalArgumentException("Invalid ID");
		}

		bookingRepository.deleteById(id);
	}
}
