package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@ServletComponentScan
public class JmmusicApplication {

    public static void main(String[] args) {
        SpringApplication.run(JmmusicApplication.class, args);
    }

}
