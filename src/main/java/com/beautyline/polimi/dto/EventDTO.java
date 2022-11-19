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
public class EventDTO implements Serializable {

	private Long id;
	private String name;
	private String description;
	private LocalDateTime startDate;
	private LocalDateTime endDate;
}
