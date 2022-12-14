package com.beautyline.polimi.repository;

import com.beautyline.polimi.entity.PackageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PackageRepository extends JpaRepository<PackageEntity, Long> {

	Optional<PackageEntity> findByCode(String code);
}
