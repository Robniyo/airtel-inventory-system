package com.inventory.system.repository;

import com.inventory.system.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    // Now searching by the registration number (username)
    Optional<User> findByUsername(String username);
}
