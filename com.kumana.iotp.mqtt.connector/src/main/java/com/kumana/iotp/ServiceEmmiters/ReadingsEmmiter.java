package com.kumana.iotp.ServiceEmmiters;


import com.kumana.iotp.SensorReadingModel;
import com.kumana.iotp.listners.ControllerListner;
import com.kumana.iotp.listners.ReadingsListner;
import com.kumana.iotp.publishers.Publish;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Admin on 14/07/2017.
 */
@MessageEndpoint
public class ReadingsEmmiter {

    private final Logger logger = LoggerFactory.getLogger(ReadingsEmmiter.class);

    /**
     * this is the final stage of the cloud connector.
     * the sensorReadingmodels will now be distributed to all readinglistners
     * cloudImpl should have ReadingsListner implementations within the project.
     **/

    @Autowired
    ControllerListner controllerListners;

    @Autowired
    List<ReadingsListner> readingsListners;

    HashMap<String, String> sensorReadings = new HashMap();


    List<String> sensorKeys= new ArrayList(Arrays.asList("Luminance", "Water Flow", "Voltage Level"));

    @ServiceActivator(inputChannel = "mqttReadingChannel")
    public void process(SensorReadingModel sensorReading) {
        if (sensorReading != null) {
            logger.info("Readings distributed to reading listners.");
            readingsListners.forEach(readingsListner -> readingsListner.onMessageReceived(sensorReading));
        }
        /**
         * lux , waterflow , voltage
         * sensor reading should contain one of the sensor values.
         * and the sensor readings should contain all three values completely.
         **/
        if (sensorKeys!=null) {
            if (sensorKeys.contains(sensorReading.key)) {
                //received reading contains a requested value.
                logger.info("reading contains " + sensorReading.key);
                sensorReadings.put(sensorReading.key, sensorReading.getReading(sensorReading.key));
                logger.debug(sensorReadings + " - currently available sensor values...");
                //sensor readings map is completely full of requested values.
                if (sensorReadings.keySet().containsAll(sensorKeys)) {
                    logger.info("sensor reading is completely full with requested values.");
                    controllerListners.onMessageReceived(sensorReadings);
                    sensorReadings.clear();
                }
            }
        }

    }


}