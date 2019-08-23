package com.kumana.iotp;

import com.kumana.iotp.listners.ControllerListner;
import com.kumana.iotp.publishers.Publish;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class DeviceDataReading implements ControllerListner {

    @Autowired
    List<Publish> publishers;

    @Autowired
    PumpUtils pumpUtils;

    private HashMap<String, String> actuatorMap = new HashMap<>();

    private final Logger logger = LoggerFactory.getLogger(DeviceDataReading.class);

    double luxLevel;
    double voltage;
    double waterFlowRate;

    @Override
    public void onMessageReceived(HashMap<String, String> readings) {
        logger.info("readings received for pump controller..");
        logger.info(readings + " - ");

        luxLevel = Double.parseDouble(readings.get(pumpUtils.getLuxKey()));
        voltage = Double.parseDouble(readings.get(pumpUtils.getVoltageKey()));
        waterFlowRate = Double.parseDouble(readings.get(pumpUtils.getWaterFlowKey()));

        //logic
        if (luxLevel > pumpUtils.getLuxThreshlod()) {
            if (voltage < pumpUtils.getVoltThreshold()) {
                if (waterFlowRate < pumpUtils.getWaterFlowThreshold()) {
                    //generate solar panel power error
                    logger.info("Solar panel voltage error generated");
                }
            } else {
                if (waterFlowRate < pumpUtils.getWaterFlowThreshold()) {
                    if (waterFlowRate <= 0) {
                        //pump off
                        actuatorMap.put(pumpUtils.getWaterPumpKey(), "off");
                        actuatorMap.put("sender", "edge");
                        actuatorMap.put("reason", "Water flow rate is zero");
                        publishers.forEach(publish -> publish.sendMsg(actuatorMap));
                        logger.info("Water Pump is OFF");

                        //send alert to cloud
                        logger.info("No water - Pump stopped alert sent");
                    }else{
                        //send alert to cloud
                        logger.info("Low water flow alert alert sent");
                     }
                }

            }

        }

    }


}
