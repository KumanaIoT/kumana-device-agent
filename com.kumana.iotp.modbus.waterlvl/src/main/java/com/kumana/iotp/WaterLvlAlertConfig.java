package com.kumana.iotp;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.HashMap;
import java.util.Map;


@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "waterlvl")
public class WaterLvlAlertConfig {

    private final Map<String, AlertModel> list = new HashMap<>();

    public Map<String, AlertModel> getList() {
        return list;
    }

    public static final String MINOR = "minor";
    public static final String MAJOR = "major";
    public static final String CRITICAL = "critical";

}