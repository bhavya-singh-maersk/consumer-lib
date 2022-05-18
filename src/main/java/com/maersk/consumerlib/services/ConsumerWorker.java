package com.maersk.consumerlib.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class ConsumerWorker<T> implements Worker<T>{

    @Override
    public void processMessages(List<T> messages) {
         log.info("Consumed message: {}", messages);
    }
}
