package com.beautyline.polimi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PlaceOrderDTO implements Serializable {

	private List<Long> items;
	private BigDecimal price;
	private String note;
	private String packageCode;
	private String paymentId;
	private Boolean isIntentId;
}

