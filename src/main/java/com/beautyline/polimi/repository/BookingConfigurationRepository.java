package com.beautyline.polimi.repository;

import com.beautyline.polimi.entity.BookingConfigurationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

public interface BookingConfigurationRepository extends JpaRepository<BookingConfigurationEntity, Long> {

	@Query("SELECT bc FROM BookingConfigurationEntity bc WHERE bc.date = :#{#dayOfWeek.name()} ORDER BY bc.startTime")
	List<BookingConfigurationEntity> findAllByDayOfWeekOrderByStartTime(DayOfWeek dayOfWeek);

	@Query("SELECT bc FROM BookingConfigurationEntity bc WHERE bc.date = :date ORDER BY bc.startTime")
	List<BookingConfigurationEntity> findAllByDateOrderByStartTime(String date);

	@Query("""
			SELECT bc FROM BookingConfigurationEntity bc WHERE bc.date = :#{#dayOfWeek.name()}
			AND (
				(:startTime <= bc.startTime and bc.endTime < :endTime)
				OR
				(bc.startTime < :startTime and (bc.startTime is null or :startTime < bc.endTime))
			)
		""")
	List<BookingConfigurationEntity> findAllByDayOfWeekAndActiveOn(DayOfWeek dayOfWeek,
		@DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime startTime, @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime endTime);

	@Query("""
			SELECT bc FROM BookingConfigurationEntity bc WHERE bc.date = :date
			AND (
				(:startTime <= bc.startTime and bc.endTime < :endTime)
				OR
				(bc.startTime < :startTime and (bc.startTime is null or :startTime < bc.endTime))
			)
		""")
	List<BookingConfigurationEntity> findAllByDateAndActiveOn(String date,
		@DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime startTime, @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime endTime);
}
