package com.beautyline.polimi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationDTO implements Serializable {

	private Long id;
	private String email;
	private String password;
	private String phone;
	private String name;
	private String surname;
}
