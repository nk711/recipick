package com.example.softeng.recipick;

public class User {
    private String display_name;
    private String email;

    public User(String display_name, String email) {
        this.display_name = display_name;
        this.email = email;
    }

    public String getDisplay_name() {
        return display_name;
    }

    public void setDisplay_name(String display_name) {
        this.display_name = display_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
