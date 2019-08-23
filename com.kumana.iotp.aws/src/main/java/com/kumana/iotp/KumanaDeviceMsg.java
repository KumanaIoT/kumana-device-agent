package com.kumana.iotp;

import com.amazonaws.services.iot.client.AWSIotMessage;
import com.amazonaws.services.iot.client.AWSIotQos;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * this class represents a AwsIotMessage
 * this callbacks will get executed according to the status of the message
 **/

public class KumanaDeviceMsg extends AWSIotMessage {
    public KumanaDeviceMsg(String topic, AWSIotQos qos, String payload) {
        super(topic, qos, payload);
    }

    private final Logger logger = LoggerFactory.getLogger(KumanaDeviceMsg.class);


    @Override
    public void onSuccess() {
        // called when message publishing succeeded
        logger.info("aws device msg is successful");
    }

    @Override
    public void onFailure() {
        // called when message publishing failed
        logger.info("aws device msg failed");
    }

    @Override
    public void onTimeout() {
        // called when message publishing timed out
        logger.warn("aws device msg timed out");
    }
}
