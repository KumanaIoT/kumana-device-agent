package com.kumana.iotp.lite.actuatorlistner;

import org.eclipse.paho.client.mqttv3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.ComponentScan;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kumana.iotp.publishers.Publish;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class MQTTConsumerCallbackProcessor implements MqttCallback {

    private static final Logger logger = LoggerFactory.getLogger(MQTTConsumerCallbackProcessor.class);

    private ConcertLiteHubConsumer concertIOTHubConsumer;
    
    private static List<Publish> publishers;

	private ObjectMapper mapper = new ObjectMapper();
    
    @Override
    public void connectionLost(Throwable throwable) {
        logger.info("Connection to MQTT Broker has been lost...");
        this.concertIOTHubConsumer.reConnect(this);
        logger.info("Connection to MQTT Broker restored...");
    }
    
    @Override
    public void messageArrived(String mqttTopic, MqttMessage mqttMessage) {


        JsonNode node;
		try {
	        logger.info("Message arrived from topic : " + mqttTopic + " | Message : " + new String(mqttMessage.getPayload()) + " | Message ID : " +mqttMessage.getId());
			node = mapper.readTree(mqttMessage.getPayload());
	        HashMap<String, String> result = mapper.readValue(node.toString(), HashMap.class);
	        
	        publishers.forEach(publish -> publish.sendMsg(result));
	        result.forEach((s, s2) -> logger.info("actuator  key :- " + s + " --- actuator value  " + s2));
		} catch (Exception e) {
			logger.error("Exception",e.getCause());
			e.printStackTrace();
		}
    }
    
    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
        // For this consumer, this method should not be invoked...
        throw  new UnsupportedOperationException();
    }

    public MQTTConsumerCallbackProcessor(final ConcertLiteHubConsumer concertIOTHubConsumer){

        if(concertIOTHubConsumer == null){
            throw new IllegalArgumentException("Concert Hub consumer should not be empty");
        }
        this.concertIOTHubConsumer = concertIOTHubConsumer;
    }
    
    
    public static void setPublishers(List<Publish> publishers) {
		MQTTConsumerCallbackProcessor.publishers = publishers;
	}
}
