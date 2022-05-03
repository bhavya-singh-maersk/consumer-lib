package com.maersk.consumerlib.config;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.kafka.listener.SeekToCurrentErrorHandler;
import org.springframework.lang.Nullable;
import org.springframework.util.backoff.FixedBackOff;

import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;

public class SeekToCurrentErrorHandlerAndCommitOffset extends SeekToCurrentErrorHandler {

    /**
     * Seek Error  handler argument constructor.
     *
     * @param recoverer   of type BiConsumer.
     * @param maxFailures of type int.
     */
    public SeekToCurrentErrorHandlerAndCommitOffset(@Nullable BiConsumer<ConsumerRecord<?, ?>, Exception> recoverer, int maxFailures) {
        super(recoverer, new FixedBackOff(0, 3));
    }

    /**
     * This method is used to handle consumer errors.
     *
     * @param thrownException of type Exception
     * @param records         of type List
     * @param consumer        of type Consumer
     * @param container       of type MessageListenerContainer
     */
    @Override
    public void handle(Exception thrownException, List<ConsumerRecord<?, ?>> records, Consumer<?, ?> consumer, MessageListenerContainer container) {
        super.handle(thrownException, records, consumer, container);
        ConsumerRecord<?, ?> record = records.get(0);
        consumer.commitSync(Collections.singletonMap(new TopicPartition(record.topic(), record.partition()),
                new OffsetAndMetadata(record.offset() + 1)));
    }
}
