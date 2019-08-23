package com.kumana.iotp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.intelligt.modbus.jlibmodbus.ModbusMaster;
import com.intelligt.modbus.jlibmodbus.ModbusMasterFactory;
import com.intelligt.modbus.jlibmodbus.exception.ModbusIOException;
import com.intelligt.modbus.jlibmodbus.serial.SerialParameters;
import com.intelligt.modbus.jlibmodbus.serial.SerialPort;
import com.intelligt.modbus.jlibmodbus.serial.SerialPortException;
import com.kumana.iotp.alertistner.AlertListner;
import com.kumana.iotp.alertistner.AlertType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;


/**
 * this class represents modbusRtu master connection
 **/

@Service
public class ModbusMasterConnection extends DeviceConnection {


    private final Logger logger = LoggerFactory.getLogger(ModbusMasterConnection.class);


    @Autowired
    List<AlertListner> alertListners;

    @Autowired
    ConnectionConfig connectionConfig;


    ObjectMapper MAPPER;
    public SerialParameters serialParameters;
    public ModbusMaster modbusMaster;


    String comport;
    int dataBits;
    int stopBits;
    int slaveId;
    int offset;
    int quantity;


    /**
     * this method will fetch all the data from
     * configuration class after the instantiation of the bean
     * project will shut down if no properties are found.
     **/
    @PostConstruct
    public void init() {
        serialParameters = new SerialParameters();
        comport = connectionConfig.getComport();
        stopBits = connectionConfig.getStopbits();
        dataBits = connectionConfig.getDatabits();
        slaveId = connectionConfig.getSlaveid();
        offset = connectionConfig.getOffset();
        quantity = connectionConfig.getQuantity();
    }

    boolean firstTry = true;

    /**
     * setting up a ModbusRtuMaster conncetion.
     **/
    @Override
    public boolean setupDevice() {
        serialParameters.setDevice(comport);
        serialParameters.setStopBits(stopBits);
        serialParameters.setDataBits(dataBits);
        serialParameters.setBaudRate(SerialPort.BaudRate.BAUD_RATE_9600);
        serialParameters.setParity(SerialPort.Parity.NONE);
        logger.info("trying to setup the modbus rtu connection");
        try {
            modbusMaster = ModbusMasterFactory.createModbusMasterRTU(serialParameters);
            modbusMaster.connect();
            connectionIsReady = true;
            logger.info("modbus rtu is ready");
        } catch (SerialPortException | ModbusIOException e) {
            logger.warn("modbus rtu is not ready");
            connectionIsReady = false;
            if (!firstTry) {
                alertListners.forEach(alertListner -> alertListner.onAlertReceived(AlertType.CRITICAL));
            }
            firstTry = false;
        }
        return connectionIsReady;
    }


}