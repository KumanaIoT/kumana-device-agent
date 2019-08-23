package com.kumana.iotp;


import com.kumana.iotp.readingslistner.HoldingRegisterListner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.FileNotFoundException;
import java.io.FileReader;

@Service
public class TemperatureReadingListner implements HoldingRegisterListner {

    private final Logger logger = LoggerFactory.getLogger(TemperatureReadingListner.class);


    @Autowired
    TemperatureConfig config;

    @Autowired
    Hex2Decimal hex2Decimal;


    SensorReadingModel sensorReadingModel = new SensorReadingModel();


    Invocable invocable;

    @PostConstruct
    void init() {
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
        try {
            engine.eval(new FileReader(config.getTransform()));
        } catch (ScriptException e) {
            logger.error("script exception check the script.");
        } catch (FileNotFoundException e) {
            logger.error("file not found exception.");
        }
        invocable = (Invocable) engine;
    }

    @Autowired
    SendMqMsg sendMqMsg;

    @Override
    public void onHoldingRegisterReceived(int[] registerValues) {
        if (registerValues.length < config.registerid) return;
        double val = registerValues[config.registerid];

        String result = "";
        try {
            result = (String) invocable.invokeFunction("transform", val + "");
        } catch (ScriptException e) {
            logger.error("script exception check the exception");
        } catch (NoSuchMethodException e) {
            logger.error("no such method in the script.");
        }

        logger.info("temperature value " + result + " actual value " + val);
        sensorReadingModel.addReading(config.sensorkey, result);
        sensorReadingModel.key = config.sensorkey;
        logger.info("publishing temperature readings to cloud connector..");
        sendMqMsg.sendReading(sensorReadingModel);
    }


}