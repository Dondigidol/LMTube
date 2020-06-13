package application;

import application.services.LoggerService;
import org.apache.logging.log4j.Level;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LMTube {
    public static void main(String[] args) {
        SpringApplication.run(LMTube.class, args);
        LoggerService.log(Level.INFO, "Application is running!");
    }


}
