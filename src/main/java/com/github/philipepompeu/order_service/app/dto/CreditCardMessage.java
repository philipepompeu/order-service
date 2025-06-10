package com.github.philipepompeu.order_service.app.dto;

import com.github.philipepompeu.order_service.domains.model.SaleOrderEntity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreditCardMessage extends BasicPaymentMessage {
    
    String cardNumber;
    String cardHolder;
    String cardIssuer;
    
    @Override
    public void create(SaleOrderEntity salesOrderEntity) {        
        setValue(salesOrderEntity.getTotalValue());
        setCardIssuer("mastercard");
        setCardHolder(salesOrderEntity.getClient().getFullName());
        setPayerId(salesOrderEntity.getClient().getLegalId());
        setPayerPhone(salesOrderEntity.getClient().getPhoneNumber());
        setPayerEmail(salesOrderEntity.getClient().getEmail());
    }
}
