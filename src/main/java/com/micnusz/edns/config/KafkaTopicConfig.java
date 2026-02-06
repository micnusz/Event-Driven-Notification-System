package com.micnusz.edns.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic eventsTopic() {
        return TopicBuilder.name("events")
                .partitions(3)
                .replicas(1)
                .build();
    }
}
