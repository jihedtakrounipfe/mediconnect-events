package com.mediconnect.events;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@EnableKafka
@SpringBootApplication(scanBasePackages = "com.mediconnect.events")
public class MediconnectEventsApplication {

    public static void main(String[] args) {
        SpringApplication.run(MediconnectEventsApplication.class, args);
    }

}
