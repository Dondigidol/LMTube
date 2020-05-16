package application.security;

import application.entities.ADUser;
import application.entities.User;
import application.entities.UserRole;
import application.services.UserRoleService;
import application.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import javax.naming.AuthenticationException;
import javax.naming.NamingException;
import java.util.Collections;

@Component
public class LdapAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRoleService userRoleService;

    @Override
    public Authentication authenticate(Authentication auth){
        String username = auth.getName();
        String password = auth.getCredentials().toString();
        try {
            ADService adService = new ADService(username, password);
            adService.init();
            ADUser adUser = adService.getUser();
            UserRole userRole = userRoleService.getUserRole(adUser.getUsername());
            if (userRole != null){
                User user = new User();
                user.setUsername(adUser.getUsername());
                user.setPassword(password);
                user.setPosition(adUser.getPosition());
                user.setFullName(adUser.getFullName());
                user.setRole(userRole.getRole());
                return new UsernamePasswordAuthenticationToken(userService.saveUser(user), password, Collections.emptyList());
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

    @Bean
    BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }



}
