package com.beautyline.polimi.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class HibernateEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Long id;

	@Column(name = "createdOn", updatable = false)
	private LocalDateTime createdOn;

	@Column(name = "lastModified")
	private LocalDateTime lastModified;
}
