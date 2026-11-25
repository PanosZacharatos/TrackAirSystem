package com.scytalys.assignment.track_picture_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;

@Configuration
public class KafkaConfig {

    @Bean
    public DefaultErrorHandler kafkaErrorHandler(){
        return new DefaultErrorHandler(new FixedBackOff(3000L, FixedBackOff.UNLIMITED_ATTEMPTS));
    }
}
