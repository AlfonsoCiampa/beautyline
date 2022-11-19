package com.beautyline.polimi.service.generic;

import com.beautyline.polimi.configuration.mail.CustomMailSender;
import com.beautyline.polimi.configuration.security.jwt.JWTAuthenticationService;
import com.beautyline.polimi.dto.LoginDTO;
import com.beautyline.polimi.dto.RegistrationDTO;
import com.beautyline.polimi.entity.AccountEntity;
import com.beautyline.polimi.entity.ConsumerEntity;
import com.beautyline.polimi.repository.AccountRepository;
import com.beautyline.polimi.repository.ConsumerRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.transaction.Transactional;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AccessServiceTest {
	@Autowired
	private AccountRepository accountRepository;
	@Autowired
	private ConsumerRepository consumerRepository;
	@Autowired
	private JWTAuthenticationService jwtAuthenticationService;
	@Autowired
	private AccessService accessService;
	@MockBean
	private CustomMailSender customMailSender;

	@Test
	public void registrationDone() {
		// arrange
		RegistrationDTO registrationDTO = new RegistrationDTO(null, "email@test.it", "password", "+393123567283", "Nicola", "Ciampa");

		// act
		String jwtToken = accessService.register(registrationDTO);

		// assert
		Assertions.assertEquals(1, accountRepository.findAll().size());
		AccountEntity accountEntity = accountRepository.findAll().get(0);
		Assertions.assertEquals(registrationDTO.getEmail(), accountEntity.getEmail());
		Assertions.assertEquals(registrationDTO.getPassword(), accountEntity.getPassword());
		Assertions.assertEquals(AccountEntity.Type.CONSUMER, accountEntity.getReferenceType());
		Assertions.assertEquals(1, consumerRepository.findAll().size());
		ConsumerEntity consumerEntity = consumerRepository.findAll().get(0);
		Assertions.assertEquals(accountEntity.getReferenceId(), consumerEntity.getId());
		Assertions.assertEquals(registrationDTO.getName(), consumerEntity.getName());
		Assertions.assertEquals(registrationDTO.getSurname(), consumerEntity.getSurname());
		Assertions.assertEquals(registrationDTO.getPhone(), consumerEntity.getPhone());

		Assertions.assertEquals(accountEntity.getReferenceId(), jwtAuthenticationService.authenticateByToken(jwtToken).getId());
	}

	@Test
	public void loginDone() {
		// arrange
		registrationDone();
		LoginDTO loginDTO = new LoginDTO("email@test.it", "password");

		// act
		String jwtToken = accessService.login(loginDTO);

		// assert
		AccountEntity accountEntity = accountRepository.findAll().get(0);
		Assertions.assertEquals(accountEntity.getReferenceId(), jwtAuthenticationService.authenticateByToken(jwtToken).getId());
	}

	// Query parameter
	@Test
	public void forgotPasswordDone() {

		// arrange
		registrationDone();
		String email = "email@test.it";
		Mockito.doReturn(true).when(customMailSender).sendResetConsumer(Mockito.any(), Mockito.any());

		// act
		accessService.forgotPassword(email);

		// assert
		Mockito.verify(customMailSender, Mockito.times(1)).sendResetConsumer(Mockito.eq(email), Mockito.any());
	}

}
