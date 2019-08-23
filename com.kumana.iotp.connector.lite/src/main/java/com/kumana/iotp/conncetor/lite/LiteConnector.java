package com.kumana.iotp.conncetor.lite;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.kumana.iotp.lite.KumanaLiteIotConfig;
import com.kumana.iotp.lite.actuatorlistner.ConcertLiteHubConsumer;
import com.kumana.iotp.publishers.Publish;
import com.kumana.iotp.sensorContol.Publisher;

/**
 * this is the starter application for Concert Lite connector implementations
 * this will package cloudconnector and a LiteImpl together.
 **/
@SpringBootApplication
@EnableScheduling
@ComponentScan({"com.kumana.iotp.lite","com.kumana.iotp.cloudconnector","com.kumana.iotp.core"})
public class LiteConnector implements CommandLineRunner {

	private final Logger logger = LoggerFactory.getLogger(LiteConnector.class);
	
	@Autowired
	private KumanaLiteIotConfig kumanaLiteIotConfig;

    public static void main(String[] args) {
        SpringApplication.run(LiteConnector.class, args);
    }


    @Override
    public void run(String... strings) throws Exception {
        logger.info("started the application LiteConnector.");
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public String toString() {
        return "LiteConnector";
    }
    
    @Bean
    public ConcertLiteHubConsumer concertIOTHubConsumer(){
        final ConcertLiteHubConsumer concertIOTHubConsumer = new ConcertLiteHubConsumer(kumanaLiteIotConfig.getMqttActuatorClientName(), kumanaLiteIotConfig.getActuatorTopic(),kumanaLiteIotConfig.getMqttServerUri());
        return concertIOTHubConsumer;
    }
    
    /*
    
    @Bean
    public Publish publisher(){
        final Publish publisher = new Publisher();
        return publisher;
    }
       
    */
}
