package com.beautyline.polimi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PackageItemDTO {

	private Long id;
	private Long packageId;
	private Long productId;
}
