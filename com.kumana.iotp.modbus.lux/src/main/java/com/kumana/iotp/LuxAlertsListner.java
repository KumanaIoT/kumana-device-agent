package com.kumana.iotp;

import com.kumana.iotp.alertistner.AlertListner;
import com.kumana.iotp.alertistner.AlertType;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

@Service
public class LuxAlertsListner implements AlertListner {

    @Autowired
    LuxAlertConfig luxAlertConfig;

    @Autowired
    SendMqMsg sendMqMsg;

    @Autowired
    LuxConfig luxConfig;

    @Override
    public void onAlertReceived(AlertType type) {
       sendMqMsg.sendAlert(luxAlertConfig.getList().get(luxAlertConfig.CRITICAL));
         }


}