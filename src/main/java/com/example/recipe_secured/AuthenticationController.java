package com.example.recipe_secured;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Controller
public class AuthenticationController {

    @Autowired
    private TokenStore tokenStore; // Injecting TokenStore

    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password, Model model) {
        RestTemplate restTemplate = new RestTemplate();
        String backendUrl = "http://localhost:8081/api/auth/login";

        Map<String, String> credentials = new HashMap<>();
        credentials.put("username", username);
        credentials.put("password", password);

        HttpEntity<Map<String, String>> request = new HttpEntity<>(credentials);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(backendUrl, request, Map.class);
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                Map<String, String> responseBody = response.getBody();
                String token = responseBody.get("token");

                if (token != null) {
                    tokenStore.setToken("Bearer " + token);
                    return "redirect:/recipes/details"; // Redirect to the recipe details list page
                } else {
                    model.addAttribute("errorMessage", "Token not found in the response.");
                }
            } else {
                model.addAttribute("errorMessage", "Invalid login credentials.");
            }
        } catch (Exception e) {
            model.addAttribute("errorMessage", "An error occurred during login.");
        }

        return "login"; // Redirect to login page on failure
    }
}
