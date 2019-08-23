package com.kumana.iotp;


import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ModbusTcpStarter implements CommandLineRunner {


    public static void main(String[] args) {
        SpringApplication.run(ModbusTcpStarter.class);
    }


    @Override
    public void run(String... strings) throws Exception {

    }


}
