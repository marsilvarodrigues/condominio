package com.pmrodrigues.condominio;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication()
@EnableAutoConfiguration
@ComponentScan(basePackages = "com.pmrodrigues")
public class CondominioApplication {

    public static void main(String[] args) {
        SpringApplication.run(CondominioApplication.class, args);
    }
}
