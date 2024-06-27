package com.tbo.examples.spring.data.rest.carrent.carrent_api_3;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class CarDataConsumer {

    private final CountDownLatch latch = new CountDownLatch(1);
    @Getter
    private String payload = null;

    @KafkaListener(topics = CarTopics.CAR_TOPIC, groupId = "group_id")
    public void receive(String payload) {
        log.info("received payload='{}'", payload);
        this.payload = payload;
        latch.countDown();
    }

    public boolean awaitLatch(int timeout) throws InterruptedException {
        return latch.await(timeout, TimeUnit.SECONDS);
    }
}
