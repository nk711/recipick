package com.example.softeng.recipick;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Recipe {
    private String name = null;
    private String description = null;
    private Map<String, Map.Entry<String, String>> ingredients = null;
    private String preparation = null;
    private int duration = 0;
    private int calories = 0;
    private double budget = 0.0;
    private int servings = 0;
    private String cuisine = null;
    private List<String> images = null;
    private boolean share = false;
    private boolean visibility = false;


    private int id;

    /** Default constructor, allows values to be pushed onto firebase */
    public Recipe() {

    }



    public Recipe(String name, String desciption, int id ) {
        this.name = name;
        this.description = desciption;
        this.id = id;
    }

    public Recipe(String name, String description, Map<String, Map.Entry<String, String>> ingredients, String preparation, int duration, int calories, double budget, int servings, String cuisine, boolean share) {
        this.name = name;
        this.description = description;
        this.ingredients = ingredients;
        this.preparation = preparation;
        this.duration = duration;
        this.calories = calories;
        this.budget = budget;
        this.servings = servings;
        this.cuisine = cuisine;
        this.images = new ArrayList<>();
        this.share = share;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Map<String, Map.Entry<String, String>> getIngredients() {
        return ingredients;
    }

    public void setIngredients(Map<String, Map.Entry<String, String>> ingredients) {
        this.ingredients = ingredients;
    }

    public String getPreparation() {
        return preparation;
    }

    public void setPreparation(String preparation) {
        this.preparation = preparation;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public double getBudget() {
        return budget;
    }

    public void setBudget(double budget) {
        this.budget = budget;
    }

    public int getServings() {
        return servings;
    }

    public void setServings(int servings) {
        this.servings = servings;
    }

    public String getCuisine() {
        return cuisine;
    }

    public void setCuisine(String cuisine) {
        this.cuisine = cuisine;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public boolean isShare() {
        return share;
    }

    public boolean isVisibility() {
        return visibility;
    }

    public void setVisibility(boolean visibility) {
        this.visibility = visibility;
    }

    public void setShare(boolean share) {
        this.share = share;
    }
}
