package com.kumana.iotp.lite.actuatorlistner;

import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

public class ConcertLiteHubConsumer {

    private static final String ALL_CHARACTER = "#";
    private static final String FORWARD_SLASH = "/";
    private String topicPrefix;
    private String broker;
    private final String consumerName;

    private static final Logger logger = LoggerFactory.getLogger(ConcertLiteHubConsumer.class);
	private static final String CONCERT_IOTHUB_APPAGENT_USERNAME = "concert.iothub.deviceagent.username";
	private static final String CONCERT_IOTHUB_APPAGENT_PASSWORD = "concert.iothub.deviceagent.password";
	
	private static String userName = "kumanaUser";
	private static String password = "kumanaP@55w0rdr0cKs";
    
    @Autowired
    private MQTTConnectionUtil mqttConnectionUtil;
    
    @Autowired
    private Environment env;

    public ConcertLiteHubConsumer(final String consumerName, final String eventTopic, final String brokerEndpoint) {
        this.consumerName = consumerName;
        this.topicPrefix = eventTopic + FORWARD_SLASH + ALL_CHARACTER;
        this.broker = brokerEndpoint;
    }
    
    @PostConstruct
    public void start() {
    	
    	resolveCredentials();
        logger.info("Attempting to Start Concert Lite Hub Consumer...");
        MQTTConsumerCallbackProcessor mqttConsumerCallbackProcessor = new MQTTConsumerCallbackProcessor(this);
        mqttConnectionUtil.connectToMQTTBroker(broker,consumerName, topicPrefix, mqttConsumerCallbackProcessor);
        logger.info("Lite IOT Hub Consumer Started...");

    }
    
    private void resolveCredentials() {
		final String externalUserName = env.getProperty(CONCERT_IOTHUB_APPAGENT_USERNAME);
		final String externalPassword = env.getProperty(CONCERT_IOTHUB_APPAGENT_PASSWORD);
    	
		if(externalUserName != null && !externalUserName.isEmpty()){
			logger.debug("External APP AGENT username detected. Using value of : {}", externalUserName);
			userName = externalUserName;
		}else{
			logger.debug("Using default username of : {} for APP AGENT communication", userName);
		}

		if(externalPassword != null && !externalPassword.isEmpty()){
			logger.debug("External APP AGENT password detected. Using value of : {}", externalPassword);
			password = externalPassword;
		}else{
			logger.debug("Using default password of : {} for APP AGENT communication", password);
		}
		
		mqttConnectionUtil.setUserName(userName);
		mqttConnectionUtil.setPassword(password);
    }

    public void reConnect(final MqttCallback mqttCallback){
    	mqttConnectionUtil.connectToMQTTBroker(broker,consumerName, topicPrefix, mqttCallback);
    }
}
