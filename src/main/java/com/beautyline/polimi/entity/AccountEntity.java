package com.beautyline.polimi.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@Table(name = "account")
public class AccountEntity extends HibernateEntity {

	@Column(name = "email", nullable = false, unique = true)
	private String email;

	@Column(name = "password", nullable = false)
	private String password;

	@Column(name = "referenceId")
	private Long referenceId;

	@Enumerated(EnumType.STRING)
	@Column(name = "referenceType", nullable = false)
	private Type referenceType;

	public enum Type {
		CONSUMER,
		ADMIN
	}
}