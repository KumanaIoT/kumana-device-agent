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
public class GsmStarter implements CommandLineRunner {

    private final Logger logger = LoggerFactory.getLogger(GsmStarter.class);

    @Autowired
    SerialSensorConnection connection;


    @Autowired
    DeviceAlerts deviceAlerts;

    @Value("${command:#AT+CSQ\r}")
    private String getcommand;


    public static void main(String[] args) {
        SpringApplication.run(GsmStarter.class, args);
    }


    @Override
    public void run(String... strings) throws Exception {
        logger.info("Gsm sensor started.");
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
