package com.beautyline.polimi.service.admin;

import com.beautyline.polimi.dto.ProductDTO;
import com.beautyline.polimi.entity.ProductEntity;
import com.beautyline.polimi.mapper.ProductMapper;
import com.beautyline.polimi.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class AdminProductService {

	private final ProductRepository productRepository;

	public ProductDTO getProduct(Long id) {
		return ProductMapper.entityToDto(productRepository.findById(id).orElse(null));
	}

	public Page<ProductDTO> getProducts(Integer page, Integer size) {
		return ProductMapper.entitiesToDtoPage(productRepository.findAll(
			PageRequest.of(Math.max(0, Optional.ofNullable(page).orElse(0)), Math.max(20, Optional.ofNullable(size).orElse(20)))
		));
	}

	public ProductDTO create(ProductDTO productDTO) {

		productDTOControls(productDTO, true);

		ProductEntity productEntity = ProductMapper.dtoToEntity(productDTO);
		productRepository.save(productEntity);
		return productDTO;

	}

	public ProductDTO update(ProductDTO productDTO) {

		productDTOControls(productDTO, false);

		ProductEntity productEntity = ProductMapper.dtoToEntity(productDTO);
		productRepository.save(productEntity);
		return productDTO;

	}

	public void delete(Long id) {
		if (id == null || productRepository.findById(id).isEmpty()) {
			throw new IllegalArgumentException("Invalid ID");
		}

		productRepository.deleteById(id);
	}

	public void productDTOControls(ProductDTO productDTO, boolean create) {

		// id validation
		if (!create && (productDTO.getId() == null || productRepository.findById(productDTO.getId()).isEmpty())) {
			throw new IllegalArgumentException("Invalid ID");
		}

		// name validation
		if (productDTO.getName() == null || productDTO.getName().equals("")) {
			throw new IllegalArgumentException("Invalid name");
		}

		// description validation
		if (productDTO.getDescription() == null || productDTO.getDescription().equals("")) {
			throw new IllegalArgumentException("Invalid description");
		}

		// price validation
		if (productDTO.getPrice() == null || productDTO.getPrice().compareTo(BigDecimal.valueOf(0)) < 0) {
			throw new IllegalArgumentException("Invalid price");
		}

		// quantity validation
		if (productDTO.getQuantity() == null || productDTO.getQuantity() < 0) {
			throw new IllegalArgumentException("Invalid quantity");
		}

		// obscure validation
		if (productDTO.getObscure() == null) {
			throw new IllegalArgumentException("Invalid obscure");
		}

	}

}
