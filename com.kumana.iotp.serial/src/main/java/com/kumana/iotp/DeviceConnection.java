package com.kumana.iotp;

import org.springframework.stereotype.Service;

@Service
public abstract class DeviceConnection {

    boolean connectionIsReady = false;

    abstract boolean setupDevice();


    abstract void triggerDevice(String command);
}
