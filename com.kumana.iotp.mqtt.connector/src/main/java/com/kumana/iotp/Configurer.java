package com.kumana.iotp;

import com.kumana.iotp.publishers.Publish;
import com.kumana.iotp.sensorContol.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
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
public class Configurer {


    @Autowired
    ConnectorConfig config;


    @Bean
    public MqttPahoClientFactory mqttClientFactory() {
        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
        factory.setServerURIs(config.getUrl());
        factory.setUserName(config.getUser());
        factory.setPassword(config.getPass());
        return factory;
    }


    @Bean
    @ServiceActivator(inputChannel = "mqttOutboundChannel", autoStartup = "true")
    public MessageHandler outbound() {
        MqttPahoMessageHandler handler = new MqttPahoMessageHandler(config.getClientid() + "pub", mqttClientFactory());
        handler.setAsync(true);
        handler.setDefaultTopic("all");
        return handler;
    }

    @Bean
    public MessageProducer inbound() {
        MqttPahoMessageDrivenChannelAdapter adapter =
                new MqttPahoMessageDrivenChannelAdapter(config.getClientid(), mqttClientFactory(), "ALERT", "READING");
        adapter.setCompletionTimeout(5000);
        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setQos(1);
        adapter.setOutputChannel(mqttRouterChannel());
        return adapter;
    }


    @Bean
    public MessageChannel mqttRouterChannel() {
        return new DirectChannel();
    }

    //service channels..
    //these are responsible for uploading data to cloud.
    @Bean
    public MessageChannel mqttAlertChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageChannel mqttReadingChannel() {
        return new DirectChannel();
    }


    //transformer channels.
    //these are responsible for transforming message to a pojo
    @Bean
    public MessageChannel alertTransformerChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageChannel readingTransformerChannel() {
        return new DirectChannel();
    }

}
