package com.kumana.iotp;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
@ConfigurationProperties("modbus")
@Component
public class ConnectionConfig implements InitializingBean {

    private final Logger logger = LoggerFactory.getLogger(ConnectionConfig.class);

    String comport;
    int databits;
    int stopbits;
    int slaveid;
    int offset;
    int quantity;

    public String getComport() {
        return comport;
    }

    public void setComport(String comport) {
        this.comport = comport;
    }

    public int getDatabits() {
        return databits;
    }

    public void setDatabits(int databits) {
        this.databits = databits;
    }

    public int getStopbits() {
        return stopbits;
    }

    public void setStopbits(int stopbits) {
        this.stopbits = stopbits;
    }

    public int getSlaveid() {
        return slaveid;
    }

    public void setSlaveid(int slaveid) {
        this.slaveid = slaveid;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (comport == null) {
            logger.error("modbus.comport is missing");
            throw new Exception("modbus comport is missing");
        }
        if (databits == 0) {
            logger.error("modbus.databits is missing");
            throw new Exception("modbus databits is missing");
        }
        if (stopbits == 0) {
            logger.error("modbus.stopbits is missing");
            throw new Exception("modbus stopbits is missing");
        }
    }
}
