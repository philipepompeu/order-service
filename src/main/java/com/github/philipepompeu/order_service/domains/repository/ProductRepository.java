package com.github.philipepompeu.order_service.domains.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.github.philipepompeu.order_service.domains.model.ProductEntity;

public interface ProductRepository extends JpaRepository<ProductEntity, UUID> {
    
    Optional<List<ProductEntity>> findByIdIn(List<UUID> ids);
}
