package application.security;

import application.entities.User;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.naming.NamingException;
import java.util.Collections;

@Component
public class LdapAuthenticationProvider implements AuthenticationProvider {

    @Override
    public Authentication authenticate(Authentication auth){
        String username = auth.getName();
        String password = auth.getCredentials().toString();
        try {
            ADService adService = new ADService(username, password);
            adService.init();
            User user = adService.getUser();
            return new UsernamePasswordAuthenticationToken(user, password, Collections.emptyList());
        } catch (NamingException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean supports(Class<?> auth){
        return auth.equals(UsernamePasswordAuthenticationToken.class);
    }

}
