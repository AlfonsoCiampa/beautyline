package com.beautyline.polimi.mapper;

import com.beautyline.polimi.dto.ConsumerDTO;
import com.beautyline.polimi.dto.RegistrationDTO;
import com.beautyline.polimi.entity.ConsumerEntity;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

public class ConsumerMapper {

	public static ConsumerDTO entityToDto(ConsumerEntity entity) {
		if (entity == null) return null;
		ConsumerDTO dto = new ConsumerDTO();
		dto.setId(entity.getId());
		dto.setName(entity.getName());
		dto.setSurname(entity.getSurname());
		dto.setPhone(entity.getPhone());
		dto.setEmail(entity.getAccount().getEmail());
		return dto;
	}

	public static ConsumerEntity dtoToEntity(ConsumerDTO dto) {
		if (dto == null) return null;
		ConsumerEntity entity = new ConsumerEntity();
		entity.setId(dto.getId());
		entity.setName(dto.getName());
		entity.setSurname(dto.getSurname());
		entity.setPhone(dto.getPhone());
		return entity;
	}

	public static ConsumerEntity registrationDtoToConsumerEntity(RegistrationDTO registrationDTO) {
		if (registrationDTO == null) return null;
		ConsumerEntity entity = new ConsumerEntity();
		entity.setId(registrationDTO.getId());
		entity.setName(registrationDTO.getName());
		entity.setSurname(registrationDTO.getSurname());
		entity.setPhone(registrationDTO.getPhone());
		return entity;
	}

	public static List<ConsumerDTO> entitiesToDtoList(List<ConsumerEntity> entities) {
		return entities.stream().map(ConsumerMapper::entityToDto).collect(Collectors.toList());
	}

	public static List<ConsumerEntity> dtoListToEntities(List<ConsumerDTO> dtoList) {
		return dtoList.stream().map(ConsumerMapper::dtoToEntity).collect(Collectors.toList());
	}

	public static Page<ConsumerDTO> entitiesToDtoPage(Page<ConsumerEntity> entities) {
		return entities.map(ConsumerMapper::entityToDto);
	}

}
