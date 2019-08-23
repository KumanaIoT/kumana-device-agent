package com.kumana.iotp.Alerts;

import com.kumana.iotp.AlertModel;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@ConfigurationProperties("Alerts")
@Configuration()
public class DeviceAlerts {

    private final Map<String, AlertModel> list = new HashMap<>();

    public Map<String, AlertModel> getList() {
        return list;
    }

    public static final String MINOR = "minor";
    public static final String MAJOR = "major";
    public static final String CRITICAL = "critical";


}