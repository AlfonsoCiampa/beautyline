package com.beautyline.polimi.mapper;

import com.beautyline.polimi.dto.PackageItemDTO;
import com.beautyline.polimi.entity.PackageItemEntity;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

public class PackageItemMapper {

	public static PackageItemDTO entityToDto(PackageItemEntity entity) {
		if (entity == null) return null;
		PackageItemDTO dto = new PackageItemDTO();
		dto.setId(entity.getId());
		dto.setPackageId(entity.getPackageId());
		dto.setProductId(entity.getProductId());
		return dto;
	}

	public static PackageItemEntity dtoToEntity(PackageItemDTO dto) {
		if (dto == null) return null;
		PackageItemEntity entity = new PackageItemEntity();
		entity.setId(dto.getId());
		entity.setPackageId(dto.getPackageId());
		entity.setProductId(dto.getProductId());
		return entity;
	}

	public static List<PackageItemDTO> entitiesToDtoList(List<PackageItemEntity> entities) {
		return entities.stream().map(PackageItemMapper::entityToDto).collect(Collectors.toList());
	}

	public static List<PackageItemEntity> dtoListToEntities(List<PackageItemDTO> dtoList) {
		return dtoList.stream().map(PackageItemMapper::dtoToEntity).collect(Collectors.toList());
	}

	public static Page<PackageItemDTO> entitiesToDtoPage(Page<PackageItemEntity> entities) {
		return entities.map(PackageItemMapper::entityToDto);
	}

}
