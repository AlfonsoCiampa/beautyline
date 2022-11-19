package com.beautyline.polimi.controller.consumer;

import com.beautyline.polimi.configuration.security.jwt.JWTService;
import com.beautyline.polimi.dto.GiftDTO;
import com.beautyline.polimi.entity.AccountEntity;
import com.beautyline.polimi.repository.AccountRepository;
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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.Map;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GiftControllerTest {

	private final HttpHeaders headers = new HttpHeaders() {{
		setBearerAuth("TEST_TOKEN");
	}};
	@MockBean
	private JWTService jwtService;
	@Autowired
	private TestRestTemplate restTemplate;
	@MockBean
	private AccountRepository accountRepository;
	@MockBean
	private GiftController giftController;

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
	public void getGiftDone() {
		// arrange
		HttpEntity<String> request = new HttpEntity<>(headers);

		// act
		ResponseEntity<GiftDTO> response = restTemplate.exchange("/consumer/gift/111", HttpMethod.GET, request, GiftDTO.class);

		// assert
		Assertions.assertEquals(200, response.getStatusCodeValue());
		Mockito.verify(giftController, Mockito.times(1)).getGift(111L);
	}

	@Test
	public void getGiftsDone() {
		// arrange
		HttpEntity<String> request = new HttpEntity<>(headers);
		ParameterizedTypeReference<Page<GiftDTO>> type = new ParameterizedTypeReference<>() {
		};

		// act
		ResponseEntity<Page<GiftDTO>> response = restTemplate.exchange("/consumer/gift/list", HttpMethod.GET, request, type);

		// assert
		Assertions.assertEquals(200, response.getStatusCodeValue());
		Mockito.verify(giftController, Mockito.times(1)).getGifts(null, null);
	}

}
