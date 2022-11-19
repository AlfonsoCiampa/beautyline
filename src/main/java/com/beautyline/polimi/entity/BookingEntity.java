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
@Table(name = "booking")
public class BookingEntity extends HibernateEntity {

	@Column(name = "consumerId", nullable = false)
	private Long consumerId;

	@Column(name = "treatmentId", nullable = false)
	private Long treatmentId;

	@Lob
	@Column(name = "note")
	private String note;

	@Column(name = "startTime", nullable = false)
	private LocalDateTime startTime;

	@Column(name = "endTime", nullable = false)
	private LocalDateTime endTime;

}