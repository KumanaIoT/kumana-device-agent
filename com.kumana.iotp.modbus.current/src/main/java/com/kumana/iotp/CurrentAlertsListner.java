package com.kumana.iotp;


import com.kumana.iotp.alertistner.AlertListner;
import com.kumana.iotp.alertistner.AlertType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CurrentAlertsListner implements AlertListner {

    @Autowired
    CurrentAlertConfig currentAlertConfig;

    @Autowired
    SendMqMsg sendMqMsg;

    @Autowired
    CurrentConfig currentConfig;


    @Override
    public void onAlertReceived(AlertType type) {
        sendMqMsg.sendAlert(currentAlertConfig.getList().get(currentAlertConfig.CRITICAL));
    }


}