package com.kumana.iotp;

import com.kumana.iotp.alertistner.AlertListner;
import com.kumana.iotp.alertistner.AlertType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VoltageAlertsListner implements AlertListner {

    @Autowired
    VoltageAlertConfig voltageAlertConfig;

    @Autowired
    SendMqMsg sendMqMsg;

    @Autowired
    VoltageConfig voltageConfig;


    @Override
    public void onAlertReceived(AlertType type) {
         sendMqMsg.sendAlert(voltageAlertConfig.getList().get(voltageAlertConfig.CRITICAL));
       }


}