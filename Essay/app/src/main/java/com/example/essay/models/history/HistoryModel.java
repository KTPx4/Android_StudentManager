package com.example.essay.models.history;

public class HistoryModel {
    private String user;
    private String date;

    public HistoryModel() {
    }

    public HistoryModel(String user, String date) {
        this.user = user;
        this.date = date;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
