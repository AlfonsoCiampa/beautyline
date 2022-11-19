package com.beautyline.polimi.mapper;

import com.beautyline.polimi.dto.BookingConfigurationDTO;
import com.beautyline.polimi.entity.BookingConfigurationEntity;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

public class BookingConfigurationMapper {

	public static BookingConfigurationDTO entityToDto(BookingConfigurationEntity entity) {
		if (entity == null) return null;
		BookingConfigurationDTO dto = new BookingConfigurationDTO();
		dto.setId(entity.getId());
		dto.setDate(entity.getDate());
		dto.setStartTime(entity.getStartTime());
		dto.setEndTime(entity.getEndTime());
		return dto;
	}

	public static BookingConfigurationEntity dtoToEntity(BookingConfigurationDTO dto) {
		if (dto == null) return null;
		BookingConfigurationEntity entity = new BookingConfigurationEntity();
		entity.setId(dto.getId());
		entity.setDate(dto.getDate());
		entity.setStartTime(dto.getStartTime());
		entity.setEndTime(dto.getEndTime());
		return entity;
	}

	public static List<BookingConfigurationDTO> entitiesToDtoList(List<BookingConfigurationEntity> entities) {
		return entities.stream().map(BookingConfigurationMapper::entityToDto).collect(Collectors.toList());
	}

	public static List<BookingConfigurationEntity> dtoListToEntities(List<BookingConfigurationDTO> dtoList) {
		return dtoList.stream().map(BookingConfigurationMapper::dtoToEntity).collect(Collectors.toList());
	}

	public static Page<BookingConfigurationDTO> entitiesToDtoPage(Page<BookingConfigurationEntity> entities) {
		return entities.map(BookingConfigurationMapper::entityToDto);
	}

}
