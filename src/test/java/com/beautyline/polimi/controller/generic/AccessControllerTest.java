package com.beautyline.polimi.controller.generic;

import com.beautyline.polimi.dto.LoginDTO;
import com.beautyline.polimi.dto.RegistrationDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.util.Map;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AccessControllerTest {
	@Autowired
	private TestRestTemplate restTemplate;
	@Autowired
	private ObjectMapper mapperJson;
	@MockBean
	private AccessController accessController;

	@Test
	public void registrationDone() throws JsonProcessingException {
		// arrange
		RegistrationDTO registrationDTO = new RegistrationDTO();

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		String requestBody = mapperJson.writeValueAsString(registrationDTO);
		HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

		// act
		ResponseEntity<String> response = restTemplate.exchange("/generic/access/register", HttpMethod.POST, request, String.class);

		// assert
		Assertions.assertEquals(200, response.getStatusCodeValue());
		Mockito.verify(accessController, Mockito.times(1)).register(Mockito.any());
	}

	@Test
	public void loginDone() throws JsonProcessingException {
		// arrange
		LoginDTO loginDTO = new LoginDTO();

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		String requestBody = mapperJson.writeValueAsString(loginDTO);
		HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

		// act
		ResponseEntity<String> response = restTemplate.exchange("/generic/access/login", HttpMethod.POST, request, String.class);

		// assert
		Assertions.assertEquals(200, response.getStatusCodeValue());
		Mockito.verify(accessController, Mockito.times(1)).login(Mockito.any());
	}

	// Query parameter
	@Test
	public void forgotPasswordDone() throws JsonProcessingException {

		// arrange
		String email = "admin@test.com";

		// act
		ResponseEntity<Void> response = restTemplate.postForEntity("/generic/access/forgotPassword?email={email}",
			HttpEntity.EMPTY, Void.class, Map.of("email", email));

		// assert
		Assertions.assertEquals(200, response.getStatusCodeValue());
		Mockito.verify(accessController, Mockito.times(1)).forgotPassword(Mockito.any());
	}

}
