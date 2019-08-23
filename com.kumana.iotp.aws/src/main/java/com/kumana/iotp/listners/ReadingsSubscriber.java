package com.kumana.iotp.listners;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kumana.iotp.KumanaAwsClientConnection;
import com.kumana.iotp.SensorReadingModel;
import com.kumana.iotp.shadow.CreateJsonSchema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class ReadingsSubscriber implements ReadingsListner {

    private final Logger logger = LoggerFactory.getLogger(ReadingsSubscriber.class);

    @Autowired
    CreateJsonSchema createJsonSchema;

    @Autowired
    KumanaAwsClientConnection awsConnection;

    HashMap<String, String> sensorValues = new HashMap();


    int messageCount = 0;

    /**
     * this will get executed once cloud connector gets a
     * sensor reading.
     **/
    @Override
    public void onMessageReceived(SensorReadingModel sensorReadingModel) {
        logger.info("Sensor data received");
        ++messageCount;
        try {
            if (sensorReadingModel != null) {
                sensorValues.putAll(sensorReadingModel.getReading());
                if (messageCount == 8) {
                    String shadow = createJsonSchema.generateReported(sensorValues);
                    awsConnection.sendMqtt(shadow);
                    messageCount = 0;
                }
            } else {
                logger.warn("recevied sensor data is null");
            }
        } catch (JsonProcessingException e) {
            logger.warn("cant't create shadow from the received sensor data");
        }
    }

}