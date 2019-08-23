package com.kumana.iotp;

import com.kumana.iotp.alertistner.AlertListner;
import com.kumana.iotp.alertistner.AlertType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WaterlvlAlertsListner implements AlertListner {

    @Autowired
    WaterLvlAlertConfig waterLvlAlertConfig;

    @Autowired
    SendMqMsg sendMqMsg;

    @Autowired
    WaterLvlConfig waterLvlConfig;

    @Override
    public void onAlertReceived(AlertType type) {
         sendMqMsg.sendAlert(waterLvlAlertConfig.getList().get(WaterLvlAlertConfig.CRITICAL));
    }


}
