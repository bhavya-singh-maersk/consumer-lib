package com.maersk.consumerlib.enums;

public enum QueueType {
	
	NORMAL,
	DLT,
	RETRY;

	public String value() {
		switch (this) {
			case NORMAL: return "normal";
			case DLT: return "dlt";
			case RETRY: return "retry";
		}
		return "";
	}
}
