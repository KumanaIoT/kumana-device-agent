package com.kumana.iotp;

import org.springframework.stereotype.Service;


@Service
public abstract class DeviceConnection {
    boolean connectionIsReady = false;
    boolean connectionIsValid = false;

    abstract boolean setupDevice();
}


