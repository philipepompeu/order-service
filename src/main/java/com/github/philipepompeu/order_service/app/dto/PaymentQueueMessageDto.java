package com.github.philipepompeu.order_service.app.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class PaymentQueueMessageDto {

    private String event;    
    private String content;
    private LocalDateTime createdAt;

    public PaymentQueueMessageDto(){
        createdAt = LocalDateTime.now();
    }
    
}
