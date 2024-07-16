package com.example.journalapp.Model;

import com.google.firebase.Timestamp;

public class Journal {
    private String title;
    private String description;
    private String imageUrl;
    private String userID;
    private Timestamp timestamp;
    private String username;

    public Journal() {
    }

    public Journal(String title, String description, String imageUrl, String userID, Timestamp timestamp, String username) {
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
        this.userID = userID;
        this.timestamp = timestamp;
        this.username = username;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
