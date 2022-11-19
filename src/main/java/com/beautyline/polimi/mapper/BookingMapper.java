package com.beautyline.polimi.mapper;

import com.beautyline.polimi.dto.BookingDTO;
import com.beautyline.polimi.entity.BookingEntity;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

public class BookingMapper {

	public static BookingDTO entityToDto(BookingEntity entity) {
		if (entity == null) return null;
		BookingDTO dto = new BookingDTO();
		dto.setId(entity.getId());
		dto.setConsumerId(entity.getConsumerId());
		dto.setTreatmentId(entity.getTreatmentId());
		dto.setNote(entity.getNote());
		dto.setStartTime(entity.getStartTime());
		dto.setEndTime(entity.getEndTime());
		return dto;
	}

	public static BookingEntity dtoToEntity(BookingDTO dto) {
		if (dto == null) return null;
		BookingEntity entity = new BookingEntity();
		entity.setId(dto.getId());
		entity.setConsumerId(dto.getConsumerId());
		entity.setTreatmentId(dto.getTreatmentId());
		entity.setNote(dto.getNote());
		entity.setStartTime(dto.getStartTime());
		entity.setEndTime(dto.getEndTime());
		return entity;
	}

	public static List<BookingDTO> entitiesToDtoList(List<BookingEntity> entities) {
		return entities.stream().map(BookingMapper::entityToDto).collect(Collectors.toList());
	}

	public static List<BookingEntity> dtoListToEntities(List<BookingDTO> dtoList) {
		return dtoList.stream().map(BookingMapper::dtoToEntity).collect(Collectors.toList());
	}

	public static Page<BookingDTO> entitiesToDtoPage(Page<BookingEntity> entities) {
		return entities.map(BookingMapper::entityToDto);
	}

}
