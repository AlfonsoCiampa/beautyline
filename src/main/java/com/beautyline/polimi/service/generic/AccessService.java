package com.beautyline.polimi.service.generic;

import com.beautyline.polimi.configuration.security.jwt.JWTAuthenticationService;
import com.beautyline.polimi.configuration.security.jwt.JWTService;
import com.beautyline.polimi.dto.LoginDTO;
import com.beautyline.polimi.dto.RegistrationDTO;
import com.beautyline.polimi.entity.AccountEntity;
import com.beautyline.polimi.entity.ConsumerEntity;
import com.beautyline.polimi.mapper.AccountMapper;
import com.beautyline.polimi.mapper.ConsumerMapper;
import com.beautyline.polimi.repository.AccountRepository;
import com.beautyline.polimi.repository.ConsumerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
@Transactional
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class AccessService {

	private final AccountRepository accountRepository;
	private final ConsumerRepository consumerRepository;
	private final JWTService jwtService;
	private final JWTAuthenticationService jwtAuthenticationService;

	public String register(RegistrationDTO registrationDTO) {

		registrationDTOControls(registrationDTO, AccountEntity.Type.CONSUMER, true);

		ConsumerEntity consumerEntity = ConsumerMapper.registrationDtoToConsumerEntity(registrationDTO);
		consumerEntity = consumerRepository.save(consumerEntity); // metti sempre l'uguale se usi la variabile dopo,
		// altrimenti la variabile non viene modificata

		// mapping the accountEntity from RegistrationDTO, here we need to get the referenceId created in line 35
		AccountEntity accountEntity = AccountMapper.consumerRegistrationDtoToAccountEntity(registrationDTO);
		accountEntity.setReferenceId(consumerEntity.getId());
		accountRepository.save(accountEntity);

		return jwtService.create(AccountEntity.Type.CONSUMER, registrationDTO.getEmail(), registrationDTO.getPassword());
	}

	public String login(LoginDTO loginDTO) {

		// email validation
		if (loginDTO.getEmail() == null || Pattern.matches("^(.+)@(\\\\S+)$", loginDTO.getEmail())) {
			throw new IllegalArgumentException("Invalid email");
		}

		// password validation
		if (loginDTO.getPassword() == null || Pattern.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$", loginDTO.getPassword())) {
			throw new IllegalArgumentException("Invalid password");
		}

		Optional<AccountEntity> accountEntity = accountRepository.findByEmailAndPassword(loginDTO.getEmail(), loginDTO.getPassword());
		boolean exists = accountEntity.isPresent();

		if (exists) {
			return jwtService.create(accountEntity.get().getReferenceType(), loginDTO.getEmail(), loginDTO.getPassword());
		} else
			return null;
	}

	public void forgotPassword(String email) {

		// email validation
		if (email == null || Pattern.matches("^(.+)@(\\\\S+)$", email)) {
			throw new IllegalArgumentException("Invalid email");
		}

		jwtAuthenticationService.reset(AccountEntity.Type.CONSUMER, email);
	}

	public void registrationDTOControls(RegistrationDTO registrationDTO, AccountEntity.Type accountType, boolean create) {

		// consumer ID validation
		if (accountType == AccountEntity.Type.CONSUMER) {
			if (!create && consumerRepository.findById(registrationDTO.getId()).isEmpty()) {
				throw new IllegalArgumentException("Invalid ID");
			}
		} else if (accountType == AccountEntity.Type.ADMIN) {
			if (!create && (accountRepository.findById(registrationDTO.getId()).isEmpty()
				|| accountRepository.findById(registrationDTO.getId()).get().getReferenceType() != AccountEntity.Type.ADMIN)) {
				throw new IllegalArgumentException("Invalid ID");
			}
		} else {
			throw new IllegalStateException();
		}

		// phone validation
		String regex = "^(\\+\\d{1,3}( )?)?((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}$";
		if (registrationDTO.getPhone() == null || !Pattern.matches(regex, registrationDTO.getPhone())) {
			throw new IllegalArgumentException("Invalid phone number");
		}

		// email validation
		regex = "^(.+)@(\\\\S+)$";
		if (registrationDTO.getEmail() == null || Pattern.matches(regex, registrationDTO.getEmail())) {
			throw new IllegalArgumentException("Invalid email");
		}

		// password validation
		regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
		if (registrationDTO.getPassword() == null || Pattern.matches(regex, registrationDTO.getPassword())) {
			throw new IllegalArgumentException("Invalid password");
		}

		// email validation
		if (create
			? accountRepository.findByEmail(registrationDTO.getEmail()).isPresent()
			: accountRepository.findByEmail(registrationDTO.getEmail())
			.map(a -> !registrationDTO.getId().equals(a.getId())).orElse(false)
		) {
			throw new IllegalArgumentException("Invalid email");
		}
	}
}
