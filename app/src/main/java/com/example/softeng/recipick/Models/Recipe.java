/**
 * Recipe.java
 */
package com.example.softeng.recipick.Models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A set of instructions to make a dish based off of ingredients provided
 */
public class Recipe implements Serializable {
    /** Holds the user id of the user that uploaded the recipe */
    private String uid = null;

    /** Holds the author of the recipe */
    private String author = null;

    /** Holds the name of the recipe */
    private String name = null;

    /** Holds the description of the recipe */
    private String description = null;

    /** Holds the list of the recipe's ingredients */
    private List<Ingredient> ingredients = null;

    /** Used to search recipes based on user's ingredients */
    private Map<String, Boolean> ingredientsQuery = null;

    /** Holds the preparation of how to make the recipe */
    private String preparation = null;

    /** Holds the duration of the recipe */
    private int duration = 0;

    /** Holds the amount of calories of the recipe */
    private int calories = 0;

    /** Holds the budget for the recipe */
    private double budget = 0.0;

    /** Holds the amount of servings for the recipe */
    private int servings = 0;

    /** Holds the type of cuisine */
    private String cuisine = null;

    /** Holds the type of meal */
    private String meals = null;

    /** Holds the list of images */
    private List<String> images = null;

    /** Holds the list of shared images */
    private List<String> sharedImages = null;

    /** If the recipe is shared or not */
    private boolean share = false;

    /** If the recipe is visible to users */
    private boolean visibility = false;


    /** Default constructor, allows values to be pushed onto firebase */
    public Recipe() {

    }

    /**
     * Constructor method
     * @param uid Database id of the recipe
     * @param name Name of the recipe
     * @param description Description provided by user
     * @param ingredients Ingredients needed for recipe
     * @param ingredientsQuery Database query for ingredients
     * @param preparation Steps required to prepare the recipe
     * @param duration Amount of time to make the recipe
     * @param calories How many calories in the recipe
     * @param budget Rough cost of all ingredients
     * @param servings How many people it serves
     * @param cuisine What cuisine category it falls under
     * @param meals What meal of the day it is suitable for
     * @param share Whether the user wishes to share the recipe of keep it for themselves
     * @param author The author of the recipe
     */
    public Recipe(String uid, String name, String description, List<Ingredient> ingredients, Map<String, Boolean> ingredientsQuery, String preparation, int duration,
                  int calories, double budget, int servings, String cuisine, String meals, boolean share, String author) {
        this.uid = uid;
        this.name = name;
        this.description = description;
        this.ingredients = ingredients;
        this.ingredientsQuery = ingredientsQuery;
        this.preparation = preparation;
        this.duration = duration;
        this.calories = calories;
        this.budget = budget;
        this.servings = servings;
        this.cuisine = cuisine;
        this.meals = meals;
        this.images = new ArrayList<>();
        this.share = share;
        this.author = author;
        this.sharedImages = new ArrayList<>();
    }

    /**
     * An accessor for the list of shared images
     *
     * @return - list of shared images
     */
    public List<String> getSharedImages() {
        return sharedImages;
    }

    /**
     * A mutator that sets the list of shared images
     *
     * @param sharedImages
     */
    public void setSharedImages(List<String> sharedImages) {
        this.sharedImages = sharedImages;
    }

    /**
     * A method that adds an image to the list of shared images
     *
     * @param sharedImage
     */
    public void addToSharedImages(String sharedImage) {
        if (this.sharedImages==null) {
            this.sharedImages = new ArrayList<>();
        }
        this.sharedImages.add(sharedImage);
    }

    /**
     * An accessor for the ingredients query
     * @return
     */
    public Map<String, Boolean> getIngredientsQuery() {
        return ingredientsQuery;
    }

    /**
     * A mutator for the ingredients query
     *
     * @param ingredientQuery
     */
    public void setIngredientsQuery( Map<String, Boolean> ingredientQuery) {
        this.ingredientsQuery = ingredientQuery;
    }

    /**
     * An accessor for the type of meal
     *
     * @return - meal type
     */
    public String getMeals() {
        return meals;
    }

    /**
     * A mutator for the type of meal
     *
     * @param meals
     */
    public void setMeals(String meals) {
        this.meals = meals;
    }

    /**
     * An accessor for the user id
     *
     * @return - user id
     */
    public String getUid() {
        return uid;
    }

    /**
     * A mutator for the user id
     *
     * @param uid
     */
    public void setUid(String uid) {
        this.uid = uid;
    }

    /**
     * An accessor for the name of the recipe
     *
     * @return - recipe name
     */
    public String getName() {
        return name;
    }

    /**
     * A mutator for the name of the recipe
     *
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * An accessor for the recipe description
     *
     * @return - description
     */
    public String getDescription() {
        return description;
    }

    /**
     * A mutator for the recipe description
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * An accessor for the list of ingredients
     *
     * @return - list of ingredients
     */
    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    /**
     * A mutator for the list of ingredients
     *
     * @param ingredients
     */
    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    /**
     * An accessor for the preparation
     *
     * @return - recipe preparation
     */
    public String getPreparation() {
        return preparation;
    }

    /**
     * A mutator for the recipe preparation
     *
     * @param preparation
     */
    public void setPreparation(String preparation) {
        this.preparation = preparation;
    }

    /**
     * An accessor for the recipe duration
     *
     * @return - recipe duration
     */
    public int getDuration() {
        return duration;
    }

    /**
     * A mutator for the recipe duration
     *
     * @param duration
     */
    public void setDuration(int duration) {
        this.duration = duration;
    }

    /**
     * An accessor for the calories
     *
     * @return - calories
     */
    public int getCalories() {
        return calories;
    }

    /**
     * A mutator for the calories
     *
     * @param calories
     */
    public void setCalories(int calories) {
        this.calories = calories;
    }

    /**
     * An accessor for recipe's budget
     *
     * @return - recipe budget
     */
    public double getBudget() {
        return budget;
    }

    /**
     * A mutator for the recipe's budget
     *
     * @param budget
     */
    public void setBudget(double budget) {
        this.budget = budget;
    }

    /**
     * An accessor for the servings
     * @return - servings
     */
    public int getServings() {
        return servings;
    }

    /**
     * A mutator for the servings
     * @param servings
     */
    public void setServings(int servings) {
        this.servings = servings;
    }

    /**
     * An accessor for the cuisine
     * @return - cuisine
     */
    public String getCuisine() {
        return cuisine;
    }

    /**
     * A mutator for the cuisine
     * @param cuisine
     */
    public void setCuisine(String cuisine) {
        this.cuisine = cuisine;
    }

    /**
     * An accessor for the list of images
     * @return - list of images
     */
    public List<String> getImages() {
        return images;
    }

    /**
     * A mutator for the list of images
     * @param images
     */
    public void setImages(List<String> images) {
        this.images = images;
    }

    /**
     * An accessor if the recipe is shared
     * @return - true if shared, otherwise false
     */
    public boolean isShare() {
        return share;
    }

    /**
     * An accessor if the recipe is visible
     * @return
     */
    public boolean isVisibility() {
        return visibility;
    }

    /**
     * A mutator for the recipe's visibility
     * @param visibility
     */
    public void setVisibility(boolean visibility) {
        this.visibility = visibility;
    }

    /**
     * A mutator for setting the recipe to be shared or not
     * @param share
     */
    public void setShare(boolean share) {
        this.share = share;
    }

    /**
     * An accessor for the recipe's author
     * @return - author
     */
    public String getAuthor() {
        return author;
    }

    /**
     * A mutator for the author
     * @param author
     */
    public void setAuthor(String author) {
        this.author = author;
    }
}
