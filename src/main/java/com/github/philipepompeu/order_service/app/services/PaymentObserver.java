package com.github.philipepompeu.order_service.app.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.philipepompeu.order_service.app.dto.PaymentMessageFactory;
import com.github.philipepompeu.order_service.app.dto.PaymentQueueMessageDto;

import com.github.philipepompeu.order_service.domains.model.SaleOrderEntity;

@Service
public class PaymentObserver implements SalesOrderObserver {

    @Autowired
    private MessageProducer messageProducer;

    @Override
    public void onSalesOrderUpdated(SaleOrderEntity salesOrder) {
        
        sendMessage(salesOrder, "onSalesOrderUpdated");
    }

    @Override
    public void onSalesOrderCreated(SaleOrderEntity salesOrder) {
        
        sendMessage(salesOrder, "onSalesOrderCreated");
    }

    @Override
    public void onSalesOrderDeleted(SaleOrderEntity salesOrder) {
        
        sendMessage(salesOrder, "onSalesOrderDeleted");
    }


    public void sendMessage(SaleOrderEntity salesOrderEntity, String event){

        System.out.println("Sending message to Payment-Service");
        
        String content = PaymentMessageFactory.getFactory().getPaymentMessage(salesOrderEntity);        
        
        if (content != null) {
            
            PaymentQueueMessageDto message = new PaymentQueueMessageDto();
            message.setEvent(event);
            message.setContent(content);
            this.messageProducer.sendPaymentMessage(message);
            
        }       
    }

}
