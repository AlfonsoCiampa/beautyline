package com.beautyline.polimi.configuration.security.jwt;

import com.beautyline.polimi.configuration.mail.CustomMailSender;
import com.beautyline.polimi.entity.AccountEntity;
import com.beautyline.polimi.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import net.bytebuddy.utility.RandomString;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class JWTAuthenticationService {
	private final JWTService jwtService;
	private final CustomMailSender customMailSender;
	private final AccountRepository accountRepository;

	public String login(AccountEntity.Type type, String username, String password) throws BadCredentialsException {
		return accountRepository
			.findByReferenceTypeAndEmailAndPassword(type, username, password)
			.map(user -> jwtService.create(type, username, password))
			.orElseThrow(() -> new BadCredentialsException("Invalid username or password"));
	}

	public void reset(AccountEntity.Type type, String email) throws BadCredentialsException {
		accountRepository
			.findByReferenceTypeAndEmail(type, email)
			.ifPresentOrElse(
				account -> {
					String newPassword = RandomString.make(8);
					account.setPassword(DigestUtils.sha3_256Hex(newPassword));
					accountRepository.save(account);
					if (type == AccountEntity.Type.CONSUMER) {
						if (!customMailSender.sendResetConsumer(email, newPassword)) {
							throw new RuntimeException("Something went wrong with email");
						}
					} else {
						throw new RuntimeException("Something went wrong with reset");
					}
				},
				() -> {
					throw new BadCredentialsException("Invalid email");
				});
	}

	public AccountEntity authenticateByToken(String token) {
		try {
			Map<String, Object> data = jwtService.verify(token);
			AccountEntity.Type role = AccountEntity.Type.valueOf(String.valueOf(data.get("role")));
			String username = String.valueOf(data.get("username"));
			String password = String.valueOf(data.get("password"));
			return accountRepository.findByReferenceTypeAndEmailAndPassword(role, username, password)
				.orElseThrow(() -> new UsernameNotFoundException("Authentication fail"));
		} catch (Exception e) {
			throw new BadCredentialsException("Invalid token");
		}
	}
}