package com.tbo.examples.spring.data.rest.carrent.carrent_api_3;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CarDataProducer {
    private final static int PARTITION_COUNT = 1;
    private final static short REPLICATION_FACTOR = 1;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;
    private final KafkaAdmin kafkaAdmin;

    @PostConstruct
    public void configureTopic() {
        kafkaAdmin.createOrModifyTopics(new NewTopic(CarTopics.CAR_TOPIC, PARTITION_COUNT, REPLICATION_FACTOR));
    }

    public void send(CarDto carDto) {
        try {
            kafkaTemplate.send(CarTopics.CAR_TOPIC, "cars", objectMapper.writeValueAsString(carDto));
        } catch (Exception e) {
            log.error("Failed to send message to Kafka", e);
        }
    }
}
