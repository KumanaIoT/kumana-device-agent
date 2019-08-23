package com.kumana.iotp;

import com.kumana.iotp.Alerts.DeviceAlerts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class WaterFlowEventsListner implements SerialPortEvents {

    private final Logger logger = LoggerFactory.getLogger(WaterFlowEventsListner.class);

    @Autowired
    SendMqMsg sendMqMsg;

    SensorReadingModel readingModel = new SensorReadingModel();

    @Value("${sensorkey}")
    String sensorKey;

    @Autowired
    Transformations transformations;

    @Autowired
    DeviceAlerts deviceAlerts;

    /**
     * serial connection will execute this when something
     * changes with the connection
     **/
    @Override
    public void onMessage(String values, ReadingType type) {
        if (type == ReadingType.ALERT) {
            AlertModel alertModel = deviceAlerts.getList().get(DeviceAlerts.CRITICAL);
            logger.info("alert received from serial connection");
            sendMqMsg.sendAlert(alertModel);
        } else {
            logger.info("reading received from serial connection");
            String val = transformations.getTransformedValue(values);
            readingModel.key = sensorKey;
            readingModel.addReading(sensorKey, val + "");
            logger.info("actual waterflow value :- "+values + " transformed value :- "+val);
            if (val!=null)sendMqMsg.sendReading(readingModel);
        }
    }

}
