package com.github.philipepompeu.order_service.domains.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.hibernate.annotations.SQLDelete;

import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name="sale_order")
public class SaleOrderEntity extends BaseEntity {

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalValue;

    @OneToMany(mappedBy = "saleOrder", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<SaleOrderItem> items;
    
    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private ClientEntity client;
    
    @Column(precision = 10, scale = 2)
    private BigDecimal freightCost;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "sales_order_status_history", joinColumns = @JoinColumn(name = "sales_order_id"))
    @OrderBy("changedAt ASC")
    private List<SalesOrderStatusHistory> statusHistory;


    public SaleOrderEntity(){

        this.items = new ArrayList<SaleOrderItem>();
        this.statusHistory = new ArrayList<>(){{
            add(new SalesOrderStatusHistory(SalesOrderStatus.OPEN));
        }};
    }


    public void setItems(List<SaleOrderItem> items){

        this.items = Optional.ofNullable(items).orElseGet(ArrayList::new);

        items.stream().forEach(it -> it.setSaleOrder(this));
    }

    @PrePersist
    @PreUpdate
    public void prePersist() {        
        this.totalValue = items.stream().map(item-> item.getPrice().multiply(item.getQuantity()) ).reduce(BigDecimal.ZERO, BigDecimal::add);               
        
        if (totalValue != null) {
            totalValue = totalValue.setScale(2, RoundingMode.HALF_UP);
        }
        if (freightCost != null) {
            freightCost = freightCost.setScale(2, RoundingMode.HALF_UP);
        }

        if (freightCost != null && totalValue != null) {
            totalValue = totalValue.add(freightCost); //Soma o custo do frete ao valor total do pedido
        }
    }    

    public void setNewStatus(SalesOrderStatus status) {
        if (!getCurrentStatus().equals(status)) {        
            this.statusHistory.add(new SalesOrderStatusHistory(status));
        }
    }

    public SalesOrderStatus getCurrentStatus() {
        return statusHistory.isEmpty()
            ? null
            : statusHistory.get(statusHistory.size() - 1).getStatus();
    }
    
}
