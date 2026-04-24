package com.inventory.system.repository;

import com.inventory.system.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    // Using Optional is best practice for finding unique records
    Optional<User> findByEmail(String email);
}