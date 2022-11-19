package com.beautyline.polimi.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@Table(name = "packageItem")
public class PackageItemEntity extends HibernateEntity {

	@Column(name = "packageId", nullable = false)
	private Long packageId;

	@Column(name = "productId", nullable = false)
	private Long productId;
}