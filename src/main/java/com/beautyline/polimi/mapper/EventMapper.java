package com.beautyline.polimi.mapper;

import com.beautyline.polimi.dto.EventDTO;
import com.beautyline.polimi.entity.EventEntity;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

public class EventMapper {

	public static EventDTO entityToDto(EventEntity entity) {
		if (entity == null) return null;
		EventDTO dto = new EventDTO();
		dto.setId(entity.getId());
		dto.setDescription(entity.getDescription());
		dto.setName(entity.getName());
		dto.setStartDate(entity.getStartDate());
		dto.setEndDate(entity.getEndDate());
		return dto;
	}

	public static EventEntity dtoToEntity(EventDTO dto) {
		if (dto == null) return null;
		EventEntity entity = new EventEntity();
		entity.setId(dto.getId());
		entity.setDescription(dto.getDescription());
		entity.setName(dto.getName());
		entity.setStartDate(dto.getStartDate());
		entity.setEndDate(dto.getEndDate());
		return entity;
	}

	public static List<EventDTO> entitiesToDtoList(List<EventEntity> entities) {
		return entities.stream().map(EventMapper::entityToDto).collect(Collectors.toList());
	}

	public static List<EventEntity> dtoListToEntities(List<EventDTO> dtoList) {
		return dtoList.stream().map(EventMapper::dtoToEntity).collect(Collectors.toList());
	}

	public static Page<EventDTO> entitiesToDtoPage(Page<EventEntity> entities) {
		return entities.map(EventMapper::entityToDto);
	}

}
