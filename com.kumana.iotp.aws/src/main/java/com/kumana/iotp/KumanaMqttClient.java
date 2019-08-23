package com.kumana.iotp;

import com.amazonaws.services.iot.client.AWSIotException;
import com.amazonaws.services.iot.client.AWSIotMqttClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.KeyStore;

/**
 * this represents the AwsConnection callbacks
 * will get executed on the connection status.
 **/
public class KumanaMqttClient extends AWSIotMqttClient {

    private final Logger logger = LoggerFactory.getLogger(KumanaMqttClient.class);

    private KumanaDeviceTopic kumanaDeviceTopic;

    public KumanaMqttClient(String clientEndpoint, String clientId, KeyStore keyStore, String keyPassword, KumanaDeviceTopic kumanaDeviceTopic) {
        super(clientEndpoint, clientId, keyStore, keyPassword);
        this.maxConnectionRetries = 0;
        this.kumanaDeviceTopic = kumanaDeviceTopic;
    }

    public KumanaMqttClient(String clientEndpoint, String clientId, String awsAccessKeyId, String awsSecretAccessKey) {
        super(clientEndpoint, clientId, awsAccessKeyId, awsSecretAccessKey);
    }

    public KumanaMqttClient(String clientEndpoint, String clientId, String awsAccessKeyId, String awsSecretAccessKey, String sessionToken) {
        super(clientEndpoint, clientId, awsAccessKeyId, awsSecretAccessKey, sessionToken);
    }


    @Override
    public void onConnectionClosed() {
        logger.warn("kumana mqtt client connection closed");
    }


    @Override
    public void onConnectionSuccess() {
        super.onConnectionSuccess();
        try {
            this.subscribe(kumanaDeviceTopic);
            //this.subscribe(TOPIC);
        } catch (AWSIotException e) {
            logger.warn("subscribing to aws update topic failed");
        }
        logger.info("kumana client connection successful");
    }

    @Override
    public void onConnectionFailure() {
        logger.error("kumana client connection failed");
    }

}