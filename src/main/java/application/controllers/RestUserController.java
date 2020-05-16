package application.controllers;

import application.exceptions.InvalidLoginResponse;
import application.payload.JWTLoginSuccessResponse;
import application.payload.LoginRequest;
import application.security.JwtTokenProvider;
import application.security.LdapAuthenticationProvider;
import application.services.MapValidationErrorService;
import application.services.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static application.security.SecurityConstants.TOKEN_PREFIX;

@RestController
@RequestMapping("/api/user")
public class RestUserController {

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private LdapAuthenticationProvider ldapAuthenticationProvider;

    @Autowired
    private MapValidationErrorService mapValidationErrorService;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest,
                                              BindingResult result){

        ResponseEntity<?> errorMap = mapValidationErrorService.validate(result);
        if (errorMap != null){
            return errorMap;
        }

        Authentication authentication = ldapAuthenticationProvider.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        if (authentication == null){
            return new ResponseEntity<>(new InvalidLoginResponse(), HttpStatus.BAD_REQUEST);
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = TOKEN_PREFIX + tokenProvider.generateToken(authentication);
        return ResponseEntity.ok(new JWTLoginSuccessResponse(true, jwt));


    }

    @GetMapping("/roles")
    public ResponseEntity<List<String>> getAvailableRoles(){
        List<String> roles = userRoleService.getAvailableRoles();
        return new ResponseEntity<>(roles, HttpStatus.OK);
    }
}
