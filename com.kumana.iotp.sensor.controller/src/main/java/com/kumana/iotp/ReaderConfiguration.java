package com.kumana.iotp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

@Configuration
@IntegrationComponentScan
public class ReaderConfiguration {

    @Autowired
    ClientConfig config;

    @Value("${sensorkey}")
    String sensorKey;

    private final static String ACTUATOR = "actuator";

    //mqtt client factory - setting up the connection.
    @Bean
    public MqttPahoClientFactory mqttClientFactory() {
        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();

        factory.setServerURIs(config.getUrl());
        factory.setUserName(config.getUser());
        factory.setPassword(config.getPass());
        return factory;
    }


    //setting up the handler..
    //this will get executed once input came from . mqttoutboundchannel..
    @Bean
    @ServiceActivator(inputChannel = "mqttOutboundChannel", autoStartup = "true")
    public MessageHandler outbound() {
        MqttPahoMessageHandler handler = new MqttPahoMessageHandler(config.getClientid(), mqttClientFactory());
        handler.setAsync(true);
        handler.setDefaultTopic("ALERT");
        return handler;
    }

    @Bean
    public MessageProducer inbound() {
        MqttPahoMessageDrivenChannelAdapter adapter =
                new MqttPahoMessageDrivenChannelAdapter(config.getClientid() + "sub", mqttClientFactory(), ACTUATOR);
        adapter.setCompletionTimeout(5000);
        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setQos(1);
        adapter.setOutputChannel(mqttInboundChannel());
        return adapter;
    }


    @Bean
    public MessageChannel mqttRouterChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageChannel mqttInboundChannel() {
        return new PublishSubscribeChannel();
    }

}
