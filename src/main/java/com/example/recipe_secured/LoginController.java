package com.example.recipe_secured;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/user-login")
    public String showLoginPage() {
        return "login"; // Renders login.html
    }
}
