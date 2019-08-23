package com.kumana.iotp.lite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import com.kumana.iotp.lite.actuatorlistner.ConcertLiteHubConsumer;

@Configuration
@ConfigurationProperties("lite")
@Component
public class KumanaLiteIotConfig implements InitializingBean {


    private final Logger logger = LoggerFactory.getLogger(KumanaLiteIotConfig.class);
    
	private String mqttServerUri;
	private String mqttReportingClientName;
	private String mqttActuatorClientName;
    private String reportingTopic;
    private String actuatorTopic;
    
    
    public String getMqttServerUri() {
		return mqttServerUri;
	}

	public void setMqttServerUri(String mqttServerUri) {
		this.mqttServerUri = mqttServerUri;
	}
	public String getMqttReportingClientName() {
		return mqttReportingClientName;
	}

	public void setMqttReportingClientName(String mqttReportingClientName) {
		this.mqttReportingClientName = mqttReportingClientName;
	}

	public String getMqttActuatorClientName() {
		return mqttActuatorClientName;
	}

	public void setMqttActuatorClientName(String mqttActuatorClientName) {
		this.mqttActuatorClientName = mqttActuatorClientName;
	}

	public String getReportingTopic() {
		return reportingTopic;
	}

	public void setReportingTopic(String reportingTopic) {
		this.reportingTopic = reportingTopic;
	}

	public String getActuatorTopic() {
		return actuatorTopic;
	}

	public void setActuatorTopic(String actuatorTopic) {
		this.actuatorTopic = actuatorTopic;
	}

	public Logger getLogger() {
		return logger;
	}


	@Override
	public void afterPropertiesSet() throws Exception {
		if (mqttServerUri == null) {
            logger.error("lite.mqttServerUri is missing");
            throw new Exception("mqtt broker URI is missing");
        }
		if (mqttReportingClientName == null) {
            logger.error("lite.mqttReportingClientName is missing");
            throw new Exception("MQTT Reporting Client name is missing");
        }
		if (mqttActuatorClientName == null) {
            logger.error("lite.mqttActuatorClientName is missing");
            throw new Exception("MQTT Actuator Client name is missing");
        }
		if (reportingTopic == null) {
            logger.error("lite.reportingTopic is missing");
            throw new Exception("Reporting topic is missing");
        }
		if (actuatorTopic == null) {
            logger.error("lite.actuatorTopic is missing");
            throw new Exception("Actuator topic is missing");
        }
		
	}
	
    @Bean
    public ConcertLiteHubConsumer concertIOTHubConsumer(){
        final ConcertLiteHubConsumer concertIOTHubConsumer = new ConcertLiteHubConsumer(getMqttActuatorClientName(), getActuatorTopic(),getMqttServerUri());     
        return concertIOTHubConsumer;
    }


}
