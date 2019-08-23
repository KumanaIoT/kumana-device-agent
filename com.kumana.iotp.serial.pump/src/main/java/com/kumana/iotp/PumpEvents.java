package com.kumana.iotp;

import com.kumana.iotp.Alerts.DeviceAlerts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PumpEvents implements SerialPortEvents {

    private final Logger logger = LoggerFactory.getLogger(PumpEvents.class);

    @Autowired
    DeviceAlerts deviceAlerts;

    @Autowired
    SendMqMsg sendMqMsg;

    @Value("${sensorkey}")
    public String sensorKey;

    @Value("${rpmKey}")
    public String rpmKey;


    @Autowired
    Transformations transformations;

    SensorReadingModel readingModel = new SensorReadingModel();

    @Override
    public void onMessage(String values, ReadingType type) {
        if (type == ReadingType.ALERT) {
            logger.warn("alert reading received from water pump");
            sendMqMsg.sendAlert(deviceAlerts.getList().get(DeviceAlerts.MAJOR));
            logger.info(values+" alert values");
        } else if (type == ReadingType.READING) {
            logger.warn("sensor reading received from water pump");
            logger.info("reading values "+values);

            String val = transformations.getTransformedValue(values);
            String keyValue = null;

            if (val!=null){
                String ar[] = val.split("-");
                if (ar.length==2){
                    if (ar[0].equals(sensorKey)){
                        /**waterpump status**/
                        keyValue = sensorKey;
                        logger.info("waterpump status received.."+ar[1]);
                    }else if (ar[0].equals(rpmKey)){
                        /**rpm status**/
                        keyValue = rpmKey;
                        logger.info("rpm received.."+ar[1]);
                    }else{
                        logger.error("reading doesn't contain any matching sensor keys.");
                    }

                    if (keyValue!=null) {
                        readingModel.key = keyValue;
                        readingModel.getReading().clear();
                        readingModel.addReading(keyValue, ar[1]);
                        sendMqMsg.sendReading(readingModel);
                    }
                }else{
                    logger.error("transformed message is incomplete.");
                }
            }

            logger.info("pump actual value :- "+ values +" transformed value :- "+val);
        }
    }
}