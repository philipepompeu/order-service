package com.github.philipepompeu.order_service.app.dto;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.philipepompeu.order_service.domains.model.SaleOrderItem;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SaleOrderItemDto {
    
    
    @JsonProperty(required = true)
    private String productId;

    @JsonIgnore()
    private String saleOrderId;

    private BigDecimal price;
    private BigDecimal quantity;

    public SaleOrderItemDto(SaleOrderItem entity){

        
        this.saleOrderId = entity.getSaleOrder().getId().toString();
        this.productId = entity.getProduct().getId().toString();
        this.price = entity.getPrice();
        this.quantity = entity.getQuantity();
    }


}
