package com.kumana.iotp.sensorContol;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kumana.iotp.MqOutGateway;
import com.kumana.iotp.publishers.Publish;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@Service
public class Publisher implements Publish {

    private final Logger logger = LoggerFactory.getLogger(Publisher.class);

    @Autowired
    MqOutGateway mqOutGateway;

    ObjectMapper mapper = new ObjectMapper();

    private final static String ACTUATOR = "actuator";

    @Override
    public void sendMsg(HashMap<String, String> actuatorValues) {
        try {
            String payload = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(actuatorValues);
            Message messageBuild = MessageBuilder.withPayload(payload)
                    .setHeader(MqttHeaders.TOPIC, ACTUATOR)
                    .build();
            mqOutGateway.send(messageBuild);
        } catch (JsonProcessingException e) {
            logger.error("couldn't process the actuator message from the cloud");
        }
    }

}