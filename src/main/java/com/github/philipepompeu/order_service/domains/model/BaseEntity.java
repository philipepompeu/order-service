package com.github.philipepompeu.order_service.domains.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;


import org.hibernate.annotations.UpdateTimestamp;


import java.time.LocalDateTime;
import java.util.UUID;

@MappedSuperclass
@EntityListeners(SoftDeleteListener.class)
@FilterDef(name = "softDeleteFilter", defaultCondition = "revoked_at IS NULL")
@Filter(name = "softDeleteFilter")
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

    public void softDelete() {
        this.revokedAt = LocalDateTime.now();
    }

    public boolean isDeleted() {
        return this.revokedAt != null;
    }
}

