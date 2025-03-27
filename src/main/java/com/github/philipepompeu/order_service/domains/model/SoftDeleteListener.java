package com.github.philipepompeu.order_service.domains.model;

import jakarta.persistence.PreRemove;

public class SoftDeleteListener {
    
    @PreRemove
    public void preRemove(BaseEntity entity) {
        entity.softDelete();
    }
}
