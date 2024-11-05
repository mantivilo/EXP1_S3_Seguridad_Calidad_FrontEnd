package com.example.recipe_secured;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class HomeController {

    @GetMapping("/home")
    public String home(Model model) {
        // Ejemplo estatico, esto podria venir de DB
        List<String> recipes = new ArrayList<>();
        recipes.add("Spaghetti Carbonara");
        recipes.add("Chicken Tikka Masala");
        recipes.add("Classic Cheesecake");

        model.addAttribute("recipes", recipes);
        return "home";
    }

    @GetMapping("/search")
    public String search() {
        return "search"; 
    }
}

