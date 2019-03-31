package com.example.softeng.recipick;

public class RecipeIngredient {

    private String title;
    private String measurement;
    private String quantity;

    public RecipeIngredient(String title, String measurement, String quantity) {
        this.title = title;
        this.measurement = measurement;
        this.quantity = quantity;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMeasurement() {
        return measurement;
    }

    public void setMeasurement(String measurement) {
        this.measurement = measurement;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
}
