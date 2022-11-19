package com.beautyline.polimi.repository;

import com.beautyline.polimi.entity.ServiceCommentEntity;
import com.beautyline.polimi.entity.ServiceCommentEntity.Type;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceCommentRepository extends JpaRepository<ServiceCommentEntity, Long> {
	Page<ServiceCommentEntity> findAllByServiceTypeAndServiceId(Type serviceType, Long serviceId, Pageable pageable);

	Page<ServiceCommentEntity> findAllByConsumerId(Long consumerId, Pageable pageable);
}
