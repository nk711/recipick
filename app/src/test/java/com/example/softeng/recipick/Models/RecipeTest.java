package com.example.softeng.recipick.Models;

import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;

public class RecipeTest {
    @Mock
    List<Ingredient> ingredients;
    @Mock
    Map<String, Boolean> ingredientsQuery;
    @Mock
    List<String> images;
    @InjectMocks
    Recipe recipe = new Recipe();

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void TestGetName() {
        recipe.setName("TestName");
        String result = recipe.getName();
        Assertions.assertEquals("TestName", result);
    }

    @Test
    void TestGetMeals() {
        recipe.setMeals("TestMeal");
        String result = recipe.getMeals();
        Assertions.assertEquals("TestMeal", result);
    }

    @Test
    void TestGetUid() {
        recipe.setUid("TestMeal");
        String result = recipe.getMeals();
        Assertions.assertEquals(null, result);
    }

    @Test
    void TestGetDescription() {
        recipe.setDescription("TestMeal");
        String result = recipe.getDescription();
        Assertions.assertEquals("TestMeal", result);
    }


    @Test
    void TestGetIngredients() {
        recipe.setIngredients(this.ingredients);
        List result = recipe.getIngredients();
        Assertions.assertEquals(null, result);
    }

    @Test
    void TestGetPreparation() {
        recipe.setPreparation("TestMeal");
        String result = recipe.getPreparation();
        Assertions.assertEquals("TestMeal", result);
    }

    @Test
    void TestGetDuration() {
        recipe.setDuration(2);
        int result = recipe.getDuration();
        Assertions.assertEquals(2, result);
    }

    @Test
    void TestGetCalories() {
        recipe.setCalories(150);
        int result = recipe.getCalories();
        Assertions.assertEquals(150, result);
    }

    @Test
    void TestGetBudget() {
        recipe.setBudget(10.00);
        double result = recipe.getBudget();
        Assertions.assertEquals(10.00, result);
    }

    @Test
    void TestIsShare() {
        recipe.setShare(true);
        boolean result = recipe.isShare();
        Assertions.assertEquals(true, result);
    }

    @Test
    void TestIsVisibility() {
        recipe.setVisibility(false);
        boolean result = recipe.isVisibility();
        Assertions.assertEquals(false, result);
    }

}
