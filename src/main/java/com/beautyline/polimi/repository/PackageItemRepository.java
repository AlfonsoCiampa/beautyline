package com.beautyline.polimi.repository;

import com.beautyline.polimi.entity.PackageItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PackageItemRepository extends JpaRepository<PackageItemEntity, Long> {
}
