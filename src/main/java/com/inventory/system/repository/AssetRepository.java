package com.inventory.system.repository;

import com.inventory.system.entity.Asset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssetRepository extends JpaRepository<Asset, Long> {
    // Basic CRUD (save, delete, find) is already built into JpaRepository
}