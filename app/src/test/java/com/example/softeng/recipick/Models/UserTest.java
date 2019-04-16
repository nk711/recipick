package com.example.softeng.recipick.Models;

import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Map;

import static org.mockito.Mockito.*;

public class UserTest {
    @Mock
    Map<String, Boolean> ingredients;
    @InjectMocks
    User user = new User();

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void TestgetDisplay_name() {
        user.setDisplay_name("TestName");
        String result = user.getDisplay_name();
        Assertions.assertEquals("TestName", result);
    }

}