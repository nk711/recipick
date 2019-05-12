package com.example.softeng.recipick.Models;

import java.io.Serializable;

/**
 * An ingredient of which recipes are made up of multiple instances of
 */
public class Ingredient implements Serializable {
    private String name ;
    private String quantity;
    private String measurement;


    public Ingredient() {

    }

    /**
     * Constructor method
     * @param name The name of the ingredient
     * @param quantity The amount of which the ingredient is needed
     * @param measurement The units which are being used for the quantity
     */
    public Ingredient(String name, String quantity, String measurement) {
        this.name = name;
        this.quantity = quantity;
        this.measurement = measurement;
    }

    //Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getMeasurement() {
        return measurement;
    }

    public void setMeasurement(String measurement) {
        this.measurement = measurement;
    }
}
