package com.kumana.iotp.listners;

import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public interface ControllerListner {


    public void onMessageReceived(HashMap<String, String> readings);


}
