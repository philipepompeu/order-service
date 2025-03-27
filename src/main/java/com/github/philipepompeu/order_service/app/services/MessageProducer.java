package com.github.philipepompeu.order_service.app.services;

import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.retry.annotation.CircuitBreaker;
import org.springframework.retry.annotation.Recover;
import org.springframework.stereotype.Service;

@Service
public class MessageProducer {

    private final static String QUEUE_NAME="payment-service";
    
    private final StreamBridge streamBridge;

    public MessageProducer(StreamBridge streamBridge) {
        this.streamBridge = streamBridge;
    }

    @CircuitBreaker(maxAttempts = 3, openTimeout = 5000, resetTimeout = 10000)
    public void sendMessage(String message) {
        streamBridge.send(QUEUE_NAME, message);
    }

    @Recover
    public void recover(RuntimeException e) {
        System.out.println("TODO: implementar um REDIS, pra reenviar as mensagens depois de um tempo.");
    }
}
