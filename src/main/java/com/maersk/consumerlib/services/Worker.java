package com.maersk.consumerlib.services;

import java.util.List;

public interface Worker <T> {

    void processMessages(List<T> messages);
}
