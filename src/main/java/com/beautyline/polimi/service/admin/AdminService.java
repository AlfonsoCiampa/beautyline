package com.beautyline.polimi.service.admin;

import com.beautyline.polimi.configuration.security.jwt.JWTService;
import com.beautyline.polimi.dto.AdminDTO;
import com.beautyline.polimi.dto.RegistrationDTO;
import com.beautyline.polimi.entity.AccountEntity;
import com.beautyline.polimi.mapper.AccountMapper;
import com.beautyline.polimi.mapper.AdminMapper;
import com.beautyline.polimi.repository.AccountRepository;
import com.beautyline.polimi.service.generic.AccessService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class AdminService {

	private final AccountRepository accountRepository;
	private final AccessService accessService;

	private AccountEntity getAdmin() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null) {
			User user = ((User) authentication.getPrincipal());

			if (!user.getAuthorities().contains(new SimpleGrantedAuthority(AccountEntity.Type.ADMIN.name()))) {
				throw new JWTService.TokenVerificationException();
			}

			return accountRepository.findByReferenceTypeAndEmail(AccountEntity.Type.ADMIN, user.getUsername())
				.orElseThrow(JWTService.TokenVerificationException::new);
		}
		throw new JWTService.TokenVerificationException();
	}

	public AdminDTO getAdminData(Long id) {
		return AdminMapper.entityToDto(accountRepository.findById(id).orElse(null));
	}

	public Page<AdminDTO> getAdminsData(Integer page, Integer size) {
		return AdminMapper.entitiesToDtoPage(accountRepository.findAllByReferenceType(
			AccountEntity.Type.ADMIN,
			PageRequest.of(Math.max(0, Optional.ofNullable(page).orElse(0)), Math.max(20, Optional.ofNullable(size).orElse(20)))
		));
	}

	public RegistrationDTO create(RegistrationDTO registrationDTO) {

		accessService.registrationDTOControls(registrationDTO, AccountEntity.Type.ADMIN, true);

		// mapping the accountEntity from RegistrationDTO, here we need to get the referenceId created in line 32
		AccountEntity accountEntity = AccountMapper.adminRegistrationDtoToAccountEntity(registrationDTO);
		accountRepository.save(accountEntity);

		return registrationDTO;
	}

	public RegistrationDTO update(RegistrationDTO registrationDTO) {

		accessService.registrationDTOControls(registrationDTO, AccountEntity.Type.ADMIN, false);

		AccountEntity accountEntity = AccountMapper.adminRegistrationDtoToAccountEntity(registrationDTO);
		accountEntity.setId(registrationDTO.getId());
		accountRepository.save(accountEntity);

		return registrationDTO;

	}

	public void delete(Long id) {
		if (id == null || accountRepository.findById(id).isEmpty() || getAdmin().getId().equals(id)) {
			throw new IllegalArgumentException("Invalid ID");
		}

		accountRepository.deleteById(id);

	}

}
