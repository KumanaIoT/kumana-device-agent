package com.kumana.iotp.ServiceEmmiters;


import com.kumana.iotp.AlertModel;
import com.kumana.iotp.MqOutGateway;
import com.kumana.iotp.listners.AlertListner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;

import java.util.List;

/**
 * Created by Admin on 13/07/2017.
 */
@MessageEndpoint
public class AlertEmmiter {


    private final Logger logger = LoggerFactory.getLogger(AlertEmmiter.class);

    private static final String MQTTTOPIC = "mqtt_topic";

    @Autowired
    List<AlertListner> alertListners;

    /**
     * this is the final stage of the cloud connector.
     * the AlertModel will now be distributed to all alertListners
     * cloudImpl should have AlertListner implementations within the project.
     **/
    @ServiceActivator(inputChannel = "mqttAlertChannel")
    public void process(AlertModel alert) {
        //store.addAlert(alert);
        if (alert != null) {
            logger.info("alert message distributed to listners..");
            alertListners.forEach(alertListner -> alertListner.onMessageReceived(alert));
        }

    }


}