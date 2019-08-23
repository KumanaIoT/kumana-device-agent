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

import javax.annotation.PreDestroy;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
@EnableScheduling
public class WaterLvlStarter implements CommandLineRunner {

    private final Logger logger = LoggerFactory.getLogger(WaterLvlStarter.class);

    ExecutorService service = Executors.newSingleThreadExecutor();

    @Autowired
    SerialSensorConnection connection;

    @Value("#{'${commands}'.split(',')}")
    private List<String> commandList;

    @Value("${getcommand}")
    private String getcommand;


    public static void main(String[] args) {
        SpringApplication.run(WaterLvlStarter.class, args);
    }


    @Override
    public void run(String... strings) throws Exception {

    }

    public  boolean connected = false;


    /**
     * triggering the serial connection
     * (writing an command to the connection if success callback will be executed on waterfloweventlistner)
     **/
    @Scheduled(fixedDelayString = "${fixedDelay.in.milliseconds}")
    public void triggerDevice() {
        if (!connected) {
            connected = connection.setupDevice();
            if (connected){
                commandList.forEach(s ->{
                    connection.triggerDevice(s);
                    try {
                        TimeUnit.SECONDS.sleep(2);
                    } catch (InterruptedException e) {
                        logger.error("water lvl schedule break");
                    }
                });
            }
        }else{
            connection.triggerDevice(getcommand);
        }
    }


    @PreDestroy
    public void exit(){
        service.shutdown();
    }




}