package com.kumana.iotp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kumana.iotp.listners.AlertListner;
import com.kumana.iotp.publishers.Publish;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AlertReading implements AlertListner {

    private final Logger logger = LoggerFactory.getLogger(AlertReading.class);

    @Autowired
    List<Publish> publishes;

    //int i = 1;

    HashMap<String, String> actuatorMap = new HashMap();

    @Override
    public void onMessageReceived(AlertModel alertModel) {
        //if (i==1){
        //    actuatorMap.put("waterpump","off");
        //     publishes.forEach(publish -> publish.sendMsg(actuatorMap));
        //}
        //++i;
    }

}
