package com.maersk.consumerlib.kafka;

import com.maersk.consumerlib.enums.QueueType;
import com.maersk.consumerlib.services.ConsumerInitiator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Slf4j
@Component
public class KafkaConsumerRunner implements CommandLineRunner {

    @Autowired
    private ConsumerFactory consumerFactory;

    @Autowired
    private KafkaConfigResolver configResolver;

    @Autowired
    private ConsumerInitiator consumerInitiator;

    @Override
    public void run(String... args) {
        //String event = configResolver.getEventName();
       // QueueType queueType = args.length>1?QueueType.valueOf(args[1].toUpperCase()):QueueType.NORMAL.value();
       // queueType = queueType!=null ? queueType:QueueType.normal;
        //consumerInitiator.startConsumer();
    }
}
