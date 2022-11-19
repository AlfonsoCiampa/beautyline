package com.beautyline.polimi.mapper;

import com.beautyline.polimi.dto.PackageDTO;
import com.beautyline.polimi.entity.PackageEntity;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

public class PackageMapper {

	public static PackageDTO entityToDto(PackageEntity entity) {
		if (entity == null) return null;
		PackageDTO dto = new PackageDTO();
		dto.setId(entity.getId());
		dto.setPrice(entity.getPrice());
		dto.setDescription(entity.getDescription());
		dto.setPackageItems(PackageItemMapper.entitiesToDtoList(entity.getPackageItems()));
		dto.setName(entity.getName());
		dto.setStartDate(entity.getStartDate());
		dto.setEndDate(entity.getEndDate());
		dto.setCode(entity.getCode());
		return dto;
	}

	public static PackageEntity dtoToEntity(PackageDTO dto) {
		if (dto == null) return null;
		PackageEntity entity = new PackageEntity();
		entity.setId(dto.getId());
		entity.setPrice(dto.getPrice());
		entity.setDescription(dto.getDescription());
		entity.setPackageItems(PackageItemMapper.dtoListToEntities(dto.getPackageItems()));
		entity.setName(dto.getName());
		entity.setStartDate(dto.getStartDate());
		entity.setEndDate(dto.getEndDate());
		entity.setCode(dto.getCode());
		return entity;
	}

	public static List<PackageDTO> entitiesToDtoList(List<PackageEntity> entities) {
		return entities.stream().map(PackageMapper::entityToDto).collect(Collectors.toList());
	}

	public static List<PackageEntity> dtoListToEntities(List<PackageDTO> dtoList) {
		return dtoList.stream().map(PackageMapper::dtoToEntity).collect(Collectors.toList());
	}

	public static Page<PackageDTO> entitiesToDtoPage(Page<PackageEntity> entities) {
		return entities.map(PackageMapper::entityToDto);
	}

}
