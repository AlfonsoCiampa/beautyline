package com.beautyline.polimi.repository;

import com.beautyline.polimi.entity.BookingEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<BookingEntity, Long> {

	List<BookingEntity> findByConsumerId(Long consumerId);

	Page<BookingEntity> findAllByConsumerId(Long consumerId, Pageable pageable);

	@Query("""
			SELECT b
			FROM BookingEntity b
			WHERE (:startTime <= b.startTime AND b.startTime < :endTime)
				OR (b.startTime < :startTime AND :startTime < b.endTime)
		""")
	List<BookingEntity> findByPeriod(LocalDateTime startTime, LocalDateTime endTime);

	@Query("""
			SELECT COUNT(b.id)
			FROM BookingEntity b
			WHERE b.consumerId = :consumerId
				AND b.endTime > :startTime
		""")
	Long countByActiveAndConsumerId(LocalDateTime startTime, Long consumerId);
}
