package com.example.softeng.recipick.Models;


import java.util.HashMap;
import java.util.Map;

/**
 * An account using the app
 */
public class User {
    private String display_name;
    private String email;
    private Map<String, Boolean> ingredients;
    private Map<String, Recipe> favourites;
    private Map<String, Boolean> trolley;

    public User() {

    }

    /**
     * Constructor method
     * @param display_name Name shown to other users
     * @param email Email address linked to account
     */
    public User(String display_name, String email) {
        this.display_name = display_name;
        this.email = email;
        this.ingredients = new HashMap<>();
        this.favourites = new HashMap<>();
        this.trolley = new HashMap<>();
    }

    //Getters and Setters
    public Map<String, Recipe> getFavourites() {
        return favourites;
    }

    public void setFavourites(Map<String, Recipe> favourites) {
        this.favourites = favourites;
    }

    public  Map<String, Boolean> getIngredients() {
        return this.ingredients;
    }

    public void setIngredients( Map<String, Boolean> ingredients) {
        this.ingredients = ingredients;
    }

    public Map<String, Boolean> getTrolley() {
        return this.trolley;
    }

    public void setTrolley(Map<String, Boolean> trolley) {
        this.trolley = trolley;
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
