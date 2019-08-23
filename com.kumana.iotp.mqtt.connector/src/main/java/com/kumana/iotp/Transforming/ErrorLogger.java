package com.kumana.iotp.Transforming;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandlingException;

@MessageEndpoint
public class ErrorLogger {

    private final Logger logger = LoggerFactory.getLogger(ErrorLogger.class);

    @ServiceActivator(inputChannel = "errorChannel")
    public void onExceptionThrown(Message<MessageHandlingException> exceptionMessage) {
        logger.error(exceptionMessage.getPayload() + " -- " + exceptionMessage);
    }

}
