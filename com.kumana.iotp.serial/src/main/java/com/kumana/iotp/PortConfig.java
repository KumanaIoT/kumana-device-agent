package com.kumana.iotp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
@ConfigurationProperties("serial")
@Component
public class PortConfig implements InitializingBean {

    private final Logger logger = LoggerFactory.getLogger(PortConfig.class);

    int baudrate;
    int databits;
    int stopbits;
    int parity;
    String transform;
    String comport;
    String identify;
    String uniquekey;
    String portregex;


    public String getComport() {
        return comport;
    }

    public void setComport(String comport) {
        this.comport = comport;
    }

    public void setBaudrate(int baudrate) {
        this.baudrate = baudrate;
    }

    public void setDatabits(int databits) {
        this.databits = databits;
    }

    public void setStopbits(int stopbits) {
        this.stopbits = stopbits;
    }

    public void setParity(int parity) {
        this.parity = parity;
    }

    public void setTransform(String transform) {
        this.transform = transform;
    }

    public String getIdentify() { return identify; }

    public void setIdentify(String identify) {
        this.identify = identify;
    }

    public String getPortregex() { return portregex; }

    public void setPortregex(String portregex) { this.portregex = portregex; }

    public String getUniquekey() {
        return uniquekey;
    }

    public void setUniquekey(String uniquekey) {
        this.uniquekey = uniquekey;
    }

    public int getBaudrate() {
        return baudrate;
    }

    public int getDatabits() {
        return databits;
    }

    public int getStopbits() {
        return stopbits;
    }

    public int getParity() {
        return parity;
    }

    public String getTransform() { return transform; }



    @Override
    public void afterPropertiesSet() throws Exception {
        if (baudrate == 0) {
            logger.error("serial.baudrate is missing");
            throw new Exception("serial baudrate is missing");
        }
        if (databits == 0) {
            logger.error("serial.databits is missing");
            throw new Exception("databits is missing");
        }
        if (stopbits == 0) {
            logger.error("serial.stopbits is missing");
            throw new Exception("serial stop bits is missing");
        }
        if (transform==null){
            logger.error("transform file location is empty");
            throw new Exception("transformation file location is missing.");
        }else if (transform!=null){
            if (transform.isEmpty()){
                logger.error("transform file location is empty");
                throw new Exception("transformation file location is missing.");
            }
        }
    }


}
