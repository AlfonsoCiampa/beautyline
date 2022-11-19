package com.beautyline.polimi.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Lob;
import javax.persistence.MappedSuperclass;
import java.math.BigDecimal;

@Getter
@Setter
@MappedSuperclass
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class ServiceSuperclass extends HibernateEntity {

	@Column(name = "name", nullable = false)
	private String name;

	@Lob
	@Column(name = "description", nullable = false)
	private String description;

	@Column(name = "price", nullable = false, precision = 7, scale = 2)
	private BigDecimal price;

	@Builder.Default
	@Column(name = "obscure", nullable = false)
	private Boolean obscure = false;


}