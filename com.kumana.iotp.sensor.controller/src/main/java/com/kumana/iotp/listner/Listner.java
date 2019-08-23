package com.kumana.iotp.listner;

import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public interface Listner {
    public void onMessageReceived(HashMap<String, String> actuatorValues);
}
