package com.beautyline.polimi.service.generic;

import com.beautyline.polimi.dto.ProductDTO;
import com.beautyline.polimi.dto.ServiceCommentDTO;
import com.beautyline.polimi.dto.TreatmentDTO;
import com.beautyline.polimi.entity.*;
import com.beautyline.polimi.repository.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ServiceServiceTest {

	@Autowired
	private ServiceService serviceService;
	@Autowired
	private TreatmentRepository treatmentRepository;
	@Autowired
	private ProductRepository productRepository;
	@Autowired
	private ServiceCommentRepository serviceCommentRepository;
	@Autowired
	private ConsumerRepository consumerRepository;
	@Autowired
	private AccountRepository accountRepository;

	@Test
	public void getTreatmentDone() {
		// arrange
		TreatmentEntity treatmentEntity = treatmentRepository.save(TreatmentEntity.builder()
			.name("Treatment1")
			.description("Is perfect for you")
			.duration(1800L)
			.price(BigDecimal.TEN)
			.obscure(false)
			.build());

		// act
		TreatmentDTO result = serviceService.getTreatment(treatmentEntity.getId());

		// assert
		Assertions.assertNotNull(result);
		Assertions.assertEquals(treatmentEntity.getId(), result.getId());
		Assertions.assertEquals(treatmentEntity.getName(), result.getName());
	}

	@Test
	public void getTreatmentsDone() {
		// arrange
		TreatmentEntity treatmentEntity = treatmentRepository.save(TreatmentEntity.builder()
			.name("Treatment1")
			.description("Is perfect for you")
			.duration(1800L)
			.price(BigDecimal.TEN)
			.obscure(false)
			.build());

		// act
		Page<TreatmentDTO> result = serviceService.getTreatments(null, null);

		// assert
		Assertions.assertNotNull(result);
		Assertions.assertEquals(1, result.getTotalElements());
		Assertions.assertEquals(1, result.getTotalPages());
		Assertions.assertEquals(0, result.getNumber());
		Assertions.assertEquals(20, result.getSize());
		TreatmentDTO treatment = result.get().findFirst().orElse(null);
		Assertions.assertNotNull(treatment);
		Assertions.assertEquals(treatmentEntity.getId(), treatment.getId());
		Assertions.assertEquals(treatmentEntity.getName(), treatment.getName());
	}

	@Test
	public void getTreatmentCommentsDone() {
		// arrange
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

		TreatmentEntity treatmentEntity = treatmentRepository.save(TreatmentEntity.builder()
			.name("Treatment1")
			.description("Is perfect for you")
			.duration(1800L)
			.price(BigDecimal.TEN)
			.obscure(false)
			.build());
		ServiceCommentEntity serviceCommentEntity = serviceCommentRepository.save(ServiceCommentEntity.builder()
			.consumerId(consumerEntity.getId())
			.serviceType(ServiceCommentEntity.Type.TREATMENT)
			.serviceId(treatmentEntity.getId())
			.description("Lorem ipsum dolor sit amet")
			.date(LocalDateTime.now())
			.build()
		);

		// act
		Page<ServiceCommentDTO> result = serviceService.getTreatmentComments(treatmentEntity.getId(), null, null);

		// assert
		Assertions.assertNotNull(result);
		Assertions.assertEquals(1, result.getTotalElements());
		Assertions.assertEquals(1, result.getTotalPages());
		Assertions.assertEquals(0, result.getNumber());
		Assertions.assertEquals(20, result.getSize());
		ServiceCommentDTO comment = result.get().findFirst().orElse(null);
		Assertions.assertNotNull(comment);
		Assertions.assertEquals(serviceCommentEntity.getId(), comment.getId());
		Assertions.assertEquals(serviceCommentEntity.getServiceType(), comment.getServiceType());
		Assertions.assertEquals(serviceCommentEntity.getServiceId(), comment.getServiceId());
		Assertions.assertEquals(serviceCommentEntity.getDescription(), comment.getDescription());
	}

	@Test
	public void getProductDone() {
		// arrange
		ProductEntity productEntity = productRepository.save(ProductEntity.builder()
			.name("Cream")
			.description("Cream")
			.price(BigDecimal.TEN)
			.quantity(100L)
			.obscure(false)
			.build());

		// act
		ProductDTO result = serviceService.getProduct(productEntity.getId());

		// assert
		Assertions.assertNotNull(result);
		Assertions.assertEquals(productEntity.getId(), result.getId());
		Assertions.assertEquals(productEntity.getName(), result.getName());
	}

	@Test
	public void getProductsDone() {
		// arrange
		ProductEntity productEntity = productRepository.save(ProductEntity.builder()
			.name("Cream")
			.description("Cream")
			.price(BigDecimal.TEN)
			.quantity(100L)
			.obscure(false)
			.build());

		// act
		Page<ProductDTO> result = serviceService.getProducts(null, null);

		// assert
		Assertions.assertNotNull(result);
		Assertions.assertEquals(1, result.getTotalElements());
		Assertions.assertEquals(1, result.getTotalPages());
		Assertions.assertEquals(0, result.getNumber());
		Assertions.assertEquals(20, result.getSize());
		ProductDTO product = result.get().findFirst().orElse(null);
		Assertions.assertNotNull(product);
		Assertions.assertEquals(productEntity.getId(), product.getId());
		Assertions.assertEquals(productEntity.getName(), product.getName());
	}

	@Test
	public void getProductCommentsDone() {
		// arrange
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

		ProductEntity productEntity = productRepository.save(ProductEntity.builder()
			.name("Cream")
			.description("Cream")
			.price(BigDecimal.TEN)
			.quantity(100L)
			.obscure(false)
			.build());
		ServiceCommentEntity serviceCommentEntity = serviceCommentRepository.save(ServiceCommentEntity.builder()
			.consumerId(consumerEntity.getId())
			.serviceType(ServiceCommentEntity.Type.PRODUCT)
			.serviceId(productEntity.getId())
			.description("Lorem ipsum dolor sit amet")
			.date(LocalDateTime.now())
			.build()
		);

		// act
		Page<ServiceCommentDTO> result = serviceService.getProductComments(productEntity.getId(), null, null);

		// assert
		Assertions.assertNotNull(result);
		Assertions.assertEquals(1, result.getTotalElements());
		Assertions.assertEquals(1, result.getTotalPages());
		Assertions.assertEquals(0, result.getNumber());
		Assertions.assertEquals(20, result.getSize());
		ServiceCommentDTO comment = result.get().findFirst().orElse(null);
		Assertions.assertNotNull(comment);
		Assertions.assertEquals(serviceCommentEntity.getId(), comment.getId());
		Assertions.assertEquals(serviceCommentEntity.getServiceType(), comment.getServiceType());
		Assertions.assertEquals(serviceCommentEntity.getServiceId(), comment.getServiceId());
		Assertions.assertEquals(serviceCommentEntity.getDescription(), comment.getDescription());
	}

	@Test
	public void getCommentDone() {
		// arrange
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

		TreatmentEntity treatmentEntity = treatmentRepository.save(TreatmentEntity.builder()
			.name("Treatment1")
			.description("Is perfect for you")
			.duration(1800L)
			.price(BigDecimal.TEN)
			.obscure(false)
			.build());
		ServiceCommentEntity serviceCommentEntity = serviceCommentRepository.save(ServiceCommentEntity.builder()
			.consumerId(consumerEntity.getId())
			.serviceType(ServiceCommentEntity.Type.TREATMENT)
			.serviceId(treatmentEntity.getId())
			.description("Lorem ipsum dolor sit amet")
			.date(LocalDateTime.now())
			.build()
		);

		// act
		ServiceCommentDTO result = serviceService.getComment(serviceCommentEntity.getId());

		// assert
		Assertions.assertNotNull(result);
		Assertions.assertEquals(serviceCommentEntity.getId(), result.getId());
		Assertions.assertEquals(serviceCommentEntity.getServiceType(), result.getServiceType());
		Assertions.assertEquals(serviceCommentEntity.getServiceId(), result.getServiceId());
		Assertions.assertEquals(serviceCommentEntity.getDescription(), result.getDescription());
	}
}
