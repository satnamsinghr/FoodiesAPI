package com.satnam.codesapi.user_service.kafka;

import com.satnam.codesapi.user_service.event.OrderEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class OrderProducer {

    private final KafkaTemplate<String, OrderEvent> kafkaTemplate;

    public OrderProducer(KafkaTemplate<String, OrderEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(OrderEvent event) {
        kafkaTemplate.send("order_topic", event.getOrderId(), event);
        System.out.println("Order Event sent: " + event);
    }
}
