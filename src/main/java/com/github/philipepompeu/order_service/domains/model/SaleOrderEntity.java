package com.github.philipepompeu.order_service.domains.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import org.hibernate.annotations.SQLDelete;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name="sale_order")
@SQLDelete(sql = "UPDATE sale_order SET revoked_at = CURRENT_TIMESTAMP WHERE id = ?")
public class SaleOrderEntity extends BaseEntity {

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalValue;

    @OneToMany(mappedBy = "saleOrder", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<SaleOrderItem> products;
    
    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private ClientEntity client;
    
    @Column(precision = 10, scale = 2)
    private BigDecimal freightCost;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @PrePersist
    @PreUpdate
    public void prePersist() {
        
        if (totalValue != null) {
            totalValue = totalValue.setScale(2, RoundingMode.HALF_UP);
        }
        if (freightCost != null) {
            freightCost = freightCost.setScale(2, RoundingMode.HALF_UP);
        }
    }
    
    
}
