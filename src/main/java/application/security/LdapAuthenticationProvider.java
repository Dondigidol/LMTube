package application.security;

import application.entities.Role;
import application.entities.User;
import application.services.LoggerService;
import application.services.UserService;
import org.apache.logging.log4j.Level;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.naming.NamingException;
import java.util.Collections;

@Component
public class LdapAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private UserService userService;

    @Value("${service.users.creator.username}")
    private String creator;
    @Value("${service.users.creator.password}")
    private String creatorPassword;

    @Value("${service.users.moderator.username}")
    private String moderator;
    @Value("${service.users.moderator.password}")
    private String moderatorPassword;

    @Value("${service.users.administrator.username}")
    private String administrator;
    @Value("${service.users.administrator.password}")
    private String administratorPassword;



    @Override
    public Authentication authenticate(Authentication auth){
        String username = auth.getName();
        String password = auth.getCredentials().toString();

        try {
            if (serviceUser(username, password) != null){
                User user = serviceUser(username, password);
                userService.saveUser(user);
                return new UsernamePasswordAuthenticationToken(user, null, Collections.emptyList());
            }

            ADService adService = new ADService(username, password);
            User adUser = adService.getUser();
            User user = userService.getUser(username);
            if (user != null){
                user.setPosition(adUser.getPosition());
                user.setFullName(adUser.getFullName());
                userService.saveUser(user);
                return new UsernamePasswordAuthenticationToken(user, null, Collections.emptyList());
            }
        } catch (NamingException e){
            LoggerService.log(Level.ERROR, e.getMessage());
        } catch (NullPointerException e){
            LoggerService.log(Level.ERROR, e.getMessage());
        }
        return null;
    }

    @Override
    public boolean supports(Class<?> auth){
        return auth.equals(UsernamePasswordAuthenticationToken.class);
    }


    private User serviceUser(String username, String password){
        if (username.equals(creator)){
            if (password.equals(creatorPassword)){
                User user = new User();
                user.setRole(Role.CREATOR);
                user.setFullName("System Content Creator");
                user.setPosition("content creator");
                user.setUsername(username);
                return user;
            }
        }  else if (username.equals(moderator)){
            if (password.equals(moderatorPassword)){
                User user = new User();
                user.setRole(Role.MODERATOR);
                user.setFullName("System Content Moderator");
                user.setPosition("Content Moderator");
                user.setUsername(username);
                return user;
            }
        } else if (username.equals(administrator)){
            if (password.equals(administratorPassword)){
                User user = new User();
                user.setRole(Role.ADMINISTRATOR);
                user.setFullName("System Administrator");
                user.setPosition("administrator");
                user.setUsername(username);
                return user;
            }
        }

        return null;
    }
}
