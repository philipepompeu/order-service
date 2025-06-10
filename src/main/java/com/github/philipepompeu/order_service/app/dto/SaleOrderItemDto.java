package com.github.philipepompeu.order_service.app.dto;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.philipepompeu.order_service.domains.model.SaleOrderItem;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SaleOrderItemDto {
    
    
    @JsonProperty(required = true)
    @NotBlank(message="Product ID is required.")
    private String productId;

    @JsonIgnore()
    private String saleOrderId;

    private String id;

    @Positive(message="Price must be positive.")
    private BigDecimal price;
    
    @Positive(message="Quantity must be positive.")    
    private BigDecimal quantity;

    public SaleOrderItemDto(SaleOrderItem entity){
        
        this.saleOrderId = entity.getSaleOrder().getId().toString();
        this.productId = entity.getProduct().getId().toString();
        this.price = entity.getPrice();
        this.quantity = entity.getQuantity();
        this.id = entity.getId().toString();
    }


}
