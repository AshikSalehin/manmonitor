package com.msbd.manmon;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.Transactional;

//@Transactional(value = "em")
//@ComponentScan(basePackages = { "com.msbd.manmon" })

@SpringBootApplication
@EnableScheduling
@EnableAutoConfiguration
public class ManMonApplication {
    
    public static void main(String[] args) {
	
        SpringApplication.run(ManMonApplication.class, args);
	
    }

}