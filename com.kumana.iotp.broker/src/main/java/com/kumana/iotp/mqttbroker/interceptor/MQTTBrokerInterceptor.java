package com.kumana.iotp.mqttbroker.interceptor;

import io.moquette.interception.AbstractInterceptHandler;
import io.moquette.interception.messages.InterceptConnectMessage;
import io.moquette.interception.messages.InterceptDisconnectMessage;
import io.moquette.interception.messages.InterceptPublishMessage;
import io.moquette.interception.messages.InterceptSubscribeMessage;
import io.moquette.interception.messages.InterceptUnsubscribeMessage;
import io.netty.buffer.ByteBuf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

public class MQTTBrokerInterceptor extends AbstractInterceptHandler {

    private static final Logger logger = LoggerFactory.getLogger(MQTTBrokerInterceptor.class);

    public String getID() {
        return null;
    }

    @Override
    public void onConnect(final InterceptConnectMessage msg) {
        logger.info("Client has been connected : {}, at {}" , msg.getClientID(), new Date());
        System.out.println("Client has been connected : " + msg.getClientID());
    }

    @Override
    public void onDisconnect(final InterceptDisconnectMessage msg) {
        logger.info("Client has been disconnected : {}, at {}" , msg.getClientID(), new Date());
        System.out.println("Client has been disconnected : " + msg.getClientID());
    }

    @Override
    public void onPublish(final InterceptPublishMessage msg) {
        logger.debug("Message published from client : {}, to topic : {}", msg.getClientID(), msg.getTopicName());
        System.out.println("Message published from client : " + msg.getClientID() + ", to topic : " + msg.getTopicName());
        final ByteBuf payload = msg.getPayload();
        byte[] array = new byte[payload.readableBytes()];
        payload.getBytes(0, array);
        logger.trace("Message content : {}", new String(array));
    }

    @Override
    public void onSubscribe(final InterceptSubscribeMessage msg) {
        logger.info("Client has been subscribed : {}", msg.getClientID());
        System.out.println("Client has been subscribed : " + msg.getClientID());
    }

    @Override
    public void onUnsubscribe(final InterceptUnsubscribeMessage msg) {
        logger.info("Client has been un-subscribed : {}", msg.getClientID());
        System.out.println("Client has been un-subscribed : " + msg.getClientID());
    }

}
