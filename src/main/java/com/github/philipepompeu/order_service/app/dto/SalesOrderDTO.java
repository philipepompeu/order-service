package com.github.philipepompeu.order_service.app.dto;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import com.github.philipepompeu.order_service.domains.model.PaymentMethod;
import com.github.philipepompeu.order_service.domains.model.SaleOrderEntity;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class SalesOrderDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String id;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private BigDecimal totalValue;
    
    @NotEmpty(message="Must have at least one item in the order.")
    @Valid
    private List<SaleOrderItemDto> products;

    @NotBlank    
    private String clientId;

    @PositiveOrZero
    private BigDecimal freightCost;
    
    
    private PaymentMethod paymentMethod;

    public SalesOrderDTO(SaleOrderEntity entity){

        this.id = entity.getId().toString();
        if (entity.getClient() != null && entity.getClient().getId() != null) {
            this.clientId = entity.getClient().getId().toString();            
        }

        this.paymentMethod = entity.getPaymentMethod();

        this.freightCost = entity.getFreightCost().setScale(2, RoundingMode.HALF_UP);
        this.totalValue = entity.getTotalValue().setScale(2, RoundingMode.HALF_UP);
        
        this.products = entity.getItems().stream().map(it-> new SaleOrderItemDto(it) ).toList();

    }

}
