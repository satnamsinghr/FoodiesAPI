package com.satnam.codesapi.user_service.kafka;

import com.satnam.codesapi.user_service.event.OrderEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class OrderConsumer {

    @KafkaListener(topics = "order_topic", groupId = "order_group")
    public void consume(OrderEvent event) {
        System.out.println("Order Event received: " + event);
    }
}
