package com.beautyline.polimi.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@Table(name = "orderItem")
public class OrderItemEntity extends HibernateEntity {

	@Column(name = "orderId", nullable = false)
	private Long orderId;

	@Column(name = "productId", nullable = false)
	private Long productId;

	@Column(name = "price", nullable = false, precision = 7, scale = 2)
	private BigDecimal price;

	@Column(name = "retrieved", nullable = false)
	private Boolean retrieved;

	@JsonManagedReference
	@OneToOne(cascade = CascadeType.ALL, mappedBy = "orderItem")
	private GiftEntity gift;
}