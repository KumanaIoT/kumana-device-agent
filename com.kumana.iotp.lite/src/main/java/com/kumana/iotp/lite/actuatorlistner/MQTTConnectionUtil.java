package com.kumana.iotp.lite.actuatorlistner;

import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.kumana.iotp.lite.util.TLSUtil;

@Component
public class MQTTConnectionUtil {

    private static final Logger logger = LoggerFactory.getLogger(MQTTConnectionUtil.class);
    private static final int MQTT_RECONNECT_RETRY_INTERVAL = 1500;
	private String userName;
    private String password;
    	
	@Autowired
	private TLSUtil tlsUtil;

    public void connectToMQTTBroker(final String brokerURL, final String consumerName, final String subscriptionTopic, final MqttCallback mqttCallbackProcessor){

        boolean isConnectedToIOTHub = false;
        while(!isConnectedToIOTHub) {
            try {
            	
                MqttClient sampleClient = new MqttClient(brokerURL, consumerName, new MemoryPersistence());
                MqttConnectOptions connOpts = new MqttConnectOptions();
    			connOpts.setCleanSession(true);
    			connOpts.setSocketFactory(tlsUtil.getSocketFactoryWithX509Cert());
    			connOpts.setUserName(userName);
    			connOpts.setPassword(password.toCharArray());
                sampleClient.connect(connOpts);
                sampleClient.subscribe(subscriptionTopic, 1);
                sampleClient.setCallback(mqttCallbackProcessor);
                isConnectedToIOTHub = true;
            } catch (Exception ex) {
                logger.error("Error connecting to MQTT Broker. Error {}", ex.getMessage());
                logger.info("Retrying in : 1500ms...");
            }
            try {
                Thread.sleep(MQTT_RECONNECT_RETRY_INTERVAL);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    
    public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
