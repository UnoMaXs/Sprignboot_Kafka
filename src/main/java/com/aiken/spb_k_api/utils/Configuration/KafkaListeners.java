package com.aiken.spb_k_api;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaListeners {

    @KafkaListener(topics = "inventoryTopic", groupId = "GroupId-1")
    void listener(String data){
        System.out.println("Listener received: " + data);
    }

}
