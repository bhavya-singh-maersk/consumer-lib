package com.maersk.consumerlib.kafka;

import com.maersk.consumerlib.services.ConsumerInitiator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ConsumerFactory {

    @Autowired
    private KafkaConsumerInitiator kafkaConsumerInitiator;

    public ConsumerInitiator get(String consumerType){
        switch(consumerType){
            case "kafka": return kafkaConsumerInitiator;
            default: return null;
        }
    }

}
