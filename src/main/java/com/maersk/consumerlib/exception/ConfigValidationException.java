package com.maersk.consumerlib.exception;

public class ConfigValidationException extends RuntimeException{

    public ConfigValidationException(String message)
    {
        super(message);
    }
}
