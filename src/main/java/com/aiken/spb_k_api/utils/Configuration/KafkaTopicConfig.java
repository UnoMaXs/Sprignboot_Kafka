package com.aiken.spb_k_api.Configuration;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    public NewTopic inventoryTopic(){
        return TopicBuilder.name("inventoryTopic")
                .build();
    }
}
