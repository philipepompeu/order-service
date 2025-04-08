package com.github.philipepompeu.order_service.app.dto;

import java.math.BigDecimal;

import com.github.philipepompeu.order_service.domains.model.SaleOrderEntity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public abstract class BasicPaymentMessage {
    
    private String orderId;
    private BigDecimal value;
    private String payerId;
    private String payerEmail;
    private String payerPhone;

    public abstract void create(SaleOrderEntity entity);
    

}
