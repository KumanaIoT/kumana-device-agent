package com.kumana.iotp.mqttbroker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;

import java.io.IOException;

@SpringBootApplication
public class Application {

    private static final String MQTT_BROKER_PORT = "mqtt.broker.port";
    private static final String MQTT_BROKER_HOST = "mqtt.broker.host";
    @Autowired
    private Environment env;

    private static Logger logger = LoggerFactory.getLogger(Application.class);

    private static final String BROKER_BANNER_TEXT="" +
            " __  __  ___ _____ _____   ____            _             \n" +
            "|  \\/  |/ _ \\_   _|_   _| | __ ) _ __ ___ | | _____ _ __ \n" +
            "| |\\/| | | | || |   | |   |  _ \\| '__/ _ \\| |/ / _ \\ '__|\n" +
            "| |  | | |_| || |   | |   | |_) | | | (_) |   <  __/ |   \n" +
            "|_|  |_|\\__\\_\\|_|   |_|   |____/|_|  \\___/|_|\\_\\___|_|   \n" +
            "                                                         \n";

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public MQTTBroker mqttBroker(){
        final MQTTBroker mqttBroker = new MQTTBroker(Integer.parseInt(env.getRequiredProperty(MQTT_BROKER_PORT)), env.getRequiredProperty(MQTT_BROKER_HOST));
        try {
            mqttBroker.startBroker();
            System.out.printf(BROKER_BANNER_TEXT);
            logger.info("KUMANA_MQTT_BROKER_STARTED");
        } catch (IOException e) {
            logger.error("KUMANA_MQTT_BROKER START-UP FAILED");
            e.printStackTrace();
        }
        return mqttBroker;
    }
}
