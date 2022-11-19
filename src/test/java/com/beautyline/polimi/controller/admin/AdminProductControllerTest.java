package com.beautyline.polimi.controller.admin;

import com.beautyline.polimi.configuration.security.jwt.JWTService;
import com.beautyline.polimi.dto.ProductDTO;
import com.beautyline.polimi.entity.AccountEntity;
import com.beautyline.polimi.repository.AccountRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.http.*;

import java.util.Map;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AdminProductControllerTest {

	private final HttpHeaders headers = new HttpHeaders() {{
		setBearerAuth("TEST_TOKEN");
	}};
	@MockBean
	private JWTService jwtService;
	@Autowired
	private TestRestTemplate restTemplate;
	@Autowired
	private ObjectMapper mapperJson;
	@MockBean
	private AccountRepository accountRepository;
	@MockBean
	private AdminProductController productController;

	@BeforeEach
	public void beforeEach() {
		AccountEntity.Type role = AccountEntity.Type.ADMIN;
		String accountMail = "account@mail.it";
		String defaultPassword = "TestPassword123";

		doReturn(Map.of(
			"role", role.name(),
			"username", accountMail,
			"password", defaultPassword
		)).when(jwtService).verify(eq("TEST_TOKEN"));

		doReturn(Optional.of(AccountEntity.builder()
			.email(accountMail)
			.password(defaultPassword)
			.referenceType(role)
			.build())
		).when(accountRepository)
			.findByReferenceTypeAndEmailAndPassword(eq(role), eq(accountMail), eq(defaultPassword));
	}

	@Test
	public void getProductDone() {
		// arrange
		HttpEntity<String> request = new HttpEntity<>(headers);

		// act
		ResponseEntity<ProductDTO> response = restTemplate.exchange("/admin/product/111", HttpMethod.GET, request, ProductDTO.class);

		// assert
		Assertions.assertEquals(200, response.getStatusCodeValue());
		Mockito.verify(productController, Mockito.times(1)).getProduct(111L);
	}

	@Test
	public void getProductsDone() {
		// arrange
		HttpEntity<String> request = new HttpEntity<>(headers);
		ParameterizedTypeReference<Page<ProductDTO>> type = new ParameterizedTypeReference<>() {
		};

		// act
		ResponseEntity<Page<ProductDTO>> response = restTemplate.exchange("/admin/product/list", HttpMethod.GET, request, type);

		// assert
		Assertions.assertEquals(200, response.getStatusCodeValue());
		Mockito.verify(productController, Mockito.times(1)).getProducts(null, null);
	}

	@Test
	public void createDone() throws JsonProcessingException {
		// arrange
		ProductDTO productDTO = new ProductDTO();

		headers.setContentType(MediaType.APPLICATION_JSON);

		String requestBody = mapperJson.writeValueAsString(productDTO);
		HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

		// act
		ResponseEntity<ProductDTO> response = restTemplate.exchange("/admin/product/create", HttpMethod.POST, request, ProductDTO.class);

		// assert
		Assertions.assertEquals(200, response.getStatusCodeValue());
		Mockito.verify(productController, Mockito.times(1)).create(Mockito.any());
	}

	@Test
	public void updateDone() throws JsonProcessingException {
		// arrange
		ProductDTO productDTO = new ProductDTO();

		headers.setContentType(MediaType.APPLICATION_JSON);

		String requestBody = mapperJson.writeValueAsString(productDTO);
		HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

		// act
		ResponseEntity<ProductDTO> response = restTemplate.exchange("/admin/product/update", HttpMethod.POST, request, ProductDTO.class);

		// assert
		Assertions.assertEquals(200, response.getStatusCodeValue());
		Mockito.verify(productController, Mockito.times(1)).update(Mockito.any());
	}

	@Test
	public void deleteDone() throws JsonProcessingException {
		// arrange
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> request = new HttpEntity<>(null, headers);

		// act
		ResponseEntity<Void> response = restTemplate.exchange("/admin/product/111", HttpMethod.DELETE, request, Void.class);

		// assert
		Assertions.assertEquals(200, response.getStatusCodeValue());
		Mockito.verify(productController, Mockito.times(1)).delete(Mockito.any());
	}
}
