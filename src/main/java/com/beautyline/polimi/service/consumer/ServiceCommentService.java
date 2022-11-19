package com.beautyline.polimi.service.consumer;

import com.beautyline.polimi.configuration.security.jwt.JWTService;
import com.beautyline.polimi.dto.ServiceCommentDTO;
import com.beautyline.polimi.entity.AccountEntity;
import com.beautyline.polimi.entity.ConsumerEntity;
import com.beautyline.polimi.entity.ServiceCommentEntity;
import com.beautyline.polimi.mapper.ServiceCommentMapper;
import com.beautyline.polimi.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class ServiceCommentService {

	private final ServiceCommentRepository serviceCommentRepository;
	private final ConsumerRepository consumerRepository;
	private final ProductRepository productRepository;
	private final TreatmentRepository treatmentRepository;
	private final AccountRepository accountRepository;

	private ConsumerEntity getConsumer() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null) {
			User user = ((User) authentication.getPrincipal());

			if (!user.getAuthorities().contains(new SimpleGrantedAuthority(AccountEntity.Type.CONSUMER.name()))) {
				throw new JWTService.TokenVerificationException();
			}

			AccountEntity account = accountRepository.findByReferenceTypeAndEmail(AccountEntity.Type.CONSUMER, user.getUsername())
				.orElseThrow(JWTService.TokenVerificationException::new);
			return consumerRepository.findById(account.getReferenceId())
				.orElseThrow(JWTService.TokenVerificationException::new);
		}
		throw new JWTService.TokenVerificationException();
	}

	public Page<ServiceCommentDTO> getComments(Integer page, Integer size) {
		return ServiceCommentMapper.entitiesToDtoPage(serviceCommentRepository.findAllByConsumerId(
			getConsumer().getId(),
			PageRequest.of(Math.max(0, Optional.ofNullable(page).orElse(0)), Math.max(20, Optional.ofNullable(size).orElse(20)))
		));
	}

	public ServiceCommentDTO create(ServiceCommentDTO serviceCommentDTO) {

		serviceCommentDTOControls(serviceCommentDTO, AccountEntity.Type.CONSUMER, true);

		ServiceCommentEntity serviceCommentEntity = ServiceCommentMapper.dtoToEntity(serviceCommentDTO);
		serviceCommentRepository.save(serviceCommentEntity);
		return serviceCommentDTO;

	}

	public ServiceCommentDTO update(ServiceCommentDTO serviceCommentDTO) {

		serviceCommentDTOControls(serviceCommentDTO, AccountEntity.Type.CONSUMER, false);

		ServiceCommentEntity serviceCommentEntity = ServiceCommentMapper.dtoToEntity(serviceCommentDTO);
		serviceCommentRepository.save(serviceCommentEntity);
		return serviceCommentDTO;

	}

	public void delete(Long id) {
		// id validation
		if (id == null || serviceCommentRepository.findById(id).isEmpty()) {
			throw new IllegalArgumentException("Invalid id");
		}
		// owner validation
		if (!Objects.equals(serviceCommentRepository.findById(id)
			.map(ServiceCommentEntity::getConsumerId)
			.orElse(null), getConsumer().getId())) {
			throw new IllegalArgumentException("Invalid owner");
		}

		serviceCommentRepository.deleteById(id);
	}

	public void serviceCommentDTOControls(ServiceCommentDTO serviceCommentDTO, AccountEntity.Type accountType, boolean create) {

		// id validation
		if (!create && (serviceCommentDTO.getId() == null || serviceCommentRepository.findById(serviceCommentDTO.getId()).isEmpty())) {
			throw new IllegalArgumentException("Invalid ID");
		}

		// consumerID validation
		if (serviceCommentDTO.getConsumerId() == null
			|| consumerRepository.findById(serviceCommentDTO.getConsumerId()).isEmpty()
			|| (accountType == AccountEntity.Type.CONSUMER && !Objects.equals(serviceCommentDTO.getConsumerId(), getConsumer().getId()))) {
			throw new IllegalArgumentException("Invalid consumer ID");
		}

		// owner validation
		if (!create && accountType == AccountEntity.Type.CONSUMER
			&& !Objects.equals(serviceCommentRepository.findById(serviceCommentDTO.getId())
			.map(ServiceCommentEntity::getConsumerId)
			.orElse(null), getConsumer().getId())) {
			throw new IllegalArgumentException("Invalid owner");
		}

		// serviceType validation
		if (serviceCommentDTO.getServiceType() == null ||
			(!serviceCommentDTO.getServiceType().equals(ServiceCommentEntity.Type.PRODUCT) &&
				!serviceCommentDTO.getServiceType().equals(ServiceCommentEntity.Type.TREATMENT))) {
			throw new IllegalArgumentException("Invalid service type");
		}

		// serviceId validation
		if (serviceCommentDTO.getServiceType().equals(ServiceCommentEntity.Type.PRODUCT)) {
			// if product
			if (serviceCommentDTO.getServiceId() == null || productRepository.findById(serviceCommentDTO.getServiceId()).isEmpty()) {
				throw new IllegalArgumentException("Invalid product ID");
			}
		} else {
			// if treatment
			if (serviceCommentDTO.getServiceId() == null || treatmentRepository.findById(serviceCommentDTO.getServiceId()).isEmpty()) {
				throw new IllegalArgumentException("Invalid treatment ID");
			}
		}

		// description validation
		if (serviceCommentDTO.getDescription() == null || serviceCommentDTO.getDescription().equals("")) {
			throw new IllegalArgumentException("Invalid description");
		}

		// date validation
		if (serviceCommentDTO.getDate() == null || serviceCommentDTO.getDate().isAfter(LocalDateTime.now())) {
			throw new IllegalArgumentException("Invalid date");
		}

	}

}
