package com.beautyline.polimi.mapper;

import com.beautyline.polimi.dto.AdminDTO;
import com.beautyline.polimi.entity.AccountEntity;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

public class AdminMapper {

	public static AdminDTO entityToDto(AccountEntity entity) {
		if (entity == null) return null;
		AdminDTO dto = new AdminDTO();
		dto.setId(entity.getId());
		dto.setEmail(entity.getEmail());
		return dto;
	}

	public static List<AdminDTO> entitiesToDtoList(List<AccountEntity> entities) {
		return entities.stream().map(AdminMapper::entityToDto).collect(Collectors.toList());
	}

	public static Page<AdminDTO> entitiesToDtoPage(Page<AccountEntity> entities) {
		return entities.map(AdminMapper::entityToDto);
	}

}
