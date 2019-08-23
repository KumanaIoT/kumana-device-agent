package com.kumana.iotp;

import com.kumana.iotp.alertistner.AlertListner;
import com.kumana.iotp.alertistner.AlertType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BatteryAlertsListner implements AlertListner {

    @Autowired
    BatteryAlertConfig batteryAlertConfig;

    @Autowired
    SendMqMsg sendMqMsg;

    @Autowired
    BatteryConfig batteryConfig;

    @Override
    public void onAlertReceived(AlertType type) {
        sendMqMsg.sendAlert(batteryAlertConfig.getList().get(batteryAlertConfig.CRITICAL));
        }


}
