package com.beautyline.polimi.mapper;


import com.beautyline.polimi.dto.TreatmentDTO;
import com.beautyline.polimi.entity.TreatmentEntity;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

public class TreatmentMapper {

	public static TreatmentDTO entityToDto(TreatmentEntity entity) {
		if (entity == null) return null;
		TreatmentDTO dto = new TreatmentDTO();
		dto.setId(entity.getId());
		dto.setObscure(entity.getObscure());
		dto.setPrice(entity.getPrice());
		dto.setDescription(entity.getDescription());
		dto.setName(entity.getName());
		dto.setDuration(entity.getDuration());
		return dto;
	}

	public static TreatmentEntity dtoToEntity(TreatmentDTO dto) {
		if (dto == null) return null;
		TreatmentEntity entity = new TreatmentEntity();
		entity.setId(dto.getId());
		entity.setObscure(dto.getObscure());
		entity.setPrice(dto.getPrice());
		entity.setDescription(dto.getDescription());
		entity.setName(dto.getName());
		entity.setDuration(dto.getDuration());
		return entity;
	}

	public static List<TreatmentDTO> entitiesToDtoList(List<TreatmentEntity> entities) {
		return entities.stream().map(TreatmentMapper::entityToDto).collect(Collectors.toList());
	}

	public static List<TreatmentEntity> dtoListToEntities(List<TreatmentDTO> dtoList) {
		return dtoList.stream().map(TreatmentMapper::dtoToEntity).collect(Collectors.toList());
	}

	public static Page<TreatmentDTO> entitiesToDtoPage(Page<TreatmentEntity> entities) {
		return entities.map(TreatmentMapper::entityToDto);
	}

}
