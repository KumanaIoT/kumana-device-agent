package com.kumana.iotp;

import jssc.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Pattern;

@Service
public class PortMatcher {

    private final Logger logger = LoggerFactory.getLogger(PortMatcher.class);

    boolean inProcess = false;

    @Autowired
    PortConfig config;


    public String getPort(){


        for (String comport: SerialPortList.getPortNames(Pattern.compile(config.getPortregex()))) {

            SerialPort serialPort = new SerialPort(comport);
            AtomicReference<String> returnval = new AtomicReference<>();
            try {
                serialPort.openPort();

                serialPort.setParams(config.baudrate, config.databits, config.stopbits, config.parity);
                StringBuilder message = new StringBuilder();
                /**
                 * adding event listner to the port data will be received from here
                 * **/
                serialPort.addEventListener(serialPortEvent -> {
                    if (serialPortEvent.isRXCHAR() && serialPortEvent.getEventValue() > 0) {
                        try {
                            byte buffer[] = serialPort.readBytes();
                            for (byte b : buffer) {
                                if ((b == '\r' || b == '\n') && message.length() > 0) {
                                    String toProcess = message.toString();
                                    returnval.set(toProcess);
                                    message.setLength(0);
                                } else {
                                    message.append((char) b);
                                }
                            }
                        } catch (SerialPortException ex) {
                            logger.error("serial port event read exception");
                        }
                    }
                }, SerialPort.MASK_RXCHAR);

                /**serial device needs time to start.**/
                TimeUnit.SECONDS.sleep(10);

                /**writing serial command to the serial port.**/
                serialPort.writeBytes(config.getIdentify().getBytes());

                /**document says it takes about 200ms to receive data from serial connection**/
                TimeUnit.SECONDS.sleep(3);


                String uniqueval = returnval.get();
                logger.info(" return val " + uniqueval + "---");

                /**if the output is the unique key. this is the right comport.**/
                if (uniqueval == null) {
                    logger.info("nothing retured from the serial connection closing the port.");
                    closeComport(serialPort);
                }

                closeComport(serialPort);

                if (uniqueval.trim().equals(config.uniquekey)) {
                    logger.info("port match found....");
                    return comport;
                } else if (uniqueval.contains(config.uniquekey)) {
                    logger.info("port match found ");
                    return comport;
                }


        }catch (SerialPortException e){
            logger.info("serial port identifying exception with "+comport);
        }catch (Exception e){
            logger.error("serial port find exception.");
        }
        }

       return null;
    }

    //close the comport.
    public void closeComport(SerialPort serialPort){
        try {
            serialPort.closePort();
        } catch (SerialPortException e) {
            logger.debug("error when closing comport. "+serialPort.getPortName());
        }
    }




}
