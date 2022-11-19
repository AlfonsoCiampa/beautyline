package com.beautyline.polimi.service.admin;

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
import org.springframework.data.domain.Page;
import org.springframework.security.test.context.support.WithMockUser;

import javax.transaction.Transactional;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AdminConsumerServiceTest {

	@Autowired
	private AccountRepository accountRepository;
	@Autowired
	private ConsumerRepository consumerRepository;
	@Autowired
	private AdminConsumerService adminConsumerService;

	@BeforeEach
	public void beforeEach() {
		AccountEntity.Type role = AccountEntity.Type.ADMIN;
		String accountMail = "account@mail.it";
		String defaultPassword = "TestPassword123";

		accountRepository.save(AccountEntity.builder()
			.email(accountMail)
			.password(defaultPassword)
			.referenceType(role)
			.build()
		);
	}

	@Test
	@WithMockUser(username = "account@mail.it", password = "TestPassword123", authorities = {"ADMIN"})
	public void getConsumerDone() {
		// arrange
		createDone();
		ConsumerEntity consumerEntity = consumerRepository.findAll().get(0);
		AccountEntity accountEntity = accountRepository.findAll().get(0);
		consumerEntity.setAccount(accountEntity);

		// act
		ConsumerDTO result = adminConsumerService.getConsumer(consumerEntity.getId());

		// assert
		Assertions.assertNotNull(result);
		Assertions.assertEquals(consumerEntity.getId(), result.getId());
		Assertions.assertEquals(consumerEntity.getName(), result.getName());
	}

	@Test
	@WithMockUser(username = "account@mail.it", password = "TestPassword123", authorities = {"ADMIN"})
	public void getConsumersDone() {
		// arrange
		createDone();
		ConsumerEntity consumerEntity = consumerRepository.findAll().get(0);
		AccountEntity accountEntity = accountRepository.findAll().get(0);
		consumerEntity.setAccount(accountEntity);

		// act
		Page<ConsumerDTO> result = adminConsumerService.getConsumers(null, null);

		// assert
		Assertions.assertNotNull(result);
		Assertions.assertEquals(1, result.getTotalElements());
		Assertions.assertEquals(1, result.getTotalPages());
		Assertions.assertEquals(0, result.getNumber());
		Assertions.assertEquals(20, result.getSize());
		ConsumerDTO consumer = result.get().findFirst().orElse(null);
		Assertions.assertNotNull(consumer);
		Assertions.assertEquals(consumerEntity.getId(), consumer.getId());
		Assertions.assertEquals(consumerEntity.getName(), consumer.getName());
	}

	@Test
	@WithMockUser(username = "account@mail.it", password = "TestPassword123", authorities = {"ADMIN"})
	public void createDone() {
		// arrange
		RegistrationDTO registrationDTO = new RegistrationDTO(null, "email@test.it", "password", "+393123567283", "Nicola", "Ciampa");

		// act
		adminConsumerService.create(registrationDTO);

		// assert
		Assertions.assertEquals(1, consumerRepository.count());
		ConsumerEntity consumerEntity = consumerRepository.findAll().get(0);
		Assertions.assertEquals(registrationDTO.getName(), consumerEntity.getName());
		Assertions.assertEquals(registrationDTO.getSurname(), consumerEntity.getSurname());
		Assertions.assertEquals(registrationDTO.getPhone(), consumerEntity.getPhone());

		Assertions.assertEquals(2, accountRepository.count());
		AccountEntity accountEntity = accountRepository.findByReferenceTypeAndEmail(AccountEntity.Type.CONSUMER, registrationDTO.getEmail()).get();
		Assertions.assertEquals(registrationDTO.getEmail(), accountEntity.getEmail());
		Assertions.assertEquals(registrationDTO.getPassword(), accountEntity.getPassword());
		Assertions.assertEquals(consumerEntity.getId(), accountEntity.getReferenceId());
		Assertions.assertEquals(AccountEntity.Type.CONSUMER, accountEntity.getReferenceType());
	}

	@Test
	@WithMockUser(username = "account@mail.it", password = "TestPassword123", authorities = {"ADMIN"})
	public void updateDone() {
		// arrange
		createDone();
		AccountEntity accountEntity = accountRepository.findByReferenceTypeAndEmail(AccountEntity.Type.CONSUMER, "email@test.it").get();
		RegistrationDTO registrationDTO = new RegistrationDTO(accountEntity.getReferenceId(), "email2@test.it", "password", "+393123567283", "Even", "Ciampa");

		// act
		adminConsumerService.update(registrationDTO);

		// assert
		Assertions.assertEquals(1, consumerRepository.count());
		ConsumerEntity consumerEntity = consumerRepository.findAll().get(0);
		Assertions.assertEquals(registrationDTO.getName(), consumerEntity.getName());
		Assertions.assertEquals(registrationDTO.getSurname(), consumerEntity.getSurname());
		Assertions.assertEquals(registrationDTO.getPhone(), consumerEntity.getPhone());

		Assertions.assertEquals(2, accountRepository.count());
		AccountEntity accountEntity2 = accountRepository.findByReferenceTypeAndEmail(AccountEntity.Type.CONSUMER, registrationDTO.getEmail()).get();
		Assertions.assertEquals(registrationDTO.getEmail(), accountEntity2.getEmail());
		Assertions.assertEquals(registrationDTO.getPassword(), accountEntity2.getPassword());
		Assertions.assertEquals(consumerEntity.getId(), accountEntity2.getReferenceId());
		Assertions.assertEquals(AccountEntity.Type.CONSUMER, accountEntity2.getReferenceType());
	}

	@Test
	@WithMockUser(username = "account@mail.it", password = "TestPassword123", authorities = {"ADMIN"})
	public void deleteDone() {
		// arrange
		createDone();
		AccountEntity accountEntity = accountRepository.findByReferenceTypeAndEmail(AccountEntity.Type.CONSUMER, "email@test.it").get();

		// act
		adminConsumerService.delete(accountEntity.getReferenceId());

		// assert
		Assertions.assertEquals(1, accountRepository.count());
		Assertions.assertEquals(0, consumerRepository.count());
	}

}
