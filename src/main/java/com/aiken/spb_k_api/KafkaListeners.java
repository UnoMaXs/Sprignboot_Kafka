package com.aiken.spb_k_api;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaListeners {

    @KafkaListener(topics = "PruebaInventario", groupId = "groupId")
    void listener(String data){
        System.out.println("Listener received: " + data + "👌");
    }

}
