package com.example.recipe_secured;

import org.springframework.stereotype.Component;

@Component
public class TokenStore {
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void clearToken() {
        this.token = null;
    }
}
