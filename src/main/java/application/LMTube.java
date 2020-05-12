package application;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
public class LMTube {

    public static void main(String[] args) {
        SpringApplication.run(LMTube.class, args);
    }

    @Configuration
    @Profile("dev")
    @PropertySource("file:./config/dev.properties")
    public static class DevelopmentConfiguration{

        @Bean
        InitializingBean init(){
            return ()-> System.out.println("Development profile is loaded.");
        }
    }

    @Configuration
    @Profile("prod")
    @PropertySource("file:./config/prod.properties")
    public static class ProductionConfiguration{

        @Bean
        InitializingBean init(){
            return ()-> System.out.println("Production profile is loaded.");
        }
    }



}
