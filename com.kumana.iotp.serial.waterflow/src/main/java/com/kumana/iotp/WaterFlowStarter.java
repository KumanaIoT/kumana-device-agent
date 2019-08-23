package com.kumana.iotp;

import com.kumana.iotp.Alerts.DeviceAlerts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Map;

@SpringBootApplication
@EnableScheduling
public class WaterFlowStarter implements CommandLineRunner {

    private final Logger logger = LoggerFactory.getLogger(WaterFlowStarter.class);

    @Autowired
    SerialSensorConnection connection;

    @Value("${command:#021\r}")
    private String getcommand;


    public static void main(String[] args) {
        SpringApplication.run(WaterFlowStarter.class, args);
    }


    @Override
    public void run(String... strings) throws Exception {

    }


    /**
     * triggering the serial connection
     * (writing an command to the connection if success callback will be executed on waterfloweventlistner)
     **/
    @Scheduled(fixedDelayString = "${fixedDelay.in.milliseconds}")
    public void triggerDevice() {
        connection.triggerDevice(getcommand);
    }


}