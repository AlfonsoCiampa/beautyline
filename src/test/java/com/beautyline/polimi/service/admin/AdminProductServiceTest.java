package com.beautyline.polimi.service.admin;

import com.beautyline.polimi.dto.ProductDTO;
import com.beautyline.polimi.entity.AccountEntity;
import com.beautyline.polimi.entity.ProductEntity;
import com.beautyline.polimi.repository.AccountRepository;
import com.beautyline.polimi.repository.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.security.test.context.support.WithMockUser;

import javax.transaction.Transactional;
import java.math.BigDecimal;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AdminProductServiceTest {

	@Autowired
	private AccountRepository accountRepository;
	@Autowired
	private ProductRepository productRepository;
	@Autowired
	private AdminProductService adminProductService;

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
	public void getProductDone() {
		// arrange
		createDone();
		ProductEntity productEntity = productRepository.findAll().get(0);

		// act
		ProductDTO result = adminProductService.getProduct(productEntity.getId());

		// assert
		Assertions.assertNotNull(result);
		Assertions.assertEquals(productEntity.getId(), result.getId());
		Assertions.assertEquals(productEntity.getName(), result.getName());
	}

	@Test
	@WithMockUser(username = "account@mail.it", password = "TestPassword123", authorities = {"ADMIN"})
	public void getProductsDone() {
		// arrange
		createDone();
		ProductEntity productEntity = productRepository.findAll().get(0);

		// act
		Page<ProductDTO> result = adminProductService.getProducts(null, null);

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
	@WithMockUser(username = "account@mail.it", password = "TestPassword123", authorities = {"ADMIN"})
	public void createDone() {
		// arrange
		ProductDTO productDTO = new ProductDTO(null, "Cream", "Beauty", 3L, BigDecimal.TEN, false);

		// act
		adminProductService.create(productDTO);

		// assert
		Assertions.assertEquals(1, productRepository.count());
		ProductEntity productEntity = productRepository.findAll().get(0);
		Assertions.assertEquals(productDTO.getName(), productEntity.getName());
		Assertions.assertEquals(productDTO.getDescription(), productEntity.getDescription());
		Assertions.assertEquals(productDTO.getQuantity(), productEntity.getQuantity());
		Assertions.assertEquals(productDTO.getPrice(), productEntity.getPrice());
		Assertions.assertEquals(productDTO.getObscure(), productEntity.getObscure());
	}

	@Test
	@WithMockUser(username = "account@mail.it", password = "TestPassword123", authorities = {"ADMIN"})
	public void updateDone() {
		// arrange
		createDone();
		ProductEntity productEntity = productRepository.findAll().get(0);
		ProductDTO productDTO = new ProductDTO(productEntity.getId(), "Cream2", "Beauty", 5L, BigDecimal.TEN, true);

		// act
		adminProductService.update(productDTO);

		// assert
		Assertions.assertEquals(1, productRepository.count());
		ProductEntity productEntity2 = productRepository.findAll().get(0);
		Assertions.assertEquals(productDTO.getId(), productEntity2.getId());
		Assertions.assertEquals(productDTO.getName(), productEntity2.getName());
		Assertions.assertEquals(productDTO.getDescription(), productEntity2.getDescription());
		Assertions.assertEquals(productDTO.getQuantity(), productEntity2.getQuantity());
		Assertions.assertEquals(productDTO.getPrice(), productEntity2.getPrice());
		Assertions.assertEquals(productDTO.getObscure(), productEntity2.getObscure());
	}

	@Test
	@WithMockUser(username = "account@mail.it", password = "TestPassword123", authorities = {"ADMIN"})
	public void deleteDone() {
		// arrange
		createDone();
		ProductEntity productEntity = productRepository.findAll().get(0);
		ProductDTO productDTO = new ProductDTO(productEntity.getId(), productEntity.getName(), productEntity.getDescription(), productEntity.getQuantity(), productEntity.getPrice(), productEntity.getObscure());

		// act
		adminProductService.delete(productDTO.getId());

		// assert
		Assertions.assertEquals(0, productRepository.count());
	}
}
