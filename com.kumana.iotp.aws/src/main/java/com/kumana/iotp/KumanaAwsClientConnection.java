package com.kumana.iotp;

import com.amazonaws.services.iot.client.*;
import com.kumana.iotp.publishers.Publish;
import com.kumana.iotp.utils.Utillities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;


@Service
public class KumanaAwsClientConnection extends KumanaAwsConnection<String> {

    private final Logger logger = LoggerFactory.getLogger(KumanaAwsClientConnection.class);


    private AWSIotMqttClient awsIotClient;
    private AWSIotMqttClient awsIotActuatorClient;

    private AWSIotDevice device;

    /**
     * properties needed to establish an aws connection
     * fetched from property file.
     * properties are mandatoy cant start the application without them.
     **/
    String clientEndpoint;
    String clientId;
    String certificateFile;
    String privateKeyFile;
    String thingName;
    String topic;
    String updateTopic;

    /**
     * the config file holding every properties.
     **/
    @Autowired
    KumanaAwsIotConfig awsIotConfig;

    @Autowired
    KumanaDeviceMsg kumanaDeviceMsg;

    @Autowired
    List<Publish> publishers;
    
    @Autowired
    KumanaDeviceTopic kumanaDeviceTopic;

    /**
     * this will get executed once the bean is instantiated
     **/
    @PostConstruct
    void init() {
        clientEndpoint = awsIotConfig.getClientEndpoint();
        clientId = awsIotConfig.getClientId();
        certificateFile = awsIotConfig.getCertificateFile();
        privateKeyFile = awsIotConfig.getPrivateKeyFile();
        thingName = awsIotConfig.getThingName();
        topic = awsIotConfig.getTopic();
        updateTopic = awsIotConfig.getUpdateTopic();

        KumanaDeviceTopic.publishers = publishers;
        Utillities.KeyStorePasswordPair pair = Utillities.getKeyStorePasswordPair(certificateFile, privateKeyFile);
        //this.awsIotClient = new AWSIotMqttClient(clientEndpoint, clientId, pair.keyStore, pair.keyPassword);
        this.awsIotClient = new KumanaMqttClient(clientEndpoint, clientId, pair.keyStore, pair.keyPassword, kumanaDeviceTopic);
        this.awsIotActuatorClient = new KumanaMqttClient(clientEndpoint, clientId, pair.keyStore, pair.keyPassword, kumanaDeviceTopic);
        device = new AWSIotDevice(thingName);
               
        try {
			awsIotActuatorClient.connect();
		    awsIotActuatorClient.subscribe(kumanaDeviceTopic);
		} catch (Exception e) {
			logger.error("An error occured while subscribing to topic [{}] . Cause: [{}] ",topic,e.getCause());
		}      
    }


    /**
     * preparing the aws client this will
     * establish the aws client
     **/
    public void prepareClient() {

        try {
            awsIotClient.attach(device);
            awsIotActuatorClient.attach(device);                        
                         
            if (awsIotActuatorClient.getConnectionStatus().equals(AWSIotConnectionStatus.DISCONNECTED)) {
            	logger.info("Reconnecting awsIotActuatorClient.");
                awsIotActuatorClient.connect();
                awsIotActuatorClient.subscribe(kumanaDeviceTopic);
            }           
                                   
            /**
             * establishing a connection is a blocking call so this
             * won't keep on retrying to establish the connection
             **/
            if (awsIotClient.getConnectionStatus().equals(AWSIotConnectionStatus.DISCONNECTED)) {            	
                logger.info("Reconnecting awsIotReportingClient.");
                awsIotClient.connect();
                
                if (awsIotClient.getConnectionStatus().equals(AWSIotConnectionStatus.CONNECTED)) {
                    clientConnected = true;
                }           	
            }
            logger.debug("Reporting Client Status: "+awsIotClient.getConnectionStatus());
            logger.debug("Actuator Client Status: "+awsIotActuatorClient.getConnectionStatus());

        } catch (AWSIotException e) {
            logger.warn("aws mqtt client connection failed", e);
        }
    }


    /**
     * sending mqtt to shadow is handled here.
     **/
    public void sendMqtt(String shadow) {
        try {

            if (clientConnected) {

                /**
                 *even if the client is connected we need to check the status
                 * in an network loss the connection status will be marked as DISCONNECTED
                 **/
                if (awsIotClient.getConnection().getConnectionStatus().equals(AWSIotConnectionStatus.DISCONNECTED) ||
                		awsIotActuatorClient.getConnection().getConnectionStatus().equals(AWSIotConnectionStatus.DISCONNECTED)) {
                    logger.warn("aws mqtt client is disconnected");
                    clientConnected = false;
                }

            } else {
                /**
                 * if client is not connected.then a client will be created
                 **/
                prepareClient();
            }

            /**
             * aws client status RECONNECTING.)
             * note :reconnect is switched off
             * **/
            if (awsIotClient.getConnection().getConnectionStatus().equals(AWSIotConnectionStatus.RECONNECTING)) {
                logger.warn("aws mqtt client is trying to reconnect");
            }


            /**
             * aws client is CONNECTED.
             * then the message will be published - publishing is non blocking
             * KUMANADEVICEMSG will receive the callbacks.
             * **/
            if (awsIotClient.getConnection().getConnectionStatus().equals(AWSIotConnectionStatus.CONNECTED)) {
                logger.info("aws client is connected");
                //KumanaDeviceMsg message = new KumanaDeviceMsg(updateTopic,AWSIotQos.QOS0,shadow);
                kumanaDeviceMsg.setQos(AWSIotQos.QOS0);
                kumanaDeviceMsg.setStringPayload(shadow);
                kumanaDeviceMsg.setTopic(updateTopic);
                //awsIotClient.publish(message,5000);
                awsIotClient.publish(kumanaDeviceMsg);
                logger.debug("Publishing to topic: "+updateTopic+" : shadow: "+shadow);
            }


        } catch (AWSIotException e) {
            logger.warn("aws mqtt client connection failed trying to connect");
            /**
             * failed to establish an connection trying again.
             * **/
            prepareClient();
        }
    }


}
