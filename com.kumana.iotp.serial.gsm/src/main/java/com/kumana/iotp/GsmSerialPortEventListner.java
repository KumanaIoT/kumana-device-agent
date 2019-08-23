package com.kumana.iotp;

import com.kumana.iotp.Alerts.DeviceAlerts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class GsmSerialPortEventListner implements SerialPortEvents {

    private final Logger logger = LoggerFactory.getLogger(GsmSerialPortEventListner.class);

    @Autowired
    SendMqMsg sendMqMsg;

    @Autowired
    Transformations transformations;

    SensorReadingModel readingModel = new SensorReadingModel();

    @Value("${sensorkey}")
    String sensorKey;


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
            sendMqMsg.sendAlert(alertModel);
        } else {
            logger.info("reading received from serial connection");
//            if (values.contains("CSQ")) {
//                String sp[] = values.split(":");
//                String valuesar[] = sp[1].split(",");
//                int signal_level = Integer.parseInt(valuesar[0].substring(1));
//                signal_level = ((signal_level / 30)) * 100;
                String signal_level =  transformations.getTransformedValue(values);
                readingModel.addReading(sensorKey, signal_level + "");
                readingModel.key = sensorKey;
                sendMqMsg.sendReading(readingModel);
            }
        }

    }

