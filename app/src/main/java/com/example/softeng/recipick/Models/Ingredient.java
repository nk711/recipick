/**
 * Ingredient.java
 */
package com.example.softeng.recipick.Models;

import java.io.Serializable;

/**
 * An ingredient of which recipes are made up of multiple instances of
 */
public class Ingredient implements Serializable {
    /** Holds the name of ingredient */
    private String name ;
    /** Holds the quantity of ingredient */
    private String quantity;
    /** Holds the measurement of ingredient */
    private String measurement;

    /**
     * Default constructor
     */
    public Ingredient() {

    }

    /**
     * A parameterised constructor that takes in name, quantity and measurement
     * as parameters and initialises the fields.
     *
     * @param name The name of the ingredient
     * @param quantity The amount of which the ingredient is needed
     * @param measurement The units which are being used for the quantity
     */
    public Ingredient(String name, String quantity, String measurement) {
        this.name = name;
        this.quantity = quantity;
        this.measurement = measurement;
    }

    /**
     * An accessor for the ingredient's name.
     *
     * @return - name of ingredient
     */
    public String getName() {
        return name;
    }

    /**
     * A mutator for the ingredient's name.
     *
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * An accessor for the ingredient's quantity.
     *
     * @return - ingredient quantity
     */
    public String getQuantity() {
        return quantity;
    }

    /**
     * A mutator for the ingredient's quantity.
     *
     * @param quantity
     */
    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    /**
     * An accessor for the ingredient's measurement
     *
     * @return - ingredient's measurement
     */
    public String getMeasurement() {
        return measurement;
    }

    /**
     * A mutator for the ingredient's measurement.
     *
     * @param measurement
     */
    public void setMeasurement(String measurement) {
        this.measurement = measurement;
    }
}
