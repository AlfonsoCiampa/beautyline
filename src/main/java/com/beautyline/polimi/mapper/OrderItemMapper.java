package com.beautyline.polimi.mapper;

import com.beautyline.polimi.dto.OrderItemDTO;
import com.beautyline.polimi.entity.GiftEntity;
import com.beautyline.polimi.entity.OrderItemEntity;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class OrderItemMapper {

	public static OrderItemDTO entityToDto(OrderItemEntity entity) {
		if (entity == null) return null;
		OrderItemDTO dto = new OrderItemDTO();
		dto.setId(entity.getId());
		dto.setProductId(entity.getProductId());
		dto.setPrice(entity.getPrice());
		dto.setRetrieved(entity.getRetrieved());
		dto.setOrderId(entity.getOrderId());
		dto.setGiftedConsumerId(Optional.ofNullable(entity.getGift()).map(GiftEntity::getConsumerId).orElse(null));
		return dto;
	}

	public static OrderItemEntity dtoToEntity(OrderItemDTO dto) {
		if (dto == null) return null;
		OrderItemEntity entity = new OrderItemEntity();
		entity.setId(dto.getId());
		entity.setProductId(dto.getProductId());
		entity.setPrice(dto.getPrice());
		entity.setRetrieved(dto.getRetrieved());
		entity.setOrderId(dto.getOrderId());
		return entity;
	}

	public static List<OrderItemDTO> entitiesToDtoList(List<OrderItemEntity> entities) {
		return entities.stream().map(OrderItemMapper::entityToDto).collect(Collectors.toList());
	}

	public static List<OrderItemEntity> dtoListToEntities(List<OrderItemDTO> dtoList) {
		return dtoList.stream().map(OrderItemMapper::dtoToEntity).collect(Collectors.toList());
	}

	public static Page<OrderItemDTO> entitiesToDtoPage(Page<OrderItemEntity> entities) {
		return entities.map(OrderItemMapper::entityToDto);
	}

}
