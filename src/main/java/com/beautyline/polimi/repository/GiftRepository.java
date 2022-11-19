package com.beautyline.polimi.repository;

import com.beautyline.polimi.entity.GiftEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GiftRepository extends JpaRepository<GiftEntity, Long> {
	Optional<GiftEntity> findByOrderItemId(Long orderItemId);

	Page<GiftEntity> findAllByConsumerId(Long consumerId, Pageable pageable);
}
