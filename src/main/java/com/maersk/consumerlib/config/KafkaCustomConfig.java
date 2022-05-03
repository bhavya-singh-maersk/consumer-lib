package com.maersk.consumerlib.config;

import com.maersk.consumerlib.kafka.KafkaConfigResolver;
import com.maersk.consumerlib.kafka.KafkaConsumer;
import com.maersk.consumerlib.services.CustomImplementor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class KafkaCustomConfig {

    private CustomImplementor customImplementor;

    @Autowired
    private ApplicationContext context;

    @Autowired
    private KafkaConfigResolver configResolver;

    @Autowired
    private KafkaConsumer kafkaConsumer;

    public void setCustomImplementor(String event) {
        if(customImplementor==null) {
            customImplementor = (CustomImplementor) context.getBean(event);
        }
    }

    public void setConsumerWorker(String event) {
        kafkaConsumer.setKafkaWorker(customImplementor.getConsumerWorker(event));
    }

    public Map<String, Object> getConsumerConfigs(String consumerGroup, String event) {
        return customImplementor.getConsumerConfigs(consumerGroup, event);
    }
}
