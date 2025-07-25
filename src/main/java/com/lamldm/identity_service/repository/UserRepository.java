package com.lamldm.identity_service.repository;

import com.lamldm.identity_service.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    boolean existsByUsername(String username); // Kiểm tra sự tồn tại của username
    Optional<User> findByUsername(String username);
}
