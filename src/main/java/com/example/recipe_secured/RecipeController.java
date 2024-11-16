package com.example.recipe_secured;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.example.recipe_secured.model.Recipe;

import java.util.List;

@Controller
public class RecipeController {

    @Autowired
    private TokenStore tokenStore;

    // Public endpoint to display the list of recipes
    @GetMapping("/recipes")
    public String getRecipes(Model model) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8081/api/recipes"; // Backend endpoint for public recipe list

        try {
            // Call the backend without token (public access)
            ResponseEntity<List> response = restTemplate.exchange(url, HttpMethod.GET, null, List.class);

            // Add recipes to the model if available
            List recipes = response.getBody();
            if (recipes != null) {
                model.addAttribute("recipes", recipes);
            } else {
                model.addAttribute("errorMessage", "No recipes found.");
            }
        } catch (RestClientException e) {
            // Handle any failure in the public request
            model.addAttribute("errorMessage", "Unable to retrieve recipes.");
        }

        return "home"; // Render the home.html
    }

    @GetMapping("/recipes/details")
    public String getRecipeDetailsList(Model model) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8081/api/recipes/details";

        String token = tokenStore.getToken();
        if (token == null || token.isEmpty()) {
            return "redirect:/user-login"; // Redirect once if not authenticated
        }

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<List> response = restTemplate.exchange(url, HttpMethod.GET, entity, List.class);
            model.addAttribute("recipes", response.getBody());
            return "recipeDetailsList"; // Render details page
        } catch (HttpClientErrorException.Unauthorized e) {
            tokenStore.clearToken(); // Clear invalid token
            return "redirect:/user-login";
        } catch (RestClientException e) {
            model.addAttribute("errorMessage", "Error retrieving recipes.");
            return "redirect:/user-login";
        }
    }


    // Private endpoint to view details of a single recipe by ID
    @GetMapping("/recipe/details/{id}")
    public String getRecipeDetails(@PathVariable Long id, Model model) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8081/api/recipes/" + id; // Backend endpoint for recipe details by ID

        String token = tokenStore.getToken();
        if (token == null || token.isEmpty()) {
            // Redirect to login if the user is unauthenticated
            return "redirect:/user-login";
        }

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            // Call the backend endpoint to fetch the recipe details
            ResponseEntity<Recipe> response = restTemplate.exchange(url, HttpMethod.GET, entity, Recipe.class);
            model.addAttribute("recipe", response.getBody());
            return "recipeDetails"; // Render the recipeDetails.html template
        } catch (RestClientException e) {
            // If the token is expired or invalid, clear the token and redirect to login
            tokenStore.setToken(null);
            return "redirect:/user-login";
        }
    }
}
