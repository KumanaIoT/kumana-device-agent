package com.kumana.iotp;


import jssc.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.*;

@Service
public class SerialSensorConnection extends DeviceConnection {

    private final Logger logger = LoggerFactory.getLogger(SerialSensorConnection.class);

    @Autowired
    PortConfig config;

    @Autowired
    List<SerialPortEvents> eventListners;

    @Autowired
    PortMatcher portMatcher;

    public SerialPort serialPort;
    int baudrate;
    int databits;
    int stopbits;
    int parity;

    @PostConstruct
    public void init() {
        databits = config.getDatabits();
        baudrate = config.getBaudrate();
        stopbits = config.getStopbits();
        parity = config.getParity();
    }


    /**
     * setting up a serial connection..
     **/
    @Override
    public boolean setupDevice() {
        try {
            String port = portMatcher.getPort();
            connectionIsReady = false;
            if (port==null) return connectionIsReady;
            logger.info("connecting to port : "+port);
            serialPort = new SerialPort(port);
            config.setComport(port);
            serialPort.openPort();
            serialPort.setParams(baudrate, databits, stopbits, parity);
            PortReader portReader = new PortReader(serialPort);
            serialPort.addEventListener(portReader, SerialPort.MASK_RXCHAR);
            connectionIsReady = true;
            logger.info("serial connection is ready");
        } catch (SerialPortException e) {
            logger.warn("serial connection failed");
            connectionIsReady = false;
        }
        return connectionIsReady;
    }


    private boolean firstTry = true;



    /**
     * triggering the serial connection with a command once we have data.
     **/
    @Override
    public void triggerDevice(String command) {
        logger.info("serial command received");

        /**checking connection validity..**/
        if (connectionIsReady) {

            /**getting available port names in the device**/
            String[] list = SerialPortList.getPortNames();
            boolean exist = Arrays.stream(list).anyMatch(str -> str.trim().equals(config.getComport()));
            if (exist) {
                logger.info("serial port connection exists");
            } else {
                try {
                    /**this port is not listed among the available ports..
                     * need to close the port and release the memmory**/
                    serialPort.closePort();
                    logger.warn("closing the port to reconnect");
                } catch (SerialPortException e) {
                    logger.warn("closing the port failed");
                }
                logger.warn("serial connection is not ready");
                connectionIsReady = false;
            }
        }

        /**sending out data..**/
        if (connectionIsReady) {
            logger.info("serial connection is ready");
            try {
                serialPort.writeBytes(command.getBytes());
            } catch (SerialPortException e) {
                logger.warn("error triggering the serial sensor");
                if (!serialPort.isOpened()) {
                    setupDevice();
                }
            }
        } else {
            logger.warn("serial sensor is not connected.");
            setupDevice();
            if (firstTry) {
                firstTry = false;
                return;
            }
            eventListners.forEach(serialPortEvents -> serialPortEvents.onMessage("alert", ReadingType.ALERT));
        }

    }


    /**
     * the call back will get executed once we get data from serial sensor
     **/
    private class PortReader implements SerialPortEventListener {

        SerialPort serialPort;

        public PortReader(SerialPort serialPort) {
            this.serialPort = serialPort;
        }

        StringBuilder message = new StringBuilder();
        StringBuilder logBuffer = new StringBuilder();

        @Override
        public void serialEvent(SerialPortEvent event) {

            if (event.isRXCHAR() && event.getEventValue() > 0) {
                try {
                    byte buffer[] = serialPort.readBytes();

                    for (byte b: buffer) {
                        if ( (b == '\r' || b == '\n') && message.length() > 0) {
                            String toProcess = message.toString();
                            logger.info("serial reading value - " + toProcess);
                            eventListners.forEach(serialPortEvents -> serialPortEvents.onMessage(toProcess, ReadingType.READING));
                            message.setLength(0);
                        }
                        else {
                            message.append((char)b);
                        }
                    }
                }
                catch (SerialPortException ex) {
                    logger.error("serial port event read exception");
                }
            }
        }
    }


}
