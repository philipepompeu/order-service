package com.github.philipepompeu.order_service.domains.model;

import java.time.LocalDateTime;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor
public class SalesOrderStatusHistory {

    @Enumerated(EnumType.STRING)
    private SalesOrderStatus status;

    private LocalDateTime changedAt;

    public SalesOrderStatusHistory(SalesOrderStatus status) {
        this.status = status;
        this.changedAt = LocalDateTime.now();

    }

    
    
}
