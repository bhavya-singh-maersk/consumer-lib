package com.maersk.consumerlib.kafka;

import com.maersk.consumerlib.services.Worker;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@Component
public class KafkaConsumer<T> {

    //@Value("${poll.timeout.ms}")
    //private long pollTimeoutMS;//100

    @Autowired
    private Worker kafkaWorker;

    @Async
    public void startConsumer(Consumer consumer) {
        List<T> messages = new ArrayList<>();
        try{
            while(true) {
                ConsumerRecords<String, T> consumerRecords = consumer.poll(Duration.ofMillis(100));
                for (TopicPartition partition : consumerRecords.partitions()) {
                    List<ConsumerRecord<String, T>> partitionRecords = consumerRecords.records(partition);
                    for (ConsumerRecord<String, T> eachRecord : partitionRecords) {
                        log.info(eachRecord.offset() + ": " + eachRecord.value());
                        messages.add(eachRecord.value());
                    }
                    kafkaWorker.processMessages(messages);
                    long lastOffset = partitionRecords.get(partitionRecords.size() - 1).offset();
                    consumer.commitSync(Collections.singletonMap(partition, new OffsetAndMetadata(lastOffset + 1)));
                    messages.clear();
                }
            }
        }catch(Exception e){
            consumer.close();
            log.error("Error in message consuming",e);
            KafkaConsumerInitiator.aliveQueues--;
            log.info("Number of alive consumer left: "+KafkaConsumerInitiator.aliveQueues);
           /* if(KafkaConsumerInitiator.aliveQueues==0){
                System.exit(1);
            }*/
        }
        finally
        {
            consumer.close();
        }
    }

    public void setKafkaWorker(Worker kafkaWorker) {
        this.kafkaWorker = kafkaWorker;
    }
}
