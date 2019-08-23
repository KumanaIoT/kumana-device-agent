package com.kumana.iotp;


import com.intelligt.modbus.jlibmodbus.Modbus;
import com.intelligt.modbus.jlibmodbus.ModbusMaster;
import com.intelligt.modbus.jlibmodbus.exception.ModbusIOException;
import com.intelligt.modbus.jlibmodbus.master.ModbusMasterTCP;
import com.intelligt.modbus.jlibmodbus.tcp.TcpParameters;
import com.kumana.iotp.alertistner.AlertListner;
import com.kumana.iotp.alertistner.AlertType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

@Service
public class ModbusMasterConnection extends DeviceConnection {


    private final Logger logger = LoggerFactory.getLogger(ModbusMasterConnection.class);

    @Autowired
    List<AlertListner> alertListners;

    @Autowired
    ConnectionConfig connectionConfig;


    ModbusMaster modbusMaster;
    TcpParameters tcpParameters = new TcpParameters();


    int slaveId;
    int offset;
    int quantity;


    @PostConstruct
    void init() {
        this.slaveId = connectionConfig.getSlaveid();
        this.offset = connectionConfig.getOffset();
        this.quantity = connectionConfig.getQuantity();
    }

    boolean firstTry = true;

    @Override
    boolean setupDevice() {

        try {
            tcpParameters.setHost(InetAddress.getByName(connectionConfig.getHost()));
            tcpParameters.setKeepAlive(connectionConfig.isAlive());
            tcpParameters.setPort(connectionConfig.getPort());
            connectionIsReady = false;
        } catch (UnknownHostException e) {
            logger.error("unknown host ");
        }

        if (modbusMaster == null) modbusMaster = new ModbusMasterTCP(tcpParameters);

        Modbus.setAutoIncrementTransactionId(true);

        if (modbusMaster.isConnected()) try {
            modbusMaster.disconnect();
        } catch (ModbusIOException e) {
            connectionIsReady = false;
            alertListners.forEach(alertListner -> alertListner.onAlertReceived(AlertType.CRITICAL));
            logger.error("modbus Tcp disconnection failed.");
        }

        if (!modbusMaster.isConnected()) try {
            modbusMaster.connect();
            connectionIsReady = true;
            logger.info("modbus Tcp connection success.");
        } catch (ModbusIOException e) {
            connectionIsReady = false;
            if (!firstTry) {
                alertListners.forEach(alertListner -> alertListner.onAlertReceived(AlertType.CRITICAL));
            }
            logger.info("modbus Tcp connection not ready.");
            firstTry = false;
        }

        return connectionIsReady;
    }


}
