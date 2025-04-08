package com.github.philipepompeu.order_service.app.services;

import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.retry.annotation.CircuitBreaker;
import org.springframework.retry.annotation.Recover;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.philipepompeu.order_service.app.dto.PaymentQueueMessageDto;

@Service
public class MessageProducer {

    private ObjectMapper objectMapper = new ObjectMapper();    
    
    private final StreamBridge streamBridge;

    public MessageProducer(StreamBridge streamBridge) {
        this.streamBridge = streamBridge;

        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    private void sendMessage(String queueName,String message) {
        streamBridge.send(queueName, message);
    }
    
    @CircuitBreaker(maxAttempts = 3, openTimeout = 5000, resetTimeout = 10000)
    public void sendPaymentMessage(PaymentQueueMessageDto message){

        try {            
            String finalMessage = objectMapper.writeValueAsString(message);            
    
            this.sendMessage("payment-service", finalMessage);
        } catch (Exception e) {
            e.printStackTrace();//TODO: tratar o recover
        }

    }

    @Recover
    public void recover(RuntimeException e) {
        System.out.println("TODO: implementar um REDIS, pra reenviar as mensagens depois de um tempo.");
    }
}
