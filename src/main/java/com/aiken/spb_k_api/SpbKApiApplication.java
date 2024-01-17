package com.aiken.spb_k_api;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaTemplate;

@SpringBootApplication
public class SpbKApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpbKApiApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(KafkaTemplate<String, String> kafkaTemplate){
        return args -> {
            for (int i = 0; i < 10; i++) {
                kafkaTemplate.send(/*TOPIC*/"amigoscode",/*MENSAJE*/"Hello kafka c:" + i);
            }
        };
    }
}
