package com.maersk.consumerlib.services;



import java.util.Map;

/**
 * User needs to implement only the CustomImplementor class to get the Kafka Consumer Working.
 * @author nandish.k
 *
 */
public interface CustomImplementor <T> {


	Worker<T> getConsumerWorker(String event);

	Map<String, Object> getConsumerConfigs(String consumerGroup, String event);

	
}
