package com.example.recipe_secured;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class RecipeSecuredApplication {

    public static void main(String[] args) {
        SpringApplication.run(RecipeSecuredApplication.class, args);
    }

    // Define the RestTemplate bean here
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
