package com.beautyline.polimi.repository;

import com.beautyline.polimi.entity.OrderEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {

	Page<OrderEntity> findAllByConsumerId(Long consumerId, Pageable pageable);

}
