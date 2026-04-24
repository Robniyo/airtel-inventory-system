package com.inventory.system.repository;

import com.inventory.system.entity.AssetRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssetRequestRepository extends JpaRepository<AssetRequest, Long> {
}