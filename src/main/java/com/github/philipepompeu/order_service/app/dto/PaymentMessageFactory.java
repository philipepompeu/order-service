package com.github.philipepompeu.order_service.app.dto;

import java.util.HashMap;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.philipepompeu.order_service.domains.model.PaymentMethod;
import com.github.philipepompeu.order_service.domains.model.SaleOrderEntity;

public class PaymentMessageFactory {
    
    private static PaymentMessageFactory instance;

    private HashMap<PaymentMethod, Class<? extends BasicPaymentMessage>> classByPaymentMethod = new HashMap<>();
    private ObjectMapper objectMapper = new ObjectMapper();

    private PaymentMessageFactory(){

        classByPaymentMethod.put(PaymentMethod.CREDIT_CARD, CreditCardMessage.class);

        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    public static PaymentMessageFactory getFactory(){
        if (instance == null) {
            synchronized (PaymentMessageFactory.class) {
                if (instance == null) {
                    instance = new PaymentMessageFactory();
                }
            }            
        }
        return instance;
    }


    public String getPaymentMessage(SaleOrderEntity salesOrderEntity){

        if (classByPaymentMethod.containsKey(salesOrderEntity.getPaymentMethod())) {
            
            try {
                BasicPaymentMessage message = classByPaymentMethod.get(salesOrderEntity.getPaymentMethod()).getDeclaredConstructor().newInstance();

                message.create(salesOrderEntity);
                message.setOrderId(salesOrderEntity.getId().toString());

                return objectMapper.writeValueAsString(message);           
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }


        return null;

    }


}
