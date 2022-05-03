package com.maersk.consumerlib.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.retry.RetryPolicy;
import org.springframework.retry.backoff.BackOffPolicy;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.backoff.FixedBackOff;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class KafkaConsumerConfig <T> {

    //@Value("${poll.timeout.ms}")
    //private long pollTimeoutMS;

    @Autowired
    private KafkaCustomConfig kafkaCustomConfig;

    public ConcurrentKafkaListenerContainerFactory<String, T> kafkaListenerContainerFactory(String consumerGroup, int consumerThreads, String event) {
        ConcurrentKafkaListenerContainerFactory<String, T> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory(consumerGroup,event));
        factory.setConcurrency(consumerThreads);
        factory.setRetryTemplate(retryTemplate());
        factory.getContainerProperties().setPollTimeout(100);
        factory.setCommonErrorHandler(new DefaultErrorHandler(new FixedBackOff(0, 1)));
        return factory;
    }

    public ConsumerFactory<String, T> consumerFactory(String consumerGroup, String event) {
        return new DefaultKafkaConsumerFactory<>(kafkaCustomConfig.getConsumerConfigs(consumerGroup, event));
    }

    public static Map<String, Object> getConsumerConfigs(String bootstrapServers, boolean enableAutoCommit, String autoCommitIntervalMS, String sessionTimeoutMS, String autoOffsetReset, Integer maxPollRecords, String consumerGroup, String event) {
        Map<String, Object> propsMap = new HashMap<>();
        propsMap.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        propsMap.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, enableAutoCommit);
        propsMap.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, autoCommitIntervalMS);
        propsMap.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, sessionTimeoutMS);
        propsMap.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        propsMap.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        propsMap.put(ConsumerConfig.GROUP_ID_CONFIG, consumerGroup);
        propsMap.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, autoOffsetReset);
        propsMap.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, maxPollRecords);
        propsMap.put(ConsumerConfig.REQUEST_TIMEOUT_MS_CONFIG, "65000");
        //propsMap.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "60000");

        propsMap.put(ConsumerConfig.RECEIVE_BUFFER_CONFIG,"1048576");
        propsMap.put(ConsumerConfig.SEND_BUFFER_CONFIG,"1048576");
        String clientId = event+"-"+consumerGroup;
        propsMap.put(ConsumerConfig.CLIENT_ID_CONFIG, clientId);
        propsMap.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, "2147483647");
        return propsMap;
    }

    private RetryPolicy retryPolicy() {
        SimpleRetryPolicy policy = new SimpleRetryPolicy();
        policy.setMaxAttempts(3);
        return policy;
    }

    private BackOffPolicy backOffPolicy() {
        ExponentialBackOffPolicy policy = new ExponentialBackOffPolicy();
        policy.setInitialInterval(2 * 1000L);
        policy.setMaxInterval(4 * 1000L);
        return policy;
    }

    private RetryTemplate retryTemplate() {
        RetryTemplate template = new RetryTemplate();
        template.setRetryPolicy(retryPolicy());
        template.setBackOffPolicy(backOffPolicy());
        return template;
    }
}
