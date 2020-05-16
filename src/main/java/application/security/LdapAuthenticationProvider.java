package application.security;

import application.entities.User;
import application.entities.UserRole;
import application.services.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.naming.AuthenticationException;
import javax.naming.NamingException;
import java.util.Collections;

@Component
public class LdapAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private UserRoleService userRoleService;

    @Override
    public Authentication authenticate(Authentication auth){
        String username = auth.getName();
        String password = auth.getCredentials().toString();
        try {
            ADService adService = new ADService(username, password);
            adService.init();
            User user = adService.getUser();
            UserRole userRole = userRoleService.getUserRole(user.getUsername());
            if (userRole != null){
                return new UsernamePasswordAuthenticationToken(user, password, Collections.emptyList());
            }
        } catch (NamingException e){
            System.out.println(username + " - invalid username or password");
        }
        return null;
    }

    @Override
    public boolean supports(Class<?> auth){
        return auth.equals(UsernamePasswordAuthenticationToken.class);
    }



}
