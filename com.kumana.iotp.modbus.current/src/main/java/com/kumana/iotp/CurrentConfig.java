package com.kumana.iotp;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;


@Configuration
@ConfigurationProperties("current")
public class CurrentConfig {

    int registerid;
    String sensorkey;
    String transform;

    public String getTransform() {
        return transform;
    }

    public void setTransform(String transform) {
        this.transform = transform;
    }

    public int getRegisterid() {
        return registerid;
    }

    public void setRegisterid(int registerid) {
        this.registerid = registerid;
    }

    public String getSensorkey() {
        return sensorkey;
    }

    public void setSensorkey(String sensorkey) {
        this.sensorkey = sensorkey;
    }


}