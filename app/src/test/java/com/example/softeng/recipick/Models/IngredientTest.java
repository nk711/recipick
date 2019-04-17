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

public class IngredientTest {
    @Mock
    Recipe recipe;
    @Mock
    Map<String, Boolean> ingredientsQuery;
    @Mock
    List<String> images;
    @InjectMocks
    Ingredient ingredient = new Ingredient();

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void TestGetName() {
        ingredient.setName("TestName");
        String result = ingredient.getName();
        Assertions.assertEquals("TestName", result);
    }


}
