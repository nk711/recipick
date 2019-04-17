package com.example.softeng.recipick.Models;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.mockito.Mockito.*;

class ReviewTest {
    @Mock
    List<String> images;
    @InjectMocks
    Review review;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }
}