package com.beautyline.polimi.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@Table(name = "bookingConfiguration")
public class BookingConfigurationEntity extends HibernateEntity {

	@Column(name = "date", nullable = false)
	private String date;

	@Column(name = "startTime", nullable = false)
	private LocalTime startTime;

	@Column(name = "endTime", nullable = false)
	private LocalTime endTime;

}