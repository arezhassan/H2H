package com.example.h2h;

public class Admin {
    String email;
    String password;

    public Admin(String email, String password) {
        this.email = email;
        this.password = password;
    }
    public Admin(){

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
