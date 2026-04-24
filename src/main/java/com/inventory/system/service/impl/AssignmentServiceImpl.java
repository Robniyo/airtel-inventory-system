package com.inventory.system.service.impl;

import com.inventory.system.entity.Asset;
import com.inventory.system.entity.Assignment;
import com.inventory.system.entity.User;
import com.inventory.system.repository.AssetRepository;
import com.inventory.system.repository.AssignmentRepository;
import com.inventory.system.repository.UserRepository;
import com.inventory.system.service.AssignmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class AssignmentServiceImpl implements AssignmentService {

    @Autowired
    private AssignmentRepository assignmentRepository;

    @Autowired
    private AssetRepository assetRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional // Ensures both asset status and assignment are saved together
    public void assignAsset(Long assetId, Long userId) {

        // 1. Find the Asset
        Asset asset = assetRepository.findById(assetId)
                .orElseThrow(() -> new RuntimeException("Error: Asset ID " + assetId + " not found."));

        // 2. Check Availability (Compare using Enums)
        if (asset.getStatus() != Asset.Status.AVAILABLE) {
            throw new RuntimeException("Error: Asset is currently " + asset.getStatus());
        }

        // 3. Find the User
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Error: User ID " + userId + " not found."));

        // 4. Create New Assignment
        Assignment assignment = new Assignment();
        assignment.setAsset(asset);
        assignment.setUser(user);
        assignment.setIssueDate(LocalDate.now()); // Requires java.time.LocalDate
        assignment.setStatus(Assignment.Status.ISSUED);

        // 5. Update Asset Status to ASSIGNED
        asset.setStatus(Asset.Status.ASSIGNED);

        // 6. Save both to Database
        assetRepository.save(asset);
        assignmentRepository.save(assignment);
    }

    @Override
    @Transactional
    public void returnAsset(Long assignmentId) {

        // 1. Find the existing assignment
        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new RuntimeException("Error: Assignment record not found."));

        // 2. Update Assignment status
        assignment.setReturnDate(LocalDate.now());
        assignment.setStatus(Assignment.Status.RETURNED);

        // 3. Set the linked Asset back to AVAILABLE
        Asset asset = assignment.getAsset();
        if (asset != null) {
            asset.setStatus(Asset.Status.AVAILABLE);
            assetRepository.save(asset);
        }

        assignmentRepository.save(assignment);
    }

    @Override
    public List<Assignment> getAllAssignments() {
        return assignmentRepository.findAll();
    }
}