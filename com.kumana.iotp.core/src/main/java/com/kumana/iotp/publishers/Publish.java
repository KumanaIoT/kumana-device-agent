package com.kumana.iotp.publishers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public interface Publish {
    public void sendMsg(HashMap<String, String> actuatorValues);
}