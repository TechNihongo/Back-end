package org.example.technihongo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
//@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
@SpringBootApplication
@ConfigurationPropertiesScan
@EnableScheduling
@EnableAsync
public class TechNihongoApplication {
    public static void main(String[] args) {
        SpringApplication.run(TechNihongoApplication.class, args);
    }
}
