package com.learnkafka.health;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.ConsumerGroupDescription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Component
@Slf4j
public class KafkaConsumerHealthIndicator implements HealthIndicator {

    private final KafkaListenerEndpointRegistry registry;

    private final KafkaAdmin kafkaAdmin;

    public KafkaConsumerHealthIndicator(KafkaListenerEndpointRegistry registry, KafkaAdmin kafkaAdmin) {
        this.registry = registry;
        this.kafkaAdmin = kafkaAdmin;
    }


    @Override
    public Health health() {
        registry.
                getAllListenerContainers()
                .forEach(messageListenerContainer -> {
                    log.info("messageListenerContainer : " + messageListenerContainer);
                    log.info("Is In expected state : " + messageListenerContainer.isInExpectedState());

                    try {
                        AdminClient client = AdminClient.create(kafkaAdmin.getConfigurationProperties());
                        var consumerGroup = messageListenerContainer.getGroupId();
                        Map<String, ConsumerGroupDescription> map;
                        map = client.describeConsumerGroups(Collections.singletonList(consumerGroup)).all().get();
                        log.info("map : " + map);
                    } catch (Exception e) {
                        log.error("Exception is Kafka health : {} ", e.getMessage(), e);
                    }


                });
        return Health.up().withDetail("Custom_Health ", "UP").build();
    }
}
