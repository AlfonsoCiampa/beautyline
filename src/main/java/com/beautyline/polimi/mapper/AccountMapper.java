package com.beautyline.polimi.mapper;

import com.beautyline.polimi.dto.RegistrationDTO;
import com.beautyline.polimi.entity.AccountEntity;

public class AccountMapper {

	public static AccountEntity consumerRegistrationDtoToAccountEntity(RegistrationDTO registrationDTO) {
		if (registrationDTO == null) return null;
		AccountEntity entity = new AccountEntity();
		entity.setEmail(registrationDTO.getEmail());
		entity.setPassword(registrationDTO.getPassword());
		entity.setReferenceId(registrationDTO.getId());
		entity.setReferenceType(AccountEntity.Type.CONSUMER);
		return entity;
	}

	public static AccountEntity adminRegistrationDtoToAccountEntity(RegistrationDTO registrationDTO) {
		if (registrationDTO == null) return null;
		AccountEntity entity = new AccountEntity();
		entity.setEmail(registrationDTO.getEmail());
		entity.setPassword(registrationDTO.getPassword());
		entity.setReferenceId(null);
		entity.setReferenceType(AccountEntity.Type.ADMIN);
		return entity;
	}

}
