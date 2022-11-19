package com.beautyline.polimi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO implements Serializable {

	private Long id;
	private Long consumerId;
	private List<OrderItemDTO> orderItems;
	private BigDecimal price;
	private LocalDateTime date;
	private String note;
	private String packageCode;
}
