package com.kumana.iotp;

import com.kumana.iotp.alertistner.AlertListner;
import com.kumana.iotp.alertistner.AlertType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HumidityAlertsListner implements AlertListner {

    @Autowired
    HumidityAlertConfig humidityAlertConfig;

    @Autowired
    SendMqMsg sendMqMsg;

    @Autowired
    HumidityConfig humidityConfig;


    @Override
    public void onAlertReceived(AlertType type) {
        sendMqMsg.sendAlert(humidityAlertConfig.getList().get(HumidityAlertConfig.CRITICAL));
    }


}