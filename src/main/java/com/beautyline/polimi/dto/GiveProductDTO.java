package com.beautyline.polimi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GiveProductDTO implements Serializable {

	private Long id;
	private Long orderItemId;
	private String consumerToGiveEmail;
}
