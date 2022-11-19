package com.beautyline.polimi.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@Table(name = "serviceComment")
public class ServiceCommentEntity extends HibernateEntity {

	@Column(name = "consumerId", nullable = false)
	private Long consumerId;

	@Column(name = "serviceId", nullable = false)
	private Long serviceId;

	@Column(name = "serviceType", nullable = false)
	private Type serviceType;

	@Lob
	@Column(name = "description", nullable = false)
	private String description;

	@Column(name = "date", nullable = false)
	private LocalDateTime date;

	public enum Type {
		TREATMENT,
		PRODUCT
	}
}
