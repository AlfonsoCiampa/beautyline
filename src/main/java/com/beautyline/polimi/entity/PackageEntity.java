package com.beautyline.polimi.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@Table(name = "package")
public class PackageEntity extends OfferSuperclass {

	@Column(name = "price", nullable = false, precision = 7, scale = 2)
	private BigDecimal price;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "packageId", referencedColumnName = "id", nullable = false, updatable = false, insertable = false)
	private List<PackageItemEntity> packageItems;

	@Column(name = "code", nullable = false)
	private String code;

}