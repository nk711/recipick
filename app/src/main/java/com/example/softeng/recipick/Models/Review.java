package com.example.softeng.recipick.Models;

import java.util.List;

public class Review {
    private List<String> images;
    private String author;
    private int rating;

    public Review(List<String> images, String author, int rating) {
        this.images = images;
        this.author = author;
        this.rating = rating;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
