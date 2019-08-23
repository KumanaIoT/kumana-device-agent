package com.kumana.iotp.listners;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kumana.iotp.AlertModel;
import com.kumana.iotp.KumanaAwsClientConnection;
import com.kumana.iotp.shadow.CreateJsonSchema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AlertSubscriber implements AlertListner {

    private final Logger logger = LoggerFactory.getLogger(AlertSubscriber.class);

    @Autowired
    CreateJsonSchema createJsonSchema;

    @Autowired
    KumanaAwsClientConnection connection;


    List<AlertModel> alertModelList = new ArrayList();

    Set<AlertModel> alertModelSet = new HashSet();

    /**
     * alert message will be received to here once
     * cloud connector receives an alert.
     **/
    @Override
    public void onMessageReceived(AlertModel alertModel) {
        logger.info("Message Received For An Alert");
            if (alertModel != null) {
                alertModelSet.add(alertModel);
                addAlert(alertModel);
            }
    }


    public synchronized void addAlert(AlertModel alertModel){
        this.alertModelSet.add(alertModel);
    }


    @Scheduled(fixedDelayString = "${fixedDelay.in.milliseconds}")
    public void sendAlerts(){
            String shadow = null;
        try {
            if (!alertModelSet.isEmpty()){
                alertModelList.clear();
                alertModelList.addAll(alertModelSet);
                shadow = createJsonSchema.generateAlerts(alertModelList);
                connection.sendMqtt(shadow);
            }else{
                logger.info("alerts list is empty not sending...");
            }
        } catch (JsonProcessingException e) {
            logger.warn("can't form shadow from the received alert");
        }
    }



}
