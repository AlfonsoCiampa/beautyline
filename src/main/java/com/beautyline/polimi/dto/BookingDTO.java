package com.beautyline.polimi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookingDTO implements Serializable {

	private Long id;
	private Long consumerId;
	private Long treatmentId;
	private String note;
	private LocalDateTime startTime;
	private LocalDateTime endTime;
}
