package com.beautyline.polimi.mapper;

import com.beautyline.polimi.dto.GiftDTO;
import com.beautyline.polimi.entity.GiftEntity;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

public class GiftMapper {

	public static GiftDTO entityToDto(GiftEntity entity) {
		if (entity == null) return null;
		GiftDTO dto = new GiftDTO();
		dto.setId(entity.getId());
		dto.setConsumerId(entity.getConsumerId());
		dto.setOrderItemId(entity.getOrderItemId());
		return dto;
	}

	public static GiftEntity dtoToEntity(GiftDTO dto) {
		if (dto == null) return null;
		GiftEntity entity = new GiftEntity();
		entity.setId(dto.getId());
		entity.setConsumerId(dto.getConsumerId());
		entity.setOrderItemId(dto.getOrderItemId());
		return entity;
	}

	public static List<GiftDTO> entitiesToDtoList(List<GiftEntity> entities) {
		return entities.stream().map(GiftMapper::entityToDto).collect(Collectors.toList());
	}

	public static List<GiftEntity> dtoListToEntities(List<GiftDTO> dtoList) {
		return dtoList.stream().map(GiftMapper::dtoToEntity).collect(Collectors.toList());
	}

	public static Page<GiftDTO> entitiesToDtoPage(Page<GiftEntity> entities) {
		return entities.map(GiftMapper::entityToDto);
	}

}
