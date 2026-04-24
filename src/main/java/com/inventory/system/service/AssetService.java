package com.inventory.system.service;

import com.inventory.system.entity.Asset;

import java.util.List;

public interface AssetService {
    Asset save(Asset asset);
    List<Asset> getAll();
}