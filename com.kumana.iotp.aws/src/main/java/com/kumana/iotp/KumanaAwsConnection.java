package com.kumana.iotp;

import org.springframework.stereotype.Service;

@Service
public abstract class KumanaAwsConnection<T> {

    boolean clientConnected = false;

    abstract void prepareClient();

    abstract void sendMqtt(String shadow);

}
