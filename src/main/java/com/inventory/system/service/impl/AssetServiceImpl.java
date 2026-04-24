package com.inventory.system.service.impl;

import com.inventory.system.entity.Asset;
import com.inventory.system.repository.AssetRepository;
import com.inventory.system.service.AssetService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AssetServiceImpl implements AssetService {

    private final AssetRepository repository;

    public AssetServiceImpl(AssetRepository repository) {
        this.repository = repository;
    }

    @Override
    public Asset save(Asset asset) {
        return repository.save(asset);
    }

    @Override
    public List<Asset> getAll() {
        return repository.findAll();
    }
}