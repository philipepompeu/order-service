package com.github.philipepompeu.order_service.domains.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.github.philipepompeu.order_service.domains.model.SaleOrderEntity;

public interface SaleOrderRepository extends JpaRepository<SaleOrderEntity, UUID> {

    @Query("SELECT p FROM SaleOrderEntity p JOIN FETCH p.items")
    Optional<SaleOrderEntity> findByIdWithProducts(UUID id);

    @EntityGraph(attributePaths = {"items"})
    List<SaleOrderEntity> findAll();
    
}
