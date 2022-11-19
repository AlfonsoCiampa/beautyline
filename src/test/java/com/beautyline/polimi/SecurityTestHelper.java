package com.beautyline.polimi;

import com.beautyline.polimi.entity.AccountEntity;
import com.beautyline.polimi.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.function.Supplier;

@Service
@Transactional
public class SecurityTestHelper {
	@Autowired
	private AccountRepository accountRepository;

	public <O> O executeAsConsumer(String accountMail, Supplier<O> logic) {
		SecurityContext oldContext = SecurityContextHolder.getContext();

		SecurityContext context = SecurityContextHolder.createEmptyContext();

		List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(AccountEntity.Type.CONSUMER.name()));
		Authentication auth =
			accountRepository.findByReferenceTypeAndEmail(AccountEntity.Type.CONSUMER, accountMail)
				.map(a -> UsernamePasswordAuthenticationToken.authenticated(
					new User(a.getEmail(), a.getPassword(), authorities), null, authorities))
				.orElseThrow(() -> new IllegalStateException("Invalid accountMail"));
		context.setAuthentication(auth);
		SecurityContextHolder.setContext(context);

		O result = logic.get();

		SecurityContextHolder.setContext(oldContext);
		return result;
	}
}
