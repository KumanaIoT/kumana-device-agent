package com.kumana.iotp;

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
public class Transformations {

    private final Logger logger = LoggerFactory.getLogger(Transformations.class);


    Invocable invocable;

    @Autowired
    PortConfig config;

    String result = null;

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

    public String getTransformedValue(String val){
        try {
            result = (String) invocable.invokeFunction("transform", val + "");
        } catch (ScriptException e) {
            logger.error("script exception check the exception");
        } catch (NoSuchMethodException e) {
            logger.error("no such method in the script.");
        }
        return result;
    }

}
