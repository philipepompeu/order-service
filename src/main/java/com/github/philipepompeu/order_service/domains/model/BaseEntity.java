package com.github.philipepompeu.order_service.domains.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import org.hibernate.annotations.SoftDelete;
import org.hibernate.annotations.SoftDeleteType;
import org.hibernate.annotations.UpdateTimestamp;


import java.time.LocalDateTime;
import java.util.UUID;

@MappedSuperclass
@SoftDelete(strategy =  SoftDeleteType.DELETED)
@Getter
@Setter
public abstract class BaseEntity  {

    @Id
    @Column(updatable = false, nullable = false)    
    @GeneratedValue(generator = "UUID")    
    private UUID id;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "revoked_at")
    private LocalDateTime revokedAt;

    @PreRemove
    public void onSoftDelete() {
        this.revokedAt = LocalDateTime.now();
    }

    
}

