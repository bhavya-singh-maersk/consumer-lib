package com.maersk.consumerlib.services;



import java.util.Map;


public interface CustomImplementor <T> {


	Worker<T> getConsumerWorker(String event);

	Map<String, Object> getConsumerConfigs(String consumerGroup, String event);

	
}
