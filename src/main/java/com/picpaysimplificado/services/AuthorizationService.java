package com.picpaysimplificado.services;

import com.picpaysimplificado.domain.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Map;

@Service
public class AuthorizationService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${app.authorizationApi}")
    private String authorizationApiUrl;

    public boolean autorizeTransaction(User sender, BigDecimal value) {
        ResponseEntity<Map> authorizationResponse =  restTemplate.getForEntity(authorizationApiUrl, Map.class);

        if (HttpStatus.OK.equals(authorizationResponse.getStatusCode()) && "success".equals(authorizationResponse.getBody().get("status"))) {
            return true;
        } else {
            return false;
        }
    }
}
