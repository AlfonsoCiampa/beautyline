package com.beautyline.polimi.service.admin;

import com.beautyline.polimi.dto.ServiceCommentDTO;
import com.beautyline.polimi.entity.AccountEntity;
import com.beautyline.polimi.entity.ServiceCommentEntity;
import com.beautyline.polimi.mapper.ServiceCommentMapper;
import com.beautyline.polimi.repository.ServiceCommentRepository;
import com.beautyline.polimi.service.consumer.ServiceCommentService;
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
public class AdminServiceCommentService {

	private final ServiceCommentRepository serviceCommentRepository;
	private final ServiceCommentService serviceCommentService;

	public ServiceCommentDTO getComment(Long id) {
		return ServiceCommentMapper.entityToDto(serviceCommentRepository.findById(id).orElse(null));
	}

	public Page<ServiceCommentDTO> getComments(Integer page, Integer size) {
		return ServiceCommentMapper.entitiesToDtoPage(serviceCommentRepository.findAll(
			PageRequest.of(Math.max(0, Optional.ofNullable(page).orElse(0)), Math.max(20, Optional.ofNullable(size).orElse(20)))
		));
	}

	public ServiceCommentDTO create(ServiceCommentDTO serviceCommentDTO) {

		serviceCommentService.serviceCommentDTOControls(serviceCommentDTO, AccountEntity.Type.ADMIN, true);

		ServiceCommentEntity serviceCommentEntity = ServiceCommentMapper.dtoToEntity(serviceCommentDTO);
		serviceCommentRepository.save(serviceCommentEntity);
		return serviceCommentDTO;

	}

	public ServiceCommentDTO update(ServiceCommentDTO serviceCommentDTO) {

		serviceCommentService.serviceCommentDTOControls(serviceCommentDTO, AccountEntity.Type.ADMIN, false);

		ServiceCommentEntity serviceCommentEntity = ServiceCommentMapper.dtoToEntity(serviceCommentDTO);
		serviceCommentRepository.save(serviceCommentEntity);
		return serviceCommentDTO;

	}

	public void delete(Long id) {
		if (id == null || serviceCommentRepository.findById(id).isEmpty()) {
			throw new IllegalArgumentException("Invalid ID");
		}

		serviceCommentRepository.deleteById(id);
	}

}
