package com.example.softeng.recipick.Models;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.mockito.Mockito.*;

class ReviewTest {
    @Mock
    List<String> images;
    @InjectMocks
    Review review = new Review(images, "author", 10);

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void TestGetAuthor() {
        review.setAuthor("TestName");
        String result = review.getAuthor();
        Assertions.assertEquals("TestName", result);

    }

    @Test
    void TestGetRating() {
        int expected = 10;
        review.setRating(expected);
        int result = review.getRating();
        Assertions.assertEquals(expected, result);

    }
}