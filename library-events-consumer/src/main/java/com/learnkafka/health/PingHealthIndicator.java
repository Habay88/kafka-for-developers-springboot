package com.learnkafka.health;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;

import java.util.Comparator;
import java.util.TreeMap;
import java.util.function.Function;

public class PingHealthIndicator extends AbstractHealthIndicator {


    @Override
    protected void doHealthCheck(Health.Builder builder) throws Exception {

    }
}