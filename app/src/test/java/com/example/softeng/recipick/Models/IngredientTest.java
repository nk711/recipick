package com.example.softeng.recipick.Models;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class IngredientTest {

    @Test
    @DisplayName("Create an Ingredient object")
    public void testIngredientConstruction() {
        Ingredient ingredient = new Ingredient();
    }

    @Test
    @DisplayName("Get the name of the ingredient.")
    public void testAccessorIngredientName() {
        Ingredient ingredient = new Ingredient();
        ingredient.setName("Ingredient1");
        Assertions.assertEquals("Ingredient1", ingredient.getName());
    }

    @Test
    @DisplayName("Change ingredient name.")
    public void testMutatorIngredientName() {
        Ingredient ingredient = new Ingredient();
        ingredient.setName("Ingredient1");
        ingredient.setName("Ingredient2");
        Assertions.assertEquals("Ingredient2", ingredient.getName());
    }

    @Test
    @DisplayName("Get quantity.")
    public void testAccessorQuantity() {
        Ingredient ingredient = new Ingredient();
        ingredient.setQuantity("3");
        Assertions.assertEquals("3", ingredient.getQuantity());
    }

    @Test
    @DisplayName("Change quantity.")
    public void testMutatorQuantity() {
        Ingredient ingredient = new Ingredient();
        ingredient.setQuantity("3");
        ingredient.setQuantity("5");
        Assertions.assertEquals("5", ingredient.getQuantity());
    }

    @Test
    @DisplayName("Get measurement.")
    public void testAccessorMeasurement() {
        Ingredient ingredient = new Ingredient();
        ingredient.setMeasurement("tbspn");
        Assertions.assertEquals("tbspn", ingredient.getMeasurement());
    }

    @Test
    @DisplayName("Change measurement.")
    public void testMutatorMeasurement() {
        Ingredient ingredient = new Ingredient();
        ingredient.setMeasurement("tbspn");
        ingredient.setMeasurement("tspn");
        Assertions.assertEquals("tspn", ingredient.getMeasurement());
    }


}
