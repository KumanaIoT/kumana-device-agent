package com.kumana.iotp;

import com.amazonaws.services.iot.client.AWSIotMessage;
import com.amazonaws.services.iot.client.AWSIotQos;
import com.amazonaws.services.iot.client.AWSIotTopic;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kumana.iotp.publishers.Publish;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;


public class KumanaDeviceTopic extends AWSIotTopic {

    private final Logger logger = LoggerFactory.getLogger(KumanaDeviceTopic.class);

    public KumanaDeviceTopic(String topic, AWSIotQos qos) {
        super(topic, qos);
    }

    public static List<Publish> publishers;

    ObjectMapper mapper = new ObjectMapper();

    @Override
    public void onMessage(AWSIotMessage message) {
        super.onMessage(message);
        logger.info("message received from aws accepted topic --------------");
        try {
            JsonNode root = mapper.readTree(message.getStringPayload());
            JsonNode node = root.path("state").path("latest").path("sensors");
            HashMap<String, String> result = mapper.readValue(node.toString(), HashMap.class);
            publishers.forEach(publish -> publish.sendMsg(result));
            result.forEach((s, s2) -> logger.info("actuator  key :- " + s + " --- actuator value  " + s2));
        } catch (IOException e) {
        }
    }


    @Override
    public void onFailure() {
        super.onFailure();
        logger.error("subscription to aws update topic failed----------------");
    }


    @Override
    public void onSuccess() {
        super.onSuccess();
        logger.info("subscription to aws update topic successful--------------");
    }


}
