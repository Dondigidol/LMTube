package application.controllers;

import application.entities.Role;
import application.entities.VideoDetails;
import application.exceptions.InvalidLoginResponse;
import application.payload.JWTLoginSuccessResponse;
import application.payload.LoginRequest;
import application.security.JwtTokenProvider;
import application.security.LdapAuthenticationProvider;
import application.services.LoggerService;
import application.services.MapValidationErrorService;
import application.services.UserService;
import application.services.VideoDetailsService;
import org.apache.logging.log4j.Level;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

import static application.security.SecurityConstants.TOKEN_PREFIX;

@RestController
@RequestMapping("/api/user")
public class RestUserController {

    @Autowired
    private UserService userRoleService;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private VideoDetailsService videoDetailsService;

    @Autowired
    private LdapAuthenticationProvider ldapAuthenticationProvider;

    @Autowired
    private MapValidationErrorService mapValidationErrorService;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest,
                                              BindingResult result){

        try {
            ResponseEntity<?> errorMap = mapValidationErrorService.validate(result);
            if (errorMap != null){
                return errorMap;
            }

            Authentication authentication = ldapAuthenticationProvider.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

            if (authentication == null){
                LoggerService.log(Level.ERROR, loginRequest.getUsername() + ": authentication error");
                return new ResponseEntity<>(new InvalidLoginResponse(), HttpStatus.BAD_REQUEST);
            }

            SecurityContextHolder.getContext().setAuthentication(authentication);

            String token = tokenProvider.generateToken(authentication);
            String jwt = TOKEN_PREFIX + token;

            LoggerService.log(Level.INFO, loginRequest.getUsername()+": authentication success, token was generated successfully");
            return ResponseEntity.ok(new JWTLoginSuccessResponse(true, jwt));

        } catch (Exception e){
            LoggerService.log(Level.ERROR, e.getMessage());
        }

        return null;
    }

    @GetMapping("/roles")
    public ResponseEntity<Role[]> getAvailableRoles(){
        Role[] roles = userRoleService.getAvailableRoles();
        return new ResponseEntity<>(roles, HttpStatus.OK);
    }


    @GetMapping("/videos")
    public ResponseEntity<?> getUserVideos(Principal principal){
        List<VideoDetails> videoDetails = videoDetailsService.getUserVideoDetails(principal.getName());
        LoggerService.log(Level.INFO, "User '"+principal.getName()+"' has loaded his video list");
        return new ResponseEntity<>(videoDetails, HttpStatus.OK);
    }
}
