package com.kumana.iotp.listner;


import com.kumana.iotp.AlertModel;
import com.kumana.iotp.Alerts.DeviceAlerts;
import com.kumana.iotp.SendMqMsg;
import com.kumana.iotp.WaterPump;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;

@Service
public class WaterPumpMsgListner implements Listner {

    private final Logger logger = LoggerFactory.getLogger(WaterPumpMsgListner.class);

    private HashMap<String,String> senderMap = new HashMap<>();

    @Value("${sensorkey}")
    public String sensorKey;

    @Autowired
    WaterPump waterPump;

    @Autowired
    DeviceAlerts deviceAlerts;

    @Autowired
    SendMqMsg sendMqMsg;

    AlertModel alert;

    @PostConstruct
    public void init(){

        alert = deviceAlerts.getList().get(DeviceAlerts.MINOR);

    }


    @Override
    public void onMessageReceived(HashMap<String, String> actuatorValues) {

        logger.info("received command --- " + actuatorValues.get(sensorKey));

        String status = "off";


        if (actuatorValues.get(sensorKey) == null) return;

        switch (actuatorValues.get(sensorKey)) {
            case SensorCommand.switchOff:
                waterPump.trigger(waterPump.switchOffCommand);
                status = "off";
                break;
            case SensorCommand.switchOn:
                waterPump.trigger(waterPump.switchOnCommand);
                status = "on";
                break;
            default:
                logger.info(actuatorValues.get(sensorKey) + "-- command not identified");
        }

        if(actuatorValues.containsKey("sender") && actuatorValues.containsKey("reason")){

            //edge
            alert.setSecondaryCode(status+"-command-received");
            senderMap.put("sender","edge");
            senderMap.put("reason",actuatorValues.get("reason"));
            alert.setVariables(senderMap);
            sendMqMsg.sendAlert(alert);

        }else{

            alert.setSecondaryCode(status+"-command-received");
            senderMap.put("sender","web");
            senderMap.put("reason", "performed by web client");
            alert.setVariables(senderMap);
            sendMqMsg.sendAlert(alert);

        }

    }


}
