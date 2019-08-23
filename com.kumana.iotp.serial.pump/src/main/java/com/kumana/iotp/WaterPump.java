package com.kumana.iotp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
@Service
@EnableScheduling
public class WaterPump implements CommandLineRunner {

    private final Logger logger = LoggerFactory.getLogger(PumpEvents.class);
    ExecutorService executorService = Executors.newFixedThreadPool(1);

    @Value("${oncommand:on\n\r}")
    public String switchOnCommand;

    @Value("${offcommand:off\n\r}")
    public String switchOffCommand;

    @Value("${status:state\r}")
    public String statusCommand;


    @Value("${rpm:rpm\r}")
    public String rpmCommand;

    @Autowired
    SerialSensorConnection connection;

    public void trigger(String command) {
        logger.info("water pump status check");
        connection.triggerDevice(command);
    }

    public static void main(String[] args) {
        SpringApplication.run(WaterPump.class, args);
    }


    @Override
    public void run(String... strings) throws Exception {
        logger.info("Water Pump actuator started..");
        logger.info(switchOnCommand+"-");
    }


    @Scheduled(fixedDelayString = "${fixedDelay.in.milliseconds}")
    public void triggerDevice() {
        executorService.submit(() -> {
                trigger(statusCommand); //status check.
            try {
                TimeUnit.SECONDS.sleep(10);
            trigger(rpmCommand); //rpm check.
            } catch (InterruptedException e) {
                logger.error("interupted process when writing commands to pump");
            }
        });
    }


    @PreDestroy
    public void destroy(){
            executorService.shutdown();
    }


}
