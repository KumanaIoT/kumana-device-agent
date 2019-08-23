package com.kumana.iotp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

import java.util.HashMap;


/**
 * this is an mqtt outbound gateway.
 * this can publish messages to mqtt topics.
 **/


@Service
public class SendMqMsg {

    private final Logger logger = LoggerFactory.getLogger(SendMqMsg.class);

    @Autowired
    MqOutGateway mqOut;


    ObjectMapper MAPPER = new ObjectMapper();

    private static final String ALERT = "ALERT";
    private static final String READING = "READING";

    private static final String MQTTTOPIC = "mqtt_topic";

    /**
     * publishing alerts to mqttServer
     **/
    public void sendAlert(AlertModel alert) {

        logger.info("trying to publish alert");
        alert.setStatusChangeTimestamp(System.currentTimeMillis());
        String payload = "";

        try {
            payload = MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(alert);
        } catch (JsonProcessingException e1) {
            logger.warn("json conversion of alerts failed");
            payload = null;
        }

        if (payload != null) {
            Message<String> messageBuild = MessageBuilder.withPayload(payload)
                    .setHeader(MQTTTOPIC, ALERT)
                    .build();
            mqOut.send(messageBuild);
        }

    }


    /**
     * publishing readings to mqtt server
     **/
    public void sendReading(SensorReadingModel sensorReadingModel) {
        //convert and send a reading
        logger.info("trying to publish sensor reading");
        String load = null;
        try {
            load = MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(sensorReadingModel);
        } catch (JsonProcessingException e) {
            sensorReadingModel = null;
            logger.warn("json conversion of sensor reading failed");
        }
        if (sensorReadingModel != null) {
            Message<String> messageBuild = MessageBuilder.withPayload(load)
                    .setHeader(MQTTTOPIC, READING)
                    .build();
            logger.debug(load);
            mqOut.send(messageBuild);
        }
    }


}
