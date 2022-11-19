package com.beautyline.polimi.service.generic;

import com.beautyline.polimi.dto.ProductDTO;
import com.beautyline.polimi.dto.ServiceCommentDTO;
import com.beautyline.polimi.dto.TreatmentDTO;
import com.beautyline.polimi.entity.ServiceCommentEntity;
import com.beautyline.polimi.mapper.ProductMapper;
import com.beautyline.polimi.mapper.ServiceCommentMapper;
import com.beautyline.polimi.mapper.TreatmentMapper;
import com.beautyline.polimi.repository.ProductRepository;
import com.beautyline.polimi.repository.ServiceCommentRepository;
import com.beautyline.polimi.repository.TreatmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class ServiceService {

	private final ProductRepository productRepository;
	private final TreatmentRepository treatmentRepository;
	private final ServiceCommentRepository serviceCommentRepository;

	public TreatmentDTO getTreatment(Long id) {
		return TreatmentMapper.entityToDto(treatmentRepository.findById(id).orElse(null));
	}

	public Page<TreatmentDTO> getTreatments(Integer page, Integer size) {
		return TreatmentMapper.entitiesToDtoPage(treatmentRepository.findAll(
			PageRequest.of(Math.max(0, Optional.ofNullable(page).orElse(0)), Math.max(20, Optional.ofNullable(size).orElse(20)))
		));
	}

	public Page<ServiceCommentDTO> getTreatmentComments(Long id, Integer page, Integer size) {
		return ServiceCommentMapper.entitiesToDtoPage(serviceCommentRepository.findAllByServiceTypeAndServiceId(
			ServiceCommentEntity.Type.TREATMENT, id,
			PageRequest.of(Math.max(0, Optional.ofNullable(page).orElse(0)), Math.max(20, Optional.ofNullable(size).orElse(20)))
		));
	}

	public ProductDTO getProduct(Long id) {
		return ProductMapper.entityToDto(productRepository.findById(id).orElse(null));
	}

	public Page<ProductDTO> getProducts(Integer page, Integer size) {
		return ProductMapper.entitiesToDtoPage(productRepository.findAll(
			PageRequest.of(Math.max(0, Optional.ofNullable(page).orElse(0)), Math.max(20, Optional.ofNullable(size).orElse(20)))
		));
	}

	public Page<ServiceCommentDTO> getProductComments(Long id, Integer page, Integer size) {
		return ServiceCommentMapper.entitiesToDtoPage(serviceCommentRepository.findAllByServiceTypeAndServiceId(
			ServiceCommentEntity.Type.PRODUCT, id,
			PageRequest.of(Math.max(0, Optional.ofNullable(page).orElse(0)), Math.max(20, Optional.ofNullable(size).orElse(20)))
		));
	}

	public ServiceCommentDTO getComment(Long id) {
		return ServiceCommentMapper.entityToDto(serviceCommentRepository.findById(id).orElse(null));
	}
}
