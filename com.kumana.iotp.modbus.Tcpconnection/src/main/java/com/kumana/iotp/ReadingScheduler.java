package com.kumana.iotp;

import com.intelligt.modbus.jlibmodbus.exception.ModbusIOException;
import com.intelligt.modbus.jlibmodbus.exception.ModbusNumberException;
import com.intelligt.modbus.jlibmodbus.exception.ModbusProtocolException;
import com.kumana.iotp.alertistner.AlertListner;
import com.kumana.iotp.alertistner.AlertType;
import com.kumana.iotp.readingslistner.HoldingRegisterListner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReadingScheduler {

    private final Logger logger = LoggerFactory.getLogger(ReadingScheduler.class);

    @Autowired
    ModbusMasterConnection modbusMasterConnection;

    @Autowired
    List<HoldingRegisterListner> holdingRegisterListners;

    @Autowired
    List<AlertListner> alertListners;

    @Scheduled(fixedDelayString = "${fixedDelay.in.milliseconds}")
    public void triggerDevice() {

        if (modbusMasterConnection.connectionIsReady) {
            int[] registerValues;
            try {
                registerValues = modbusMasterConnection.modbusMaster.readHoldingRegisters(modbusMasterConnection.slaveId, modbusMasterConnection.offset, modbusMasterConnection.quantity);
                holdingRegisterListners.forEach(holdingRegisterListner -> holdingRegisterListner.onHoldingRegisterReceived(registerValues));
            } catch (ModbusProtocolException | ModbusNumberException | ModbusIOException e) {
                logger.warn("error triggering the modbus tcp connection.");
                try {
                    modbusMasterConnection.modbusMaster.disconnect();
                    logger.error("modbus tcp disconnection successful.");
                } catch (ModbusIOException e1) {
                    logger.error("modbus tcp disconnection failed.");
                }
                modbusMasterConnection.setupDevice();
                logger.warn("generating an modbus tcp alert ");
                alertListners.forEach(alertListner -> alertListner.onAlertReceived(AlertType.CRITICAL));
            }


        } else {
            /**
             generate an alert
             **/
            modbusMasterConnection.setupDevice();
        }
    }


}
