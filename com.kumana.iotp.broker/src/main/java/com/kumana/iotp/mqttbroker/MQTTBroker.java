package com.kumana.iotp.mqttbroker;

import com.kumana.iotp.mqttbroker.interceptor.MQTTBrokerInterceptor;
import io.moquette.BrokerConstants;
import io.moquette.interception.InterceptHandler;
import io.moquette.server.Server;
import io.moquette.server.config.IConfig;
import io.moquette.server.config.MemoryConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class MQTTBroker {

    private static final Logger logger = LoggerFactory.getLogger(MQTTBroker.class);
    private int MQTT_BROKER_PORT = 0;
    private String MQTT_BROKER_HOST = null;

    private MQTTBrokerInterceptor mqttBrokerInterceptor;

    public MQTTBroker(final int mqttBrokerPort, final String mqttbrokerhost) {
        if(mqttBrokerPort != 0 && mqttbrokerhost != null && !mqttbrokerhost.isEmpty()){
            this.MQTT_BROKER_PORT = mqttBrokerPort;
            this.MQTT_BROKER_HOST = mqttbrokerhost;
        } else {
            throw  new IllegalArgumentException("Invalid properties found for MQTT HOST and (or) MQTT PORT");
        }
    }

    public void startBroker() throws IOException {
        this.mqttBrokerInterceptor = new MQTTBrokerInterceptor();
        final Properties props = new Properties();
        props.setProperty(BrokerConstants.PORT_PROPERTY_NAME, Integer.toString(MQTT_BROKER_PORT));
        props.setProperty(BrokerConstants.HOST_PROPERTY_NAME, MQTT_BROKER_HOST);
        props.setProperty(BrokerConstants.ALLOW_ANONYMOUS_PROPERTY_NAME, "true");

        final IConfig configs = new MemoryConfig(props);

        final Server moquetteBroker = new Server();
        final List<? extends InterceptHandler> userHandlers = Arrays.asList(mqttBrokerInterceptor);

        logger.info("Attempting to start MQTT Broker on HOST : {} and PORT : {}", MQTT_BROKER_HOST, MQTT_BROKER_PORT);
        System.out.println("Attempting to start MQTT Broker on HOST : " + MQTT_BROKER_HOST + " and PORT : " + MQTT_BROKER_PORT);
        moquetteBroker.startServer(configs, userHandlers);
        logger.info("MQTT Broker on HOST : {} and PORT : {} has been started...", MQTT_BROKER_HOST, MQTT_BROKER_PORT);
        System.out.println("MQTT Broker on Host : " + MQTT_BROKER_HOST + " and PORT : " + MQTT_BROKER_PORT + " has been started...");

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                logger.info("Attempting to shut-down MQTT Broker on HOST : {} and PORT : {}", MQTT_BROKER_HOST, MQTT_BROKER_PORT);
                System.out.println("Attempting to shut-down MQTT Broker on HOST : " + MQTT_BROKER_HOST + " and PORT : " + MQTT_BROKER_PORT);
                moquetteBroker.stopServer();
                logger.info("MQTT Broker has stopped on HOST : {} and PORT : {}", MQTT_BROKER_HOST, MQTT_BROKER_PORT);
                System.out.println("MQTT Broker has stopped on HOST : " + MQTT_BROKER_HOST + " and PORT : " + MQTT_BROKER_PORT);
            }
        });
    }


}
