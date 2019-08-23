package com.kumana.iotp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * this is the starter application for a AWS cloud implementations
 * this will package cloudconnector and a awsImpl together.
 **/
@SpringBootApplication
@EnableScheduling
public class AwsConnector implements CommandLineRunner {


    private final Logger logger = LoggerFactory.getLogger(AwsConnector.class);

    public static void main(String[] args) {
        SpringApplication.run(AwsConnector.class, args);
    }


    @Override
    public void run(String... strings) throws Exception {
        logger.info("started the application AwsConnector.");
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public String toString() {
        return "AwsConnector";
    }
}
