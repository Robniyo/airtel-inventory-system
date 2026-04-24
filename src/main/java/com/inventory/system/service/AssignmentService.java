package com.inventory.system.service;

import com.inventory.system.entity.Assignment;
import java.util.List;

public interface AssignmentService {

    void assignAsset(Long assetId, Long userId);

    void returnAsset(Long assignmentId);

    List<Assignment> getAllAssignments();
}