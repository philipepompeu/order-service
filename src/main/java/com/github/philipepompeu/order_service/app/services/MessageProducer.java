package com.github.philipepompeu.order_service.app.services;

import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.retry.annotation.CircuitBreaker;
import org.springframework.retry.annotation.Recover;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.philipepompeu.order_service.app.dto.PaymentQueueMessageDto;

@Service
public class MessageProducer {

    private static final Logger log = LoggerFactory.getLogger(MessageProducer.class);    

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
    
            if(!streamBridge.send("payment-service", message)){
                log.info("Fail to send message");                
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize message", e);
        }

    }

    @Recover
    public void recover(RuntimeException e, PaymentQueueMessageDto message) {
         try {
            String serialized = objectMapper.writeValueAsString(message);
            redisTemplate.opsForList().rightPush(redisKey, serialized);
            log.info("Mensagem salva no Redis para reprocessamento.");

        } catch (JsonProcessingException ex) {
            ex.printStackTrace();
        }
    }

    @Scheduled(fixedDelay = 90000) // a cada 90 segundos
    public void retryFailedMessages() {
        Long size = redisTemplate.opsForList().size(redisKey);
        if (size == null || size == 0) return;

        for (int i = 0; i < size; i++) {
            String messageJson = redisTemplate.opsForList().leftPop(redisKey);
            if (messageJson == null) continue;

            try {
                PaymentQueueMessageDto message = objectMapper.readValue(messageJson, PaymentQueueMessageDto.class);
                sendPaymentMessage(message); // reprocessar com circuit breaker
                log.info("Mensagem reenviada com sucesso.");
            } catch (Exception e) {
                // falhou de novo, adiciona de volta
                redisTemplate.opsForList().rightPush(redisKey, messageJson);
                log.error("Falha ao reprocessar, mensagem reempilhada.");
            }
        }
    }
}
