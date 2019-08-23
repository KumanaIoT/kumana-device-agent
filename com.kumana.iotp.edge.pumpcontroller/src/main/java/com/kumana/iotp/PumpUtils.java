package com.kumana.iotp;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("pumpcontroller")
public class PumpUtils {

    String waterPumpKey;
    String waterFlowKey;
    String luxKey;
    String voltageKey;

    int luxThreshlod;
    int waterFlowThreshold;
    int voltThreshold;


    public String getWaterFlowKey() {
        return waterFlowKey;
    }

    public void setWaterFlowKey(String waterFlowKey) {
        this.waterFlowKey = waterFlowKey;
    }

    public String getLuxKey() {
        return luxKey;
    }

    public void setLuxKey(String luxKey) {
        this.luxKey = luxKey;
    }

    public String getVoltageKey() {
        return voltageKey;
    }

    public void setVoltageKey(String voltageKey) {
        this.voltageKey = voltageKey;
    }

    public int getLuxThreshlod() {
        return luxThreshlod;
    }

    public void setLuxThreshlod(int luxThreshlod) {
        this.luxThreshlod = luxThreshlod;
    }

    public int getWaterFlowThreshold() {
        return waterFlowThreshold;
    }

    public void setWaterFlowThreshold(int waterFlowThreshold) {
        this.waterFlowThreshold = waterFlowThreshold;
    }

    public int getVoltThreshold() {
        return voltThreshold;
    }

    public void setVoltThreshold(int voltThreshold) {
        this.voltThreshold = voltThreshold;
    }

    public String getWaterPumpKey() {
        return waterPumpKey;
    }

    public void setWaterPumpKey(String waterPumpKey) {
        this.waterPumpKey = waterPumpKey;
    }
}
