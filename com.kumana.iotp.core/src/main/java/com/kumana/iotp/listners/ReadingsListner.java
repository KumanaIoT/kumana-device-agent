package com.kumana.iotp.listners;

import com.kumana.iotp.SensorReadingModel;
import org.springframework.stereotype.Service;

@Service
public interface ReadingsListner {
    public void onMessageReceived(SensorReadingModel sensorReadingModel);
}
