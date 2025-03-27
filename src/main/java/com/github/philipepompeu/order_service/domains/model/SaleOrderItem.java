package com.github.philipepompeu.order_service.domains.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;

import org.hibernate.annotations.SQLDelete;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
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
@Table(name="sale_order_item")
@SQLDelete(sql = "UPDATE sale_order_item SET revoked_at = CURRENT_TIMESTAMP WHERE id = ?")
public class SaleOrderItem extends BaseEntity implements Serializable {

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private ProductEntity product;

    @ManyToOne
    @JoinColumn(name = "sales_order_id", nullable = false)
    private SaleOrderEntity saleOrder;

    @Column(precision = 10, scale = 2)
    private BigDecimal price;
    
    @Column(precision = 10, scale = 2)
    private BigDecimal quantity;    
    
    @PrePersist
    @PreUpdate
    public void prePersist() {        
        if (price != null) {
            price = price.setScale(2, RoundingMode.HALF_UP);
        }      
        
        if (quantity != null) {
            quantity = quantity.setScale(2, RoundingMode.HALF_UP);
        }      
    }
    
}
