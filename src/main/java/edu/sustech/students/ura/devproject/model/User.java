package edu.sustech.students.ura.devproject.model;

import java.io.Serializable;

public class User implements Serializable {
    private String username;
    private String password;
    private SaveData saveData;

    // Constructors, getters, and setters
    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.saveData = new SaveData();
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public SaveData getSaveData() {
        return saveData;
    }

    public void setSaveData(SaveData saveData) {
        this.saveData = saveData;
    }
}