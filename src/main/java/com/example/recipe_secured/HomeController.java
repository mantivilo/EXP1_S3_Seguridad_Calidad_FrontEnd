package com.example.recipe_secured;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private TokenStore tokenStore;

    @GetMapping("/home")
    public String home(Model model) {
        String backendUrl = "http://localhost:8081/api/recipes";
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        String token = tokenStore.getToken();

        // Ensure isLoggedIn is always defined
        model.addAttribute("isLoggedIn", token != null && !token.isEmpty());

        if (token != null && !token.isEmpty()) {
            headers.set("Authorization", token);
        }

        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<List> response = restTemplate.exchange(
                    backendUrl,
                    HttpMethod.GET,
                    entity,
                    List.class
            );
            List recipes = response.getBody();
            model.addAttribute("recipes", recipes);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "An error occurred while loading recipes.");
        }

        return "home";
    }
}

