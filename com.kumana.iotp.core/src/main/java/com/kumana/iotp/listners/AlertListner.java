package com.kumana.iotp.listners;

import com.kumana.iotp.AlertModel;
import org.springframework.stereotype.Service;

@Service
public interface AlertListner {

    public void onMessageReceived(AlertModel alertModel);

}
