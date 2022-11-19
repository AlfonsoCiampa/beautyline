package com.beautyline.polimi.service.admin;

import com.beautyline.polimi.dto.ConsumerDTO;
import com.beautyline.polimi.dto.RegistrationDTO;
import com.beautyline.polimi.entity.AccountEntity;
import com.beautyline.polimi.entity.ConsumerEntity;
import com.beautyline.polimi.mapper.AccountMapper;
import com.beautyline.polimi.mapper.ConsumerMapper;
import com.beautyline.polimi.repository.AccountRepository;
import com.beautyline.polimi.repository.ConsumerRepository;
import com.beautyline.polimi.service.generic.AccessService;
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
public class AdminConsumerService {

	private final AccountRepository accountRepository;
	private final ConsumerRepository consumerRepository;
	private final AccessService accessService;

	public ConsumerDTO getConsumer(Long id) {
		return ConsumerMapper.entityToDto(consumerRepository.findById(id).orElse(null));
	}

	public Page<ConsumerDTO> getConsumers(Integer page, Integer size) {
		return ConsumerMapper.entitiesToDtoPage(consumerRepository.findAll(
			PageRequest.of(Math.max(0, Optional.ofNullable(page).orElse(0)), Math.max(20, Optional.ofNullable(size).orElse(20)))
		));
	}

	public RegistrationDTO create(RegistrationDTO registrationDTO) {

		accessService.registrationDTOControls(registrationDTO, AccountEntity.Type.CONSUMER, true);

		ConsumerEntity consumerEntity = ConsumerMapper.registrationDtoToConsumerEntity(registrationDTO);
		consumerEntity = consumerRepository.save(consumerEntity);    // metti sempre l'uguale se usi la variabile dopo,
		// altrimenti la variabile non viene modificata
		// mapping the accountEntity from RegistrationDTO, here we need to get the referenceId created in line 32
		AccountEntity accountEntity = AccountMapper.consumerRegistrationDtoToAccountEntity(registrationDTO);
		accountEntity.setReferenceId(consumerEntity.getId());
		accountRepository.save(accountEntity);

		return registrationDTO;
	}

	public RegistrationDTO update(RegistrationDTO registrationDTO) {

		accessService.registrationDTOControls(registrationDTO, AccountEntity.Type.CONSUMER, false);

		ConsumerEntity consumerEntity = ConsumerMapper.registrationDtoToConsumerEntity(registrationDTO);
		consumerRepository.save(consumerEntity);
		AccountEntity accountEntity = AccountMapper.consumerRegistrationDtoToAccountEntity(registrationDTO);
		accountEntity.setId(accountRepository.findByReferenceId(registrationDTO.getId()).get().getId());
		accountRepository.save(accountEntity);

		return registrationDTO;

	}

	public void delete(Long id) {
		if (id == null
			|| consumerRepository.findById(id).isEmpty()
			|| accountRepository.findByReferenceId(id).isEmpty()) {
			throw new IllegalArgumentException("Invalid ID");
		}
		accountRepository.deleteById(accountRepository.findByReferenceId(id).get().getId());
		consumerRepository.deleteById(id);
	}

}
