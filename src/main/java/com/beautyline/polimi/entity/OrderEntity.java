package com.beautyline.polimi.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@Table(name = "`order`")
public class OrderEntity extends HibernateEntity {

	@Column(name = "consumerId", nullable = false)
	private Long consumerId;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "orderId", referencedColumnName = "id", nullable = false, updatable = false, insertable = false)
	private List<OrderItemEntity> orderItems;

	@Column(name = "price", nullable = false, precision = 7, scale = 2)
	private BigDecimal price;

	@Column(name = "date", nullable = false)
	private LocalDateTime date;

	@Lob
	@Column(name = "note")
	private String note;

	@Column(name = "packageCode")
	private String packageCode;

}