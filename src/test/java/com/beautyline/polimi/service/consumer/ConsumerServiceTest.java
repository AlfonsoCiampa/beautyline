package com.beautyline.polimi.service.consumer;

import com.beautyline.polimi.dto.ConsumerDTO;
import com.beautyline.polimi.dto.RegistrationDTO;
import com.beautyline.polimi.entity.AccountEntity;
import com.beautyline.polimi.entity.ConsumerEntity;
import com.beautyline.polimi.repository.AccountRepository;
import com.beautyline.polimi.repository.ConsumerRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;

import javax.transaction.Transactional;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ConsumerServiceTest {

	@Autowired
	private AccountRepository accountRepository;
	@Autowired
	private ConsumerRepository consumerRepository;
	@Autowired
	private ConsumerService consumerService;

	@BeforeEach
	public void beforeEach() {
		AccountEntity.Type role = AccountEntity.Type.CONSUMER;
		String accountMail = "account@mail.it";
		String defaultPassword = "TestPassword123";

		ConsumerEntity consumerEntity = consumerRepository.save(ConsumerEntity.builder()
			.name("Nicola")
			.surname("Ciampa")
			.phone("+393123567283")
			.build());
		AccountEntity accountEntity = accountRepository.save(AccountEntity.builder()
			.email(accountMail)
			.password(defaultPassword)
			.referenceType(role)
			.referenceId(consumerEntity.getId())
			.build()
		);
		consumerEntity.setAccount(accountEntity);
		consumerRepository.save(consumerEntity);
	}

	@Test
	@WithMockUser(username = "account@mail.it", password = "TestPassword123", authorities = {"CONSUMER"})
	public void getConsumerDone() {
		// arrange
		ConsumerEntity consumerEntity = consumerRepository.findAll().get(0);

		// act
		ConsumerDTO result = consumerService.getData();

		// assert
		Assertions.assertNotNull(result);
		Assertions.assertEquals(consumerEntity.getId(), result.getId());
		Assertions.assertEquals(consumerEntity.getName(), result.getName());
	}

	@Test
	@WithMockUser(username = "account@mail.it", password = "TestPassword123", authorities = {"CONSUMER"})
	public void updateDone() {
		// arrange
		ConsumerEntity consumerEntity = consumerRepository.findAll().get(0);
		RegistrationDTO registrationDTO = new RegistrationDTO(consumerEntity.getId(), "email2@test.it", "Password2", "+383123567283", "Even", "Pepa");

		// act
		consumerService.update(registrationDTO);

		// assert
		Assertions.assertEquals(1, consumerRepository.count());
		consumerEntity = consumerRepository.findAll().get(0);
		Assertions.assertEquals(registrationDTO.getId(), consumerEntity.getId());
		Assertions.assertEquals(registrationDTO.getName(), consumerEntity.getName());
		Assertions.assertEquals(registrationDTO.getSurname(), consumerEntity.getSurname());
		Assertions.assertEquals(registrationDTO.getPhone(), consumerEntity.getPhone());

		Assertions.assertEquals(1, accountRepository.count());
		AccountEntity accountEntity = accountRepository.findByReferenceTypeAndEmail(AccountEntity.Type.CONSUMER, registrationDTO.getEmail()).get();
		Assertions.assertEquals(registrationDTO.getEmail(), accountEntity.getEmail());
		Assertions.assertEquals(registrationDTO.getPassword(), accountEntity.getPassword());
		Assertions.assertEquals(registrationDTO.getId(), accountEntity.getReferenceId());
		Assertions.assertEquals(AccountEntity.Type.CONSUMER, accountEntity.getReferenceType());
	}

}
