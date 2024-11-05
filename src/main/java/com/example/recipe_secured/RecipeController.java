package com.example.recipe_secured;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RecipeController {

    @GetMapping("/recipe")
    public String recipe(@RequestParam("name") String name, Model model) {
        // Sample static recipe details
        model.addAttribute("name", name);
        model.addAttribute("ingredients", List.of("1 lb pasta", "2 cups tomato sauce", "1/2 cup cheese"));
        model.addAttribute("instructions", "Cook pasta, add sauce, sprinkle cheese.");
        model.addAttribute("cookTime", "30 minutes");
        model.addAttribute("difficulty", "Easy");

        return "recipe";
    }
}
