package com.beautyline.polimi.service.consumer;

import com.beautyline.polimi.configuration.security.jwt.JWTService;
import com.beautyline.polimi.dto.ConsumerDTO;
import com.beautyline.polimi.dto.RegistrationDTO;
import com.beautyline.polimi.entity.AccountEntity;
import com.beautyline.polimi.entity.ConsumerEntity;
import com.beautyline.polimi.mapper.ConsumerMapper;
import com.beautyline.polimi.repository.AccountRepository;
import com.beautyline.polimi.repository.ConsumerRepository;
import com.beautyline.polimi.service.admin.AdminConsumerService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class ConsumerService {
	private final AccountRepository accountRepository;
	private final ConsumerRepository consumerRepository;
	private final AdminConsumerService adminConsumerService;

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

	public ConsumerDTO getData() {
		return ConsumerMapper.entityToDto(getConsumer());
	}

	public RegistrationDTO update(RegistrationDTO registrationDTO) {
		// ID validation
		if (registrationDTO.getId() == null || !registrationDTO.getId().equals(getConsumer().getId())) {
			throw new IllegalArgumentException("Invalid ID");
		}

		adminConsumerService.update(registrationDTO);
		return registrationDTO;

	}

}
