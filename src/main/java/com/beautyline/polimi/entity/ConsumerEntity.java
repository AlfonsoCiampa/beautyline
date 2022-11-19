package com.beautyline.polimi.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.WhereJoinTable;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@Table(name = "consumer")
public class ConsumerEntity extends HibernateEntity {

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "surname", nullable = false)
	private String surname;

	@Column(name = "phone", nullable = false)
	private String phone;

	@OneToOne
	@WhereJoinTable(clause = "referenceType = 'CONSUMER'")
	@JoinColumn(name = "id", referencedColumnName = "referenceId", nullable = false, updatable = false, insertable = false)
	private AccountEntity account;
}