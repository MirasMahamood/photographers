package com.app.photographers;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class PhotographersApplication {

    public static void main(String[] args) {
        SpringApplication.run(PhotographersApplication.class, args);
    }

}
