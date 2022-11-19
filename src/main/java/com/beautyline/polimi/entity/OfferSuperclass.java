package com.beautyline.polimi.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Lob;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class OfferSuperclass extends HibernateEntity {

	@Column(name = "name", nullable = false)
	private String name;

	@Lob
	@Column(name = "description", nullable = false)
	private String description;

	@Column(name = "startDate", nullable = false)
	private LocalDateTime startDate;

	@Column(name = "endDate", nullable = false)
	private LocalDateTime endDate;

}