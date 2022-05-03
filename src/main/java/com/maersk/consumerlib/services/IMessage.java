package com.maersk.consumerlib.services;

public interface IMessage<T> {

	T getDeserializedObject();
	String getEvent();
	String getRawData();
	Integer getPartition();
	T getDeserializedObject(Class<T> clazz);
}
