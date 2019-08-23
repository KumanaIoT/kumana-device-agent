package com.kumana.iotp;

import com.amazonaws.services.iot.client.AWSIotMqttClient;
import com.amazonaws.services.iot.client.AWSIotQos;
import com.kumana.iotp.utils.Utillities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.io.File;

@Configuration
@ConfigurationProperties("aws")
@Component
public class KumanaAwsIotConfig implements InitializingBean {


    private final Logger logger = LoggerFactory.getLogger(KumanaAwsIotConfig.class);
    private final AWSIotQos topicQos = AWSIotQos.QOS0;

    private String topic;
    private String clientEndpoint;
    private String clientId;
    private String certificateFile;
    private String privateKeyFile;
    private String thingName;
    private String updateTopic;

    public String getUpdateTopic() {
        return updateTopic;
    }

    public void setUpdateTopic(String updateTopic) {
        this.updateTopic = updateTopic;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getClientEndpoint() {
        return clientEndpoint;
    }

    public void setClientEndpoint(String clientEndpoint) {
        this.clientEndpoint = clientEndpoint;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getCertificateFile() {
        return certificateFile;
    }

    public void setCertificateFile(String certificateFile) {
        this.certificateFile = certificateFile;
    }

    public String getPrivateKeyFile() {
        return privateKeyFile;
    }

    public void setPrivateKeyFile(String privateKeyFile) {
        this.privateKeyFile = privateKeyFile;
    }

    public String getThingName() {
        return thingName;
    }

    public void setThingName(String thingName) {
        this.thingName = thingName;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (topic == null) {
            logger.error("aws.topic is missing");
            throw new Exception("update topic is missing");
        }
        if (clientEndpoint == null) {
            logger.error("aws.clientendpoint is null");
            throw new Exception("client endpoint is missing");
        }
        if (clientId == null) {
            logger.error("aws.clientid is missing");
            throw new Exception("clientid is missing");
        }
        if (certificateFile == null) {
            logger.error("aws.certificateFile is missing");
            throw new Exception("certificate file is missing");
        } else {
            File f = new File(certificateFile);
            if (!f.exists()) {
                logger.error("couldn't find certificate file");
                throw new Exception("check certificate file location");
            }
        }

        if (privateKeyFile == null) {
            logger.error("aws.privateKeyFile is missing");
            throw new Exception("private key file is missing");
        } else {
            File f = new File(privateKeyFile);
            if (!f.exists()) {
                logger.error("couldn't find private key file");
                throw new Exception("check private key file location");
            }
        }
        if (thingName == null) {
            logger.error("aws.thingName is missing");
            throw new Exception("thing name is missing");
        }
        if (updateTopic == null) {
            logger.error("aws.updateTopic is missing");
            throw new Exception("update topic is missing");
        }
    }

    @Bean
    public KumanaDeviceMsg getKumanaDeviceMsg() {
        return new KumanaDeviceMsg("DEFAULT", AWSIotQos.QOS0, "DEFAULT PAYLOAD");
    }

    @Bean
    public KumanaDeviceTopic getKumanaDeviceTopic() {
        return new KumanaDeviceTopic(topic, topicQos);
    }


}
