package com.beautyline.polimi.controller.admin;

import com.beautyline.polimi.configuration.security.jwt.JWTService;
import com.beautyline.polimi.dto.EventDTO;
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
public class AdminEventControllerTest {

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
	private AdminEventController eventController;

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
	public void getEventDone() {
		// arrange
		HttpEntity<String> request = new HttpEntity<>(headers);

		// act
		ResponseEntity<EventDTO> response = restTemplate.exchange("/admin/event/111", HttpMethod.GET, request, EventDTO.class);

		// assert
		Assertions.assertEquals(200, response.getStatusCodeValue());
		Mockito.verify(eventController, Mockito.times(1)).getEvent(111L);
	}

	@Test
	public void getEventsDone() {
		// arrange
		HttpEntity<String> request = new HttpEntity<>(headers);
		ParameterizedTypeReference<Page<EventDTO>> type = new ParameterizedTypeReference<>() {
		};

		// act
		ResponseEntity<Page<EventDTO>> response = restTemplate.exchange("/admin/event/list", HttpMethod.GET, request, type);

		// assert
		Assertions.assertEquals(200, response.getStatusCodeValue());
		Mockito.verify(eventController, Mockito.times(1)).getEvents(null, null);
	}

	@Test
	public void createDone() throws JsonProcessingException {
		// arrange
		EventDTO eventDTO = new EventDTO();

		headers.setContentType(MediaType.APPLICATION_JSON);

		String requestBody = mapperJson.writeValueAsString(eventDTO);
		HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

		// act
		ResponseEntity<EventDTO> response = restTemplate.exchange("/admin/event/create", HttpMethod.POST, request, EventDTO.class);

		// assert
		Assertions.assertEquals(200, response.getStatusCodeValue());
		Mockito.verify(eventController, Mockito.times(1)).create(Mockito.any());
	}

	@Test
	public void updateDone() throws JsonProcessingException {
		// arrange
		EventDTO eventDTO = new EventDTO();

		headers.setContentType(MediaType.APPLICATION_JSON);

		String requestBody = mapperJson.writeValueAsString(eventDTO);
		HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

		// act
		ResponseEntity<EventDTO> response = restTemplate.exchange("/admin/event/update", HttpMethod.POST, request, EventDTO.class);

		// assert
		Assertions.assertEquals(200, response.getStatusCodeValue());
		Mockito.verify(eventController, Mockito.times(1)).update(Mockito.any());
	}

	@Test
	public void deleteDone() throws JsonProcessingException {
		// arrange
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> request = new HttpEntity<>(null, headers);

		// act
		ResponseEntity<Void> response = restTemplate.exchange("/admin/event/111", HttpMethod.DELETE, request, Void.class);

		// assert
		Assertions.assertEquals(200, response.getStatusCodeValue());
		Mockito.verify(eventController, Mockito.times(1)).delete(Mockito.any());
	}

}