package com.beautyline.polimi.repository;

import com.beautyline.polimi.entity.EventEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<EventEntity, Long> {
}
