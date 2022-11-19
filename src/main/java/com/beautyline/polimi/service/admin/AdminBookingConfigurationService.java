package com.beautyline.polimi.service.admin;

import com.beautyline.polimi.dto.BookingConfigurationDTO;
import com.beautyline.polimi.entity.BookingConfigurationEntity;
import com.beautyline.polimi.mapper.BookingConfigurationMapper;
import com.beautyline.polimi.repository.BookingConfigurationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class AdminBookingConfigurationService {

	private final BookingConfigurationRepository bookingConfigurationRepository;

	public BookingConfigurationDTO getBookingConfiguration(Long id) {
		return BookingConfigurationMapper.entityToDto(bookingConfigurationRepository.findById(id).orElse(null));
	}

	public Page<BookingConfigurationDTO> getBookingConfigurations(Integer page, Integer size) {
		return BookingConfigurationMapper.entitiesToDtoPage(bookingConfigurationRepository.findAll(
			PageRequest.of(Math.max(0, Optional.ofNullable(page).orElse(0)), Math.max(20, Optional.ofNullable(size).orElse(20)))
		));
	}

	public BookingConfigurationDTO create(BookingConfigurationDTO bookingConfigurationDTO) {
		bookingConfigurationDTOControls(bookingConfigurationDTO, true);
		BookingConfigurationEntity bookingConfigurationEntity = BookingConfigurationMapper.dtoToEntity(bookingConfigurationDTO);
		bookingConfigurationRepository.save(bookingConfigurationEntity);
		return bookingConfigurationDTO;
	}

	public BookingConfigurationDTO update(BookingConfigurationDTO bookingConfigurationDTO) {
		bookingConfigurationDTOControls(bookingConfigurationDTO, false);
		BookingConfigurationEntity bookingConfigurationEntity = BookingConfigurationMapper.dtoToEntity(bookingConfigurationDTO);
		bookingConfigurationRepository.save(bookingConfigurationEntity);
		return bookingConfigurationDTO;

	}

	public void delete(Long id) {
		if (id == null || bookingConfigurationRepository.findById(id).isEmpty()) {
			throw new IllegalArgumentException("Invalid ID");
		}

		bookingConfigurationRepository.deleteById(id);
	}

	public void bookingConfigurationDTOControls(BookingConfigurationDTO bookingConfigurationDTO, Boolean create) {

		// id validation
		if (!create && (bookingConfigurationDTO.getId() == null || bookingConfigurationRepository.findById(bookingConfigurationDTO.getId()).isEmpty())) {
			throw new IllegalArgumentException("Invalid ID");
		}

		// name validation
		if (bookingConfigurationDTO.getDate() == null) {
			throw new IllegalArgumentException("Invalid date");
		}

		boolean invalidDateFormat = true;
		try {
			LocalDate.parse(bookingConfigurationDTO.getDate());
			invalidDateFormat = false;
		} catch (Exception ignored) {
		}

		if (invalidDateFormat && Arrays.stream(DayOfWeek.values()).noneMatch(d -> d.name().equals(bookingConfigurationDTO.getDate()))) {
			throw new IllegalArgumentException("Invalid date");
		}

		// description validation
		if (bookingConfigurationDTO.getStartTime() == null) {
			throw new IllegalArgumentException("Invalid start time");
		}

		// endDate validation
		if (bookingConfigurationDTO.getEndTime() == null || bookingConfigurationDTO.getEndTime().isBefore(bookingConfigurationDTO.getStartTime())) {
			throw new IllegalArgumentException("Invalid end time");
		}

		// interval validation
		List<BookingConfigurationEntity> bookingConfigurations;
		if (!invalidDateFormat) {
			bookingConfigurations = bookingConfigurationRepository.findAllByDateAndActiveOn(
				LocalDate.parse(bookingConfigurationDTO.getDate()).format(DateTimeFormatter.ISO_DATE),
				bookingConfigurationDTO.getStartTime(),
				bookingConfigurationDTO.getEndTime()
			);
		} else {
			bookingConfigurations = bookingConfigurationRepository.findAllByDayOfWeekAndActiveOn(
				DayOfWeek.valueOf(bookingConfigurationDTO.getDate()),
				bookingConfigurationDTO.getStartTime(),
				bookingConfigurationDTO.getEndTime()
			);
		}

		if (!create) {
			bookingConfigurations.removeIf(bc -> bc.getId().equals(bookingConfigurationDTO.getId()));
		}
		if (bookingConfigurations.size() != 0) {
			throw new IllegalArgumentException("Invalid date");
		}

	}
}
