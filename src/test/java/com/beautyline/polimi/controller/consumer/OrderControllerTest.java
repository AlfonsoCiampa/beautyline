package com.beautyline.polimi.controller.consumer;

import com.beautyline.polimi.configuration.security.jwt.JWTService;
import com.beautyline.polimi.dto.OrderDTO;
import com.beautyline.polimi.dto.PlaceOrderDTO;
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
public class OrderControllerTest {

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
	private OrderController orderController;

	@BeforeEach
	public void beforeEach() {
		AccountEntity.Type role = AccountEntity.Type.CONSUMER;
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
			.build())).when(accountRepository)
			.findByReferenceTypeAndEmailAndPassword(eq(role), eq(accountMail), eq(defaultPassword));
	}

	@Test
	public void getOrderDone() {
		// arrange
		HttpEntity<String> request = new HttpEntity<>(headers);

		// act
		ResponseEntity<OrderDTO> response = restTemplate.exchange("/consumer/order/111", HttpMethod.GET, request, OrderDTO.class);

		// assert
		Assertions.assertEquals(200, response.getStatusCodeValue());
		Mockito.verify(orderController, Mockito.times(1)).getOrder(111L);
	}

	@Test
	public void getOrdersDone() {
		// arrange
		HttpEntity<String> request = new HttpEntity<>(headers);
		ParameterizedTypeReference<Page<OrderDTO>> type = new ParameterizedTypeReference<>() {
		};

		// act
		ResponseEntity<Page<OrderDTO>> response = restTemplate.exchange("/consumer/order/list", HttpMethod.GET, request, type);

		// assert
		Assertions.assertEquals(200, response.getStatusCodeValue());
		Mockito.verify(orderController, Mockito.times(1)).getOrders(null, null);
	}

	@Test
	public void createDone() throws JsonProcessingException {
		// arrange
		headers.setContentType(MediaType.APPLICATION_JSON);

		String requestBody = mapperJson.writeValueAsString(new PlaceOrderDTO());
		HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

		// act
		ResponseEntity<OrderDTO> response = restTemplate.exchange("/consumer/order/create", HttpMethod.POST, request, OrderDTO.class);

		// assert
		Assertions.assertEquals(200, response.getStatusCodeValue());
		Mockito.verify(orderController, Mockito.times(1)).place(Mockito.any());
	}

}
