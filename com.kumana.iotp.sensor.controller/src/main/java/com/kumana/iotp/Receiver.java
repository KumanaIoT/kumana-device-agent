package com.kumana.iotp;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kumana.iotp.listner.Listner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.Router;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Header;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@MessageEndpoint
public class Receiver {

    private final Logger logger = LoggerFactory.getLogger(Receiver.class);

    @Autowired(required = false)
    List<Listner> listners;

    ObjectMapper mapper = new ObjectMapper();

    @ServiceActivator(inputChannel = "mqttInboundChannel")
    public void receiver(Message message) {
        logger.info("actuator msg received. start processing");
        JsonNode root = null;
        try {
            root = mapper.readTree(message.getPayload().toString());
            HashMap<String, String> result = mapper.readValue(root.toString(), HashMap.class);
            if (listners != null) listners.forEach(listner -> listner.onMessageReceived(result));
        } catch (IOException e) {
            logger.error("couldn't process the actuator msg from the device");
        }
    }
}