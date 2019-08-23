package com.kumana.iotp.Transforming;

import com.fasterxml.jackson.databind.ObjectMapper;;
import com.kumana.iotp.AlertModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.Transformer;
import org.springframework.messaging.Message;

import java.io.IOException;

/**
 * Created by Admin on 14/07/2017.
 */
@MessageEndpoint
public class AlertTransformer {


    ObjectMapper mapper;

    private final Logger logger = LoggerFactory.getLogger(AlertTransformer.class);


    /**
     * mqtt messages with ALERT topics are received here.
     * they will be converted to AlertModel class and redirect to mqttAlertChannel
     * which means to AlertEmmiter.process();
     **/
    @Transformer(inputChannel = "alertTransformerChannel", outputChannel = "mqttAlertChannel")
    public AlertModel transformAlert(Message<String> message) {
        AlertModel alert = null;

        if (mapper == null) {
            mapper = new ObjectMapper();
        }

        try {
            logger.info("trying to get an alert from the mqtt payload");
            alert = mapper.readValue(message.getPayload(), AlertModel.class);
        } catch (IOException e) {
            return alert = new AlertModel();
        }

        return alert;
    }

}
