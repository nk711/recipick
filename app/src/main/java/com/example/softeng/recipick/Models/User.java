package com.example.softeng.recipick.Models;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String display_name;
    private String email;
    private List<String> ingredients;


    public User() {

    }

    public User(String display_name, String email) {
        this.display_name = display_name;
        this.email = email;
        this.ingredients = new ArrayList<>();
    }

    public List<String> getIngredients() {
        if (this.ingredients==null) {
            this.ingredients = new ArrayList<>();
        }
        return ingredients;
    }

    public void setIngredients(List<String> ingredients) {
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
