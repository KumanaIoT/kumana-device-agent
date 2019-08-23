package com.kumana.iotp.alertistner;

import org.springframework.stereotype.Service;

@Service
public interface AlertListner {
    public void onAlertReceived(AlertType type);
}
