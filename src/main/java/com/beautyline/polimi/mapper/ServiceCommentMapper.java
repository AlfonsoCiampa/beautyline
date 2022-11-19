package com.beautyline.polimi.mapper;

import com.beautyline.polimi.dto.ServiceCommentDTO;
import com.beautyline.polimi.entity.ServiceCommentEntity;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

public class ServiceCommentMapper {

	public static ServiceCommentDTO entityToDto(ServiceCommentEntity entity) {
		if (entity == null) return null;
		ServiceCommentDTO dto = new ServiceCommentDTO();
		dto.setId(entity.getId());
		dto.setServiceId(entity.getServiceId());
		dto.setConsumerId(entity.getConsumerId());
		dto.setServiceType(entity.getServiceType());
		dto.setDate(entity.getDate());
		dto.setDescription(entity.getDescription());
		return dto;
	}

	public static ServiceCommentEntity dtoToEntity(ServiceCommentDTO dto) {
		if (dto == null) return null;
		ServiceCommentEntity entity = new ServiceCommentEntity();
		entity.setId(dto.getId());
		entity.setServiceId(dto.getServiceId());
		entity.setConsumerId(dto.getConsumerId());
		entity.setServiceType(dto.getServiceType());
		entity.setDate(dto.getDate());
		entity.setDescription(dto.getDescription());
		return entity;
	}

	public static List<ServiceCommentDTO> entitiesToDtoList(List<ServiceCommentEntity> entities) {
		return entities.stream().map(ServiceCommentMapper::entityToDto).collect(Collectors.toList());
	}

	public static List<ServiceCommentEntity> dtoListToEntities(List<ServiceCommentDTO> dtoList) {
		return dtoList.stream().map(ServiceCommentMapper::dtoToEntity).collect(Collectors.toList());
	}

	public static Page<ServiceCommentDTO> entitiesToDtoPage(Page<ServiceCommentEntity> entities) {
		return entities.map(ServiceCommentMapper::entityToDto);
	}
}
