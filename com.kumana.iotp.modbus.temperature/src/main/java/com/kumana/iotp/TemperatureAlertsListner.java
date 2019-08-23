package com.kumana.iotp;

import com.kumana.iotp.alertistner.AlertListner;
import com.kumana.iotp.alertistner.AlertType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TemperatureAlertsListner implements AlertListner {

    @Autowired
    TemperatureAlertConfig temperatureAlertConfig;

    @Autowired
    SendMqMsg sendMqMsg;

    @Autowired
    TemperatureConfig temperatureConfig;


    @Override
    public void onAlertReceived(AlertType type) {
         sendMqMsg.sendAlert(temperatureAlertConfig.getList().get(temperatureAlertConfig.CRITICAL));
         }


}