package com.maersk.consumerlib.kafka;

import com.maersk.consumerlib.enums.QueueType;
import com.maersk.consumerlib.exception.ConfigValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Component
public class KafkaConfigResolver {

    @Autowired
    private Environment environment;

    public List<String> getEvents() throws ConfigValidationException {
        environment.resolvePlaceholders("${kafka.notification.topic}");
        String eventName = environment.getProperty("kafka.event");
        if(Objects.nonNull(eventName) && !eventName.isEmpty()){
            return Arrays.asList(eventName.split(","));
        }
        throw new ConfigValidationException("Event name not configured");
    }

    public String getEventName() throws ConfigValidationException {
        String eventName = environment.getProperty("kafka.event");
        if(Objects.nonNull(eventName) && !eventName.isEmpty()){
            return eventName;
        }
        throw new ConfigValidationException("Event name not configured");
    }

    public List<String> getTopics(String event, QueueType queueType) throws ConfigValidationException {
        String topicStr;
        if(QueueType.DLT == queueType){
            topicStr = getDeadTopic(event);
        } else if(QueueType.RETRY == queueType) {
            topicStr = getRetryTopic(event);
        } else {
            topicStr = environment.getProperty("kafka."+event+".topic");
        }
        if(Objects.nonNull(topicStr) && !topicStr.isEmpty()){
            return Arrays.asList(topicStr.split(","));
        }
        throw new ConfigValidationException("kafka topics not configured in kafka.properties for event: "+event);
    }

    public List<String> getTopics(String event) throws ConfigValidationException {
        List<String> topicList = new ArrayList<>();
        topicList.add(getTargetTopic(event));
        topicList.add(getEventRetryTopic(event));
        topicList.add(getDeadLetterTopic(event));
        return topicList;
    }

    public String getDeadTopic(String event) throws ConfigValidationException {
        String topic = environment.getProperty("kafka.topics.event.dead."+event);
        if(Objects.nonNull(topic) && !topic.isEmpty()){
            return topic;
        }
        throw new ConfigValidationException("kafka dead topic not configured in kafka.properties for event: "+event);
    }

    public String getRetryTopic(String event) throws ConfigValidationException {
        String topic = environment.getProperty("kafka.topics.event.retry."+event);
        if(Objects.nonNull(topic) && !topic.isEmpty()){
            return topic;
        }
        throw new ConfigValidationException("kafka dead topic not configured in kafka.properties for event: "+event);
    }

    public String getConsumerGroup(String event) throws ConfigValidationException {
        String consumerGroup = environment.getProperty("kafka.listener."+event+".group");
        if(Objects.nonNull(consumerGroup) && !consumerGroup.isEmpty()){
            return consumerGroup;
        }
        throw new ConfigValidationException("kafka topics consumer group not configured in kafka.properties.");
    }

    public Integer getConsumerThreads(String event) throws ConfigValidationException {
        String consumerThreads = environment.getProperty("kafka.listener."+event+".thread");
        if(Objects.nonNull(consumerThreads) && !consumerThreads.isEmpty()){
            return Integer.parseInt(consumerThreads);
        }
        throw new ConfigValidationException("kafka consumer threads count not configured in kafka.properties for event: "+event);
    }


    public String getTargetTopic(String event) throws ConfigValidationException {
        String topic = environment.getProperty("kafka.listener."+event+".topic.normal");
        if(Objects.nonNull(topic) && !topic.isEmpty()){
            return topic;
        }
        throw new ConfigValidationException("kafka target topic not configured for event: "+event);
    }

    public String getEventRetryTopic(String event) throws ConfigValidationException {
        String topic = environment.getProperty("kafka.listener."+event+".topic.retry");
        if(Objects.nonNull(topic) && !topic.isEmpty()){
            return topic;
        }
        throw new ConfigValidationException("kafka retry topic not configured for event: "+event);
    }

    public String getDeadLetterTopic(String event) throws ConfigValidationException {
        String topic = environment.getProperty("kafka.listener."+event+".topic.dlt");
        if(Objects.nonNull(topic) && !topic.isEmpty()){
            return topic;
        }
        throw new ConfigValidationException("kafka dead letter topic not configured for event: "+event);
    }
}
