package com.beautyline.polimi.service.consumer;

import com.beautyline.polimi.dto.ServiceCommentDTO;
import com.beautyline.polimi.entity.AccountEntity;
import com.beautyline.polimi.entity.ConsumerEntity;
import com.beautyline.polimi.entity.ProductEntity;
import com.beautyline.polimi.entity.ServiceCommentEntity;
import com.beautyline.polimi.repository.AccountRepository;
import com.beautyline.polimi.repository.ConsumerRepository;
import com.beautyline.polimi.repository.ProductRepository;
import com.beautyline.polimi.repository.ServiceCommentRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.security.test.context.support.WithMockUser;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ServiceCommentServiceTest {

	@Autowired
	private AccountRepository accountRepository;
	@Autowired
	private ConsumerRepository consumerRepository;
	@Autowired
	private ServiceCommentRepository serviceCommentRepository;
	@Autowired
	private ServiceCommentService serviceCommentService;
	@Autowired
	private ProductRepository productRepository;

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
		accountRepository.save(AccountEntity.builder()
			.email(accountMail)
			.password(defaultPassword)
			.referenceType(role)
			.referenceId(consumerEntity.getId())
			.build()
		);
	}

	@Test
	@WithMockUser(username = "account@mail.it", password = "TestPassword123", authorities = {"CONSUMER"})
	public void getCommentsDone() {
		// arrange
		createDone();
		ServiceCommentEntity serviceCommentEntity = serviceCommentRepository.findAll().get(0);

		// act
		Page<ServiceCommentDTO> result = serviceCommentService.getComments(null, null);

		// assert
		Assertions.assertNotNull(result);
		Assertions.assertEquals(1, result.getTotalElements());
		Assertions.assertEquals(1, result.getTotalPages());
		Assertions.assertEquals(0, result.getNumber());
		Assertions.assertEquals(20, result.getSize());
		ServiceCommentDTO serviceComment = result.get().findFirst().orElse(null);
		Assertions.assertNotNull(serviceComment);
		Assertions.assertEquals(serviceCommentEntity.getId(), serviceComment.getId());
		Assertions.assertEquals(serviceCommentEntity.getDescription(), serviceComment.getDescription());
	}

	@Test
	@WithMockUser(username = "account@mail.it", password = "TestPassword123", authorities = {"CONSUMER"})
	public void createDone() {
		// arrange
		ConsumerEntity consumerEntity = consumerRepository.findAll().get(0);

		ProductEntity productEntity = productRepository.save(ProductEntity.builder()
			.name("Cream")
			.description("Cream")
			.price(BigDecimal.TEN)
			.quantity(100L)
			.obscure(false)
			.build());

		ServiceCommentDTO serviceCommentDTO = new ServiceCommentDTO(null, consumerEntity.getId(), productEntity.getId(),
			ServiceCommentEntity.Type.PRODUCT, "I love this product!", LocalDateTime.now());

		// act
		serviceCommentService.create(serviceCommentDTO);

		// assert
		Assertions.assertEquals(1, serviceCommentRepository.count());
		ServiceCommentEntity serviceCommentEntity = serviceCommentRepository.findAll().get(0);
		Assertions.assertEquals(serviceCommentDTO.getConsumerId(), serviceCommentEntity.getConsumerId());
		Assertions.assertEquals(serviceCommentDTO.getServiceId(), serviceCommentEntity.getServiceId());
		Assertions.assertEquals(serviceCommentDTO.getServiceType(), serviceCommentEntity.getServiceType());
		Assertions.assertEquals(serviceCommentDTO.getDescription(), serviceCommentEntity.getDescription());
		Assertions.assertEquals(serviceCommentDTO.getDate(), serviceCommentEntity.getDate());
	}

	@Test
	@WithMockUser(username = "account@mail.it", password = "TestPassword123", authorities = {"CONSUMER"})
	public void updateDone() {
		// arrange
		createDone();
		ServiceCommentEntity serviceCommentEntity = serviceCommentRepository.findAll().get(0);
		ServiceCommentDTO serviceCommentDTO = new ServiceCommentDTO(serviceCommentEntity.getId(),
			serviceCommentEntity.getConsumerId(), serviceCommentEntity.getServiceId(),
			ServiceCommentEntity.Type.PRODUCT, "I hate this product!", LocalDateTime.now());

		// act
		serviceCommentService.update(serviceCommentDTO);

		// assert
		Assertions.assertEquals(1, serviceCommentRepository.count());
		ServiceCommentEntity serviceCommentEntity2 = serviceCommentRepository.findAll().get(0);
		Assertions.assertEquals(serviceCommentDTO.getId(), serviceCommentEntity2.getId());
		Assertions.assertEquals(serviceCommentDTO.getConsumerId(), serviceCommentEntity2.getConsumerId());
		Assertions.assertEquals(serviceCommentDTO.getServiceId(), serviceCommentEntity2.getServiceId());
		Assertions.assertEquals(serviceCommentDTO.getServiceType(), serviceCommentEntity2.getServiceType());
		Assertions.assertEquals(serviceCommentDTO.getDescription(), serviceCommentEntity2.getDescription());
		Assertions.assertEquals(serviceCommentDTO.getDate(), serviceCommentEntity2.getDate());
	}

	@Test
	@WithMockUser(username = "account@mail.it", password = "TestPassword123", authorities = {"CONSUMER"})
	public void deleteDone() {
		// arrange
		createDone();
		ServiceCommentEntity serviceCommentEntity = serviceCommentRepository.findAll().get(0);

		// act
		serviceCommentService.delete(serviceCommentEntity.getId());

		// assert
		Assertions.assertEquals(0, serviceCommentRepository.count());
	}

}
