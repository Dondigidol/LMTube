package application;

import application.services.LoggerService;
import org.apache.logging.log4j.Level;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class Lmplay extends SpringBootServletInitializer{
    public static void main(String[] args) {
        SpringApplication.run(Lmplay.class, args);
        LoggerService.log(Level.INFO, "Application is running!");
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application){
        return application.sources(Lmplay.class).properties("file:./config/application.properties");
    }




}
