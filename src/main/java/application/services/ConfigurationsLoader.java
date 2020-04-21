package application.services;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@Configuration
@PropertySources({@PropertySource("file:./config/environment.properties"),
                @PropertySource("file:./config/application.properties")})
public class ConfigurationsLoader {

}
