/**
 * UserTest.java
 */
package com.example.softeng.recipick.Models;

import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.Map;

/**
 * JUnit testing for the User class.
 */
public class UserTest {
    /**
     * Mock User object
     */
    @InjectMocks
    User user = new User();

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    /**
     * Test the getter for the display name
     */
    @Test
    @DisplayName("Get display name.")
    public void testGetDisplayName() {
        user.setDisplay_name("MrRecipick");
        Assertions.assertEquals("MrRecipick", user.getDisplay_name());
    }

    /**
     * Test the getter for the user email
     */
    @Test
    @DisplayName("Get user email.")
    public void testGetEmail() {
        user.setEmail("myemail@surrey.ac.uk");
        Assertions.assertEquals("myemail@surrey.ac.uk", user.getEmail());
    }

    /**
     * Test the getter for the ingredients the user currently has
     */
    @Test
    @DisplayName("Get ingredients")
    public void testGetIngredients() {
        // HashMap that holds the ingredients of the user.
        Map<String, Boolean> ingredients = new HashMap<>();

        // Add ingredients to the HashMap
        ingredients.put("Ingredient1",true);
        ingredients.put("Ingredient2",true);
        ingredients.put("Ingredient3",true);

        // Set the ingredients of the user using the populated HashMap.
        user.setIngredients(ingredients);

        // Check if user actually has the ingredients within the HashMap
        boolean hasIngredients = user.getIngredients().keySet().containsAll(user.getIngredients().keySet());

        Assertions.assertTrue(hasIngredients);
    }

    @Test
    @DisplayName("Setting user ingredients")
    public void testSetIngredients() {
        // HashMap that holds the ingredients of the user.
        Map<String, Boolean> ingredients = new HashMap<>();

        // Add ingredients to the HashMap
        ingredients.put("Ingredient1",true);
        ingredients.put("Ingredient2",true);
        ingredients.put("Ingredient3",true);

        // Set the ingredients of the user using the populated HashMap.
        user.setIngredients(ingredients);

        // Check if user actually has the ingredients within the HashMap
        boolean hasIngredients = user.getIngredients().keySet().containsAll(user.getIngredients().keySet());

        // Add another ingredient in their list
        ingredients.put("Ingredient4", true);

        // Set the ingredients of the user again.
        user.setIngredients(ingredients);

        // Check if user actually has the ingredients within the HashMap
        boolean hasIngredients2 = user.getIngredients().keySet().containsAll(user.getIngredients().keySet());
        Assertions.assertTrue(hasIngredients);
        Assertions.assertTrue(hasIngredients2);
    }

    /**
     * Test the getter for the ingredients the user currently has
     */
    @Test
    @DisplayName("Get ingredients from Trolley")
    public void testGetTrolley() {
        // HashMap that holds the ingredients of the user.
        Map<String, Boolean> ingredients = new HashMap<>();

        // Add ingredients to the HashMap
        ingredients.put("Ingredient1",true);
        ingredients.put("Ingredient2",true);
        ingredients.put("Ingredient3",true);

        // Set the ingredients of the user using the populated HashMap.
        user.setTrolley(ingredients);

        // Check if user actually has the ingredients within the HashMap
        boolean hasIngredients = user.getTrolley().keySet().containsAll(user.getTrolley().keySet());

        Assertions.assertTrue(hasIngredients);
    }
    @Test
    @DisplayName("Setting user ingredients")
    public void testSetTrolley() {
        // HashMap that holds the ingredients of the user.
        Map<String, Boolean> ingredients = new HashMap<>();

        // Add ingredients to the HashMap
        ingredients.put("Ingredient1",true);
        ingredients.put("Ingredient2",true);
        ingredients.put("Ingredient3",true);

        // Set the ingredients of the user using the populated HashMap.
        user.setTrolley(ingredients);
        // Check if user actually has the ingredients within the their Trolley.
        boolean trolley= user.getTrolley().keySet().containsAll(user.getTrolley().keySet());
        // Add another ingredient in user's trolley
        ingredients.put("Ingredient4", true);
        // Set user trolley again
        user.setTrolley(ingredients);
        //Check if user has all the added ingredients in their trolley again.
        boolean trolley2 = user.getTrolley().keySet().containsAll(user.getTrolley().keySet());

        Assertions.assertTrue(trolley);
        Assertions.assertTrue(trolley2);
    }

    @Test
    @DisplayName("Get favourite recipes")
    public void testGetFavourites() {
        // HashMap that holds the favourite user's recipes
        Map<String, Recipe> favourites = new HashMap<>();

        // Creating Recipe objects
        Recipe recipe1 = new Recipe();
        Recipe recipe2 = new Recipe();
        Recipe recipe3 = new Recipe();

        // Populating hashmap with recipes
        favourites.put("1", recipe1);
        favourites.put("2", recipe2);
        favourites.put("3", recipe3);

        // Setting user's favourite recipes
        user.setFavourites(favourites);

        Assertions.assertTrue(user.getFavourites().keySet().containsAll(favourites.keySet()));
    }

    @Test
    @DisplayName("Set favourite recipes")
    public void testSetFavourites() {
        // HashMap that holds the favourite user's recipes
        Map<String, Recipe> favourites = new HashMap<>();

        // Creating Recipe objects
        Recipe recipe1 = new Recipe();
        Recipe recipe2 = new Recipe();
        Recipe recipe3 = new Recipe();
        Recipe recipe4 = new Recipe();

        // Populating hashmap with recipes
        favourites.put("1", recipe1);
        favourites.put("2", recipe2);
        favourites.put("3", recipe3);

        // Setting user's favourite recipes
        user.setFavourites(favourites);

        // Check if user has all the recipes added to their favourites
        boolean hasFavourites = user.getFavourites().keySet().containsAll(favourites.keySet());

        // Add another recipe to user's favourites
        favourites.put("4", recipe4);

        // Set user's favourites again
        user.setFavourites(favourites);

        // Check if user has all the recipes added to their favourites
        boolean hasFavourites2 = user.getFavourites().keySet().containsAll(favourites.keySet());

        Assertions.assertTrue(hasFavourites);
        Assertions.assertTrue(hasFavourites2);
    }



    

}