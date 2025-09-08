package com.nhbank.ngw;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class NGWApplication {
    public static void main(String[] args) {
        SpringApplication.run(NGWApplication.class, args);
    }
}
