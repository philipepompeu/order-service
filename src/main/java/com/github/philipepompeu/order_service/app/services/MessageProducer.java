package com.github.philipepompeu.order_service.app.services;

import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.retry.annotation.CircuitBreaker;
import org.springframework.retry.annotation.Recover;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.philipepompeu.order_service.app.dto.PaymentQueueMessageDto;

@Service
public class MessageProducer {

    private ObjectMapper objectMapper = new ObjectMapper();   
    
    private final StreamBridge streamBridge;

    private final StringRedisTemplate redisTemplate;
    private String redisKey = "failed-payment-messages";

    public MessageProducer(StreamBridge streamBridge, StringRedisTemplate redisTemplate)  {
        this.streamBridge = streamBridge;
        this.redisTemplate = redisTemplate;

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
            throw new RuntimeException("Failed to serialize message", e);
        }

    }

    @Recover
    public void recover(RuntimeException e, PaymentQueueMessageDto message) {
         try {
            String serialized = objectMapper.writeValueAsString(message);
            redisTemplate.opsForList().rightPush(redisKey, serialized);
            System.out.println("Mensagem salva no Redis para reprocessamento.");
        } catch (JsonProcessingException ex) {
            ex.printStackTrace();
        }
    }

    @Scheduled(fixedDelay = 30000) // a cada 30 segundos
    public void retryFailedMessages() {
        Long size = redisTemplate.opsForList().size(redisKey);
        if (size == null || size == 0) return;

        for (int i = 0; i < size; i++) {
            String messageJson = redisTemplate.opsForList().leftPop(redisKey);
            if (messageJson == null) continue;

            try {
                PaymentQueueMessageDto message = objectMapper.readValue(messageJson, PaymentQueueMessageDto.class);
                sendPaymentMessage(message); // reprocessar com circuit breaker
                System.out.println("Mensagem reenviada com sucesso.");
            } catch (Exception e) {
                // falhou de novo, adiciona de volta
                redisTemplate.opsForList().rightPush(redisKey, messageJson);
                System.out.println("Falha ao reprocessar, mensagem reempilhada.");
            }
        }
    }
}
