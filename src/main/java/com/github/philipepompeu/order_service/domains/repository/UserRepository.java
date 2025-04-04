package com.github.philipepompeu.order_service.domains.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.github.philipepompeu.order_service.domains.model.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, UUID> {
    
    Optional<UserEntity> findByUserName(String username);
}
