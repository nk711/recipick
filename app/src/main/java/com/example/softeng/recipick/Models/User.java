/**
 * User.java
 */
package com.example.softeng.recipick.Models;


import java.util.HashMap;
import java.util.Map;

/**
 * An account using the app
 */
public class User {
    /** Holds the display name of user */
    private String display_name;
    /** Holds the user's email address */
    private String email;
    /** Holds the user's ingredients */
    private Map<String, Boolean> ingredients;
    /** Holds the user's favourite recipes */
    private Map<String, Recipe> favourites;
    /** Holds the user's trolley */
    private Map<String, Boolean> trolley;

    /**
     * A default constructor
     */
    public User() {

    }

    /**
     * A parameterised constructor that initialises the fields.
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

    /**
     * An accessor for the user's favourites
     * @return - user's favourite recipes
     */
    public Map<String, Recipe> getFavourites() {
        return favourites;
    }

    /**
     * A mutator for the user's favourites
     * @param favourites
     */
    public void setFavourites(Map<String, Recipe> favourites) {
        this.favourites = favourites;
    }

    /**
     * An accessor for the user's ingredients
     * @return - user's ingredients
     */
    public  Map<String, Boolean> getIngredients() {
        return this.ingredients;
    }

    /**
     * A mutator for the user's ingredients
     * @param ingredients
     */
    public void setIngredients( Map<String, Boolean> ingredients) {
        this.ingredients = ingredients;
    }

    /**
     * An accessor for the user's trolley
     * @return - ingredients in user's trolley
     */
    public Map<String, Boolean> getTrolley() {
        return this.trolley;
    }

    /**
     * A mutator for the user's trolley
     * @param trolley
     */
    public void setTrolley(Map<String, Boolean> trolley) {
        this.trolley = trolley;
    }

    /**
     * An accessor for the user's display name
     * @return - display name
     */
    public String getDisplay_name() {
        return display_name;
    }

    /**
     * A mutator for the user's display name
     * @param display_name
     */
    public void setDisplay_name(String display_name) {
        this.display_name = display_name;
    }

    /**
     * An accessor for the user's email address
     * @return - user's email address
     */
    public String getEmail() {
        return email;
    }

    /**
     * A mutator for the user's email
     * @param email
     */
    public void setEmail(String email) {
        this.email = email;
    }
}
