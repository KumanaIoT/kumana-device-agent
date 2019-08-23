package com.kumana.iotp.Transforming;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kumana.iotp.SensorReadingModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.Transformer;
import org.springframework.messaging.Message;

import java.io.IOException;

/**
 * Created by Admin on 14/07/2017.
 */
@MessageEndpoint
public class ReadingTransformer {

    ObjectMapper mapper;

    private final Logger logger = LoggerFactory.getLogger(ReadingTransformer.class);


    /**
     * mqtt messages with READING topics are received here.
     * they will be converted to SensorReadingModel class and redirect to mqttReadingChannel
     * which means to ReadingsEmmiter.process();
     **/
    @Transformer(inputChannel = "readingTransformerChannel", outputChannel = "mqttReadingChannel")
    public SensorReadingModel transformReading(Message<String> message) {
        SensorReadingModel sensorReading = null;

        if (mapper == null) {
            mapper = new ObjectMapper();
        }

        try {
            logger.info("trying to get and sensormodel from the mqtt payload");
            sensorReading = mapper.readValue(message.getPayload(), SensorReadingModel.class);

        } catch (IOException e) {
            e.printStackTrace();
            return sensorReading = new SensorReadingModel();
        }

        return sensorReading;
    }

}