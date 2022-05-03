package com.maersk.consumerlib.kafka;

import com.maersk.consumerlib.config.KafkaConsumerConfig;
import com.maersk.consumerlib.config.KafkaCustomConfig;
import com.maersk.consumerlib.enums.QueueType;
import com.maersk.consumerlib.exception.ConfigValidationException;
import com.maersk.consumerlib.services.ConsumerInitiator;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Slf4j
@Component
public class KafkaConsumerInitiator implements ConsumerInitiator {

    @Autowired
    private KafkaConfigResolver configResolver;

    @Autowired
    private KafkaConsumerConfig kafkaConsumerConfig;

    @Autowired
    private KafkaCustomConfig kafkaCustomConfig;

    @Autowired
    private ApplicationContext context;

    public static int aliveQueues=0;

    @Override
    public void startConsumer() {
        var events = configResolver.getEvents();
        log.info("Events: {}", events);
        events.forEach(eachEvent -> {
            log.info("Current event: {}", eachEvent);
            String consumerGroup = configResolver.getConsumerGroup(eachEvent);
            int consumerThreads = configResolver.getConsumerThreads(eachEvent);
            List<String> topicList = configResolver.getTopics(eachEvent);
            if(CollectionUtils.isEmpty(topicList)){
                throw new ConfigValidationException("Topics not configured");
            }
            log.info("Topics: "+topicList);
            initiateConsumer(consumerGroup, consumerThreads, topicList, eachEvent);
        });
    }

    public void initiateConsumer(String consumerGroup, int consumerThreads, List<String> topicList, String event){

        //To set the custom implementor
        kafkaCustomConfig.setCustomImplementor(event);
        KafkaConsumer consumer = getMessageProcessor(event);

        ConcurrentKafkaListenerContainerFactory factory = kafkaConsumerConfig.kafkaListenerContainerFactory(consumerGroup, consumerThreads, event);
        for(int i =0;i<consumerThreads;i++){
            Consumer cons = factory.getConsumerFactory().createConsumer();
            cons.subscribe(topicList);
            consumer.startConsumer(cons);
            aliveQueues++;
        }
    }

    public KafkaConsumer getMessageProcessor(String event) {
        switch(event){
            default: {
                KafkaConsumer consumer =  context.getBean(KafkaConsumer.class);
                kafkaCustomConfig.setConsumerWorker(event);
                return consumer;
            }
        }
    }
}
