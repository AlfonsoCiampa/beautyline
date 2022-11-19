package com.beautyline.polimi.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@Table(name = "gift")
public class GiftEntity extends HibernateEntity {

	@Column(name = "orderItemId", nullable = false)
	private Long orderItemId;

	@Column(name = "consumerId", nullable = false)
	private Long consumerId;

	@JsonBackReference
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "orderItemId", referencedColumnName = "id", nullable = false, updatable = false, insertable = false)
	private OrderItemEntity orderItem;
}