package application.controllers;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/user")
public class RestUserController {

    @PostMapping("/login")
    public String login(@RequestParam("username") String username,
                        @RequestParam("password") String password){

        HttpHeaders headers = new HttpHeaders();

        MultiValueMap<String, String> parametersMap = new LinkedMultiValueMap<>();
        parametersMap.add("username", username);
        parametersMap.add("password", password);

        HttpEntity<MultiValueMap<String,String>> requestEntity = new HttpEntity<>(parametersMap, headers);

        RestTemplate restTemplate = new RestTemplate();



        return null;
    }
}
