package com.example.softeng.recipick.Models;

import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import android.support.annotation.Nullable;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import static org.mockito.Mockito.*;

public class IngredientsTest {
    @Mock
    Recipe recipe;
    @Mock
    Ingredient ingredient;
    @Mock
    Map<String, Boolean> ingredientsQuery;
    @Mock
    List<String> images;
    @InjectMocks
    List<Ingredient> ingredients;

//    @InjectMocks
//    List<Ingredient> ingredients = new List<Ingredient>() {
//        @Override
//        public int size() {
//            return 0;
//        }
//
//        @Override
//        public boolean isEmpty() {
//            return false;
//            ;
//        }
//
//        @Override
//        public boolean contains(@androidx.annotation.Nullable Object o) {
//            return false;
//        }
//
//        @androidx.annotation.NonNull
//        @Override
//        public Iterator<Ingredient> iterator() {
//            return null;
//        }
//
//        @androidx.annotation.Nullable
//        @Override
//        public Object[] toArray() {
//            return new Object[0];
//        }
//
//        @Override
//        public <T> T[] toArray(@androidx.annotation.Nullable T[] ts) {
//            return null;
//        }
//
//        @Override
//        public boolean add(Ingredient ingredient) {
//            return false;
//        }
//
//        @Override
//        public boolean remove(@androidx.annotation.Nullable Object o) {
//            return false;
//        }
//
//        @Override
//        public boolean containsAll(@androidx.annotation.NonNull Collection<?> collection) {
//            return false;
//        }
//
//        @Override
//        public boolean addAll(@androidx.annotation.NonNull Collection<? extends Ingredient> collection) {
//            return false;
//        }
//
//        @Override
//        public boolean addAll(int i, @androidx.annotation.NonNull Collection<? extends Ingredient> collection) {
//            return false;
//        }
//
//        @Override
//        public boolean removeAll(@androidx.annotation.NonNull Collection<?> collection) {
//            return false;
//        }
//
//        @Override
//        public boolean retainAll(@androidx.annotation.NonNull Collection<?> collection) {
//            return false;
//        }
//
//        @Override
//        public void clear() {
//
//        }
//
//        @Override
//        public Ingredient get(int i) {
//            return null;
//        }
//
//        @Override
//        public Ingredient set(int i, Ingredient ingredient) {
//            return null;
//        }
//
//        @Override
//        public void add(int i, Ingredient ingredient) {
//
//        }
//
//        @Override
//        public Ingredient remove(int i) {
//            return null;
//        }
//
//        @Override
//        public int indexOf(@androidx.annotation.Nullable Object o) {
//            return 0;
//        }
//
//        @Override
//        public int lastIndexOf(@androidx.annotation.Nullable Object o) {
//            return 0;
//        }
//
//        @androidx.annotation.NonNull
//        @Override
//        public ListIterator<Ingredient> listIterator() {
//            return null;
//        }
//
//        @androidx.annotation.NonNull
//        @Override
//        public ListIterator<Ingredient> listIterator(int i) {
//            return null;
//        }
//
//        @androidx.annotation.NonNull
//        @Override
//        public List<Ingredient> subList(int i, int i1) {
//            return null;
//        }
//    };

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void TestGetName() {
        ingredients.add(ingredient);
        ingredients.get(ingredients.indexOf(ingredient)).setName("TestName");
        String result = ingredients.get(ingredients.indexOf(ingredient)).getName();
        Assertions.assertEquals("TestName", result);
    }


}
