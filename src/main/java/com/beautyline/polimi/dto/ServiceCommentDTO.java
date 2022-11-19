package com.beautyline.polimi.dto;

import com.beautyline.polimi.entity.ServiceCommentEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ServiceCommentDTO implements Serializable {

	private Long id;
	private Long consumerId;
	private Long serviceId;
	private ServiceCommentEntity.Type serviceType;
	private String description;
	private LocalDateTime date;
}
