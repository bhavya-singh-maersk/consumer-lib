package com.maersk.consumerlib.kafka;

import com.maersk.consumerlib.services.ConsumerInitiator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class KafkaConsumerRunner implements CommandLineRunner {

    @Autowired
    private ConsumerInitiator consumerInitiator;

    @Override
    public void run(String... args) {
        consumerInitiator.startConsumer();
    }
}
