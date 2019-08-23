package com.kumana.iotp;

/**
 * Created by Admin on 13/07/2017.
 */

import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.messaging.Message;

@MessagingGateway(defaultRequestChannel = "mqttOutboundChannel")
public interface MqOutGateway {
    void send(Message<String> message);
}