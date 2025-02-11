package org.example.technihongo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
//@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
@SpringBootApplication
public class TechNihongoApplication {
    public static void main(String[] args) {
        SpringApplication.run(TechNihongoApplication.class, args);
    }
}
