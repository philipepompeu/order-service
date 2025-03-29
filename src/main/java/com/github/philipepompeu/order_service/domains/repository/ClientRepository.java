package com.github.philipepompeu.order_service.domains.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.github.philipepompeu.order_service.domains.model.ClientEntity;

public interface ClientRepository  extends JpaRepository<ClientEntity, UUID> {
    
}
