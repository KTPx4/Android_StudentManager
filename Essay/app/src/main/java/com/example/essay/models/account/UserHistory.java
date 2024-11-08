package com.example.essay.models.account;

public class UserHistory {

    private String name;
    private String user;
    private String phone;
    private String role;
    private String linkAvt;
    private String date;

    public UserHistory() {
    }

    public UserHistory(String name, String user, String phone, String role, String linkAvt, String date) {
        this.name = name;
        this.user = user;
        this.phone = phone;
        this.role = role;
        this.linkAvt = linkAvt;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getLinkAvt() {
        return linkAvt;
    }

    public void setLinkAvt(String linkAvt) {
        this.linkAvt = linkAvt;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
