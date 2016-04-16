package com.nortal.treasurehunt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@ImportResource({"classpath*:applicationContext.xml"})
public class TreasureHuntSpringBootApplication {

    public static void main(String[] args) {
        SpringApplication.run(TreasureHuntSpringBootApplication.class, args);
    }
}
