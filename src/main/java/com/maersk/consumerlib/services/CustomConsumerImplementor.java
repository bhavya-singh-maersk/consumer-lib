package com.maersk.consumerlib.services;

import com.maersk.consumerlib.config.KafkaConsumerConfig;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component("eacloud")
public class CustomConsumerImplementor<T> implements CustomImplementor<T> {

    @Override
    public Worker<T> getConsumerWorker(String event) {
        //Consuming systems to return class containing message processing logic
        return null;
    }

    @Override
    public Map<String, Object> getConsumerConfigs(String consumerGroup, String event) {
        // Consuming systems to return their consumer config map
        return KafkaConsumerConfig.getConsumerConfigs("localhost:9092", true, "100", "60000", "earliest", 50,  consumerGroup, event);
    }
}
