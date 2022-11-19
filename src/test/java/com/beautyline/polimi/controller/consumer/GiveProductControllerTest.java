package com.beautyline.polimi.controller.consumer;

import com.beautyline.polimi.configuration.security.jwt.JWTService;
import com.beautyline.polimi.dto.GiveProductDTO;
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
import org.springframework.http.*;

import java.util.Map;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GiveProductControllerTest {

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
	private GiveProductController giveProductController;

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
	public void createDone() throws JsonProcessingException {
		// arrange

		GiveProductDTO giveProductDTO = new GiveProductDTO();

		headers.setContentType(MediaType.APPLICATION_JSON);

		String requestBody = mapperJson.writeValueAsString(giveProductDTO);
		HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

		// act
		ResponseEntity<GiveProductDTO> response = restTemplate.exchange("/consumer/giveProduct/create", HttpMethod.POST, request, GiveProductDTO.class);

		// assert
		Assertions.assertEquals(200, response.getStatusCodeValue());
		Mockito.verify(giveProductController, Mockito.times(1)).create(Mockito.any());
	}

}
