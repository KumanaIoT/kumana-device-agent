package com.kumana.iotp.lite;

import java.util.List;

import javax.annotation.PostConstruct;

import com.kumana.iotp.lite.util.TLSUtil;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.kumana.iotp.lite.actuatorlistner.MQTTConsumerCallbackProcessor;
import com.kumana.iotp.publishers.Publish;

@Service
@Configurable
@ComponentScan({"com.kumana.iotp.cloudconnector","com.kumana.iotp.core", "com.kumana.iotp"})
public class LiteDataPublisher {

	private static final String CONCERT_IOTHUB_APPAGENT_USERNAME = "concert.iothub.deviceagent.username";
	private static final String CONCERT_IOTHUB_APPAGENT_PASSWORD = "concert.iothub.deviceagent.password";
	private final Logger logger = LoggerFactory.getLogger(LiteDataPublisher.class);

	@Autowired
	KumanaLiteIotConfig kumanaLiteIotConfig;
	
    @Autowired
    List<Publish> publishers;

    @Autowired
    private Environment env;

    @Autowired
    private TLSUtil tlsUtil;

	private String mqttServerUri;
	private String mqttReportingClinetName;
	private String reportingTopic;
	private String userName = "kumanaUser";
	private String password = "kumanaP@55w0rdr0cKs";

	
	private MqttClient mqttClient;

	@PostConstruct
	public void init() {
		mqttServerUri = kumanaLiteIotConfig.getMqttServerUri();
		mqttReportingClinetName = kumanaLiteIotConfig.getMqttReportingClientName();
		reportingTopic = kumanaLiteIotConfig.getReportingTopic();
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
		MQTTConsumerCallbackProcessor.setPublishers(publishers);
		
		conncet();
	}
	
	public void conncet() {
				
		try {
			mqttClient = new MqttClient(mqttServerUri, mqttReportingClinetName,
					new MemoryPersistence());
			
			MqttConnectOptions connOpts = new MqttConnectOptions();
			connOpts.setCleanSession(true);
			connOpts.setSocketFactory(tlsUtil.getSocketFactoryWithX509Cert());
			connOpts.setUserName(userName);
			connOpts.setPassword(password.toCharArray());
			mqttClient.connect(connOpts);
		} catch (MqttException e) {
			logger.error("Error occured while connecting to MQTT broker.. [{}]",e.getCause());
			throw new RuntimeException(e.getCause());
		}
		
	}

	public void publishData(final String deviceShadow) {

		try {
			
			if (!mqttClient.isConnected()) {
				conncet();
			}
			
			MqttMessage shadowMessage = new MqttMessage(deviceShadow.getBytes());
			shadowMessage.setQos(1);
			final String publishTopic = reportingTopic;
			logger.info("Attempting to publish data : " + deviceShadow);
			mqttClient.publish(publishTopic, shadowMessage);
		} catch (Exception e) {
			logger.error("Error occured while publishing data.. [{}]",e.getCause());
			throw new RuntimeException(e);
		}
			
		logger.info("Data published successfully...");

	}

}
