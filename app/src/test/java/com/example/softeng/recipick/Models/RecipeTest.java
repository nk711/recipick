/**
 * RecipeTest.java
 */
package com.example.softeng.recipick.Models;

import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

/**
 * JUnit testing for the Recipe class.
 */
public class RecipeTest {
    @InjectMocks
    Recipe recipe = new Recipe();

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    @DisplayName("Get recipe name.")
    void testGetRecipeName() {
        recipe.setName("Recipe1");
        Assertions.assertEquals("Recipe1", recipe.getName());
    }

    @Test
    @DisplayName("Get type of meal.")
    void testGetMeals() {
        recipe.setMeals("Lunch");
        Assertions.assertEquals("Lunch", recipe.getMeals());
    }

    @Test
    @DisplayName("Get user ID")
    void testGetUid() {
        recipe.setUid("123456");
        Assertions.assertEquals("123456", recipe.getUid());
    }

    @Test
    @DisplayName("Get recipe description")
    void testGetDescription() {
        recipe.setDescription("Nice recipe!");
        Assertions.assertEquals("Nice recipe!", recipe.getDescription());
    }


    @Test
    @DisplayName("Get recipe ingredients")
    void testGetIngredients() {
        List<Ingredient> ingredients = new ArrayList<>();
        Ingredient ingredient1 = new Ingredient();
        Ingredient ingredient2 = new Ingredient();
        Ingredient ingredient3 = new Ingredient();

        // Expected ingredients of the recipe in the list
        Object[] expectedIngredients = new Object[3];
        expectedIngredients[0] = ingredient1;
        expectedIngredients[1] = ingredient2;
        expectedIngredients[2] = ingredient3;

        // Add the recipe's ingredients
        ingredients.add(ingredient1);
        ingredients.add(ingredient2);
        ingredients.add(ingredient3);

        // Setting ingredients for the recipe
        recipe.setIngredients(ingredients);

        // Actual ingredients converted to an array of type Object when the getter is invoked.
        Object[] actualIngredients = recipe.getIngredients().toArray();

        Assertions.assertArrayEquals(expectedIngredients, actualIngredients);
    }

    @Test
    @DisplayName("Get recipe preparation.")
    void testGetPreparation() {
        recipe.setPreparation("Step1Step2");
        Assertions.assertEquals("Step1Step2", recipe.getPreparation());
    }

    @Test
    @DisplayName("Get duration.")
    void testGetDuration() {
        recipe.setDuration(2);
        Assertions.assertEquals(2, recipe.getDuration());
    }

    @Test
    @DisplayName("Get calories.")
    void testGetCalories() {
        recipe.setCalories(150);
        Assertions.assertEquals(150, recipe.getCalories());
    }

    @Test
    @DisplayName("Get budget.")
    void testGetBudget() {
        recipe.setBudget(10.00);
        Assertions.assertEquals(10.00, recipe.getBudget());
    }

    @Test
    @DisplayName("Is recipe shared.")
    void testIsShare() {
        Recipe recipe2 = new Recipe();
        recipe2.setShare(false);
        recipe.setShare(true);
        Assertions.assertEquals(true, recipe.isShare());
        Assertions.assertEquals(false, recipe2.isShare());
    }

    @Test
    @DisplayName("Is it visible to everyone.")
    void testIsVisibility() {
        Recipe recipe2 = new Recipe();
        recipe.setVisibility(false);
        recipe2.setVisibility(true);
        Assertions.assertEquals(false, recipe.isVisibility());
        Assertions.assertEquals(true, recipe2.isVisibility());
    }


}
