package com.example.softeng.recipick.Models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class User {
    private String display_name;
    private String email;
    private Map<String, Boolean> ingredients;


    public User() {

    }

    public User(String display_name, String email) {
        this.display_name = display_name;
        this.email = email;
        this.ingredients = new HashMap<>();
    }

    public  Map<String, Boolean> getIngredients() {
        return this.ingredients;
    }

    public void setIngredients( Map<String, Boolean> ingredients) {
        this.ingredients = ingredients;
    }

    public String getDisplay_name() {
        return display_name;
    }

    public void setDisplay_name(String display_name) {
        this.display_name = display_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
