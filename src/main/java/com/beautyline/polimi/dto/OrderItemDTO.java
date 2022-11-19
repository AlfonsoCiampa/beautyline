package com.beautyline.polimi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDTO {

	private Long id;
	private Long orderId;
	private Long productId;
	private BigDecimal price;
	private Boolean retrieved;
	private Long giftedConsumerId;
}
