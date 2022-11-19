package com.beautyline.polimi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TreatmentDTO implements Serializable {

	private Long id;
	private String name;
	private String description;
	private BigDecimal price;
	private Boolean obscure;
	private Long duration;

}