package com.kumana.iotp.Routers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.Router;
import org.springframework.messaging.handler.annotation.Header;

/**
 * Created by Admin on 14/07/2017.
 */

/**
 * this is the place where all the messages are received
 * and they are being routed according to the topics they have...
 **/
@MessageEndpoint
public class MessageRouter {

    private final Logger logger = LoggerFactory.getLogger(MessageRouter.class);


    static final String ALERT = "ALERT";
    static final String READING = "READING";

    @Router(inputChannel = "mqttRouterChannel")
    public String route(@Header("mqtt_topic") String topic) {
        String route = null;
        switch (topic) {
            case ALERT:
                logger.info("alert message received");
                route = "alertTransformerChannel";
                break;
            case READING:
                logger.info("reading message received");
                route = "readingTransformerChannel";
                break;
        }
        return route;
    }

    /**
     * in case of an alert the message will be routed to alertTransformerChannel
     * which leads .to AlertTransformer.transFormAlert();
     * **/

    /**
     * in case of a reading the message will be routed to readingTransformerChannel
     * which leads to ReadingTransformer.transformReading()
     * **/


}
