package com.beautyline.polimi.mapper;

import com.beautyline.polimi.dto.OrderDTO;
import com.beautyline.polimi.entity.OrderEntity;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

public class OrderMapper {

	public static OrderDTO entityToDto(OrderEntity entity) {
		if (entity == null) return null;
		OrderDTO dto = new OrderDTO();
		dto.setId(entity.getId());
		dto.setPackageCode(entity.getPackageCode());
		dto.setConsumerId(entity.getConsumerId());
		dto.setOrderItems(OrderItemMapper.entitiesToDtoList(entity.getOrderItems()));
		dto.setPrice(entity.getPrice());
		dto.setDate(entity.getDate());
		dto.setNote(entity.getNote());
		return dto;
	}

	public static OrderEntity dtoToEntity(OrderDTO dto) {
		if (dto == null) return null;
		OrderEntity entity = new OrderEntity();
		entity.setId(dto.getId());
		entity.setPackageCode(dto.getPackageCode());
		entity.setConsumerId(dto.getConsumerId());
		entity.setOrderItems(OrderItemMapper.dtoListToEntities(dto.getOrderItems()));
		entity.setPrice(dto.getPrice());
		entity.setDate(dto.getDate());
		entity.setNote(dto.getNote());
		return entity;
	}

	public static List<OrderDTO> entitiesToDtoList(List<OrderEntity> entities) {
		return entities.stream().map(OrderMapper::entityToDto).collect(Collectors.toList());
	}

	public static List<OrderEntity> dtoListToEntities(List<OrderDTO> dtoList) {
		return dtoList.stream().map(OrderMapper::dtoToEntity).collect(Collectors.toList());
	}

	public static Page<OrderDTO> entitiesToDtoPage(Page<OrderEntity> entities) {
		return entities.map(OrderMapper::entityToDto);
	}
}
