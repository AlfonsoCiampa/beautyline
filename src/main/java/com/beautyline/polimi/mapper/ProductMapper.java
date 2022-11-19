package com.beautyline.polimi.mapper;

import com.beautyline.polimi.dto.ProductDTO;
import com.beautyline.polimi.entity.ProductEntity;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

public class ProductMapper {

	public static ProductDTO entityToDto(ProductEntity entity) {
		if (entity == null) return null;
		ProductDTO dto = new ProductDTO();
		dto.setId(entity.getId());
		dto.setName(entity.getName());
		dto.setDescription(entity.getDescription());
		dto.setQuantity(entity.getQuantity());
		dto.setPrice(entity.getPrice());
		dto.setObscure(entity.getObscure());
		return dto;
	}

	public static ProductEntity dtoToEntity(ProductDTO dto) {
		if (dto == null) return null;
		ProductEntity entity = new ProductEntity();
		entity.setId(dto.getId());
		entity.setName(dto.getName());
		entity.setDescription(dto.getDescription());
		entity.setQuantity(dto.getQuantity());
		entity.setPrice(dto.getPrice());
		entity.setObscure(dto.getObscure());
		return entity;
	}

	public static List<ProductDTO> entitiesToDtoList(List<ProductEntity> entities) {
		return entities.stream().map(ProductMapper::entityToDto).collect(Collectors.toList());
	}

	public static List<ProductEntity> dtoListToEntities(List<ProductDTO> dtoList) {
		return dtoList.stream().map(ProductMapper::dtoToEntity).collect(Collectors.toList());
	}

	public static Page<ProductDTO> entitiesToDtoPage(Page<ProductEntity> entities) {
		return entities.map(ProductMapper::entityToDto);
	}

}
