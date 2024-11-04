package com.example.essay.models.account;

public class User {
    private String id;
    private String name;
    private String user;
    private String phone;
    private String role;
    private String linkAvt;
    private String birth;

    // Constructor
    public User(String name, String email, String phone, String role, String linkAvt, String birth) {
        this.name = name;
        this.user = email;
        this.phone = phone;
        this.role = role;
        this.linkAvt = linkAvt;
        this.birth = birth;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLinkAvt() {
        return linkAvt;
    }

    public void setLinkAvt(String linkAvt) {
        this.linkAvt = linkAvt;
    }

    // Getter and Setter methods
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
}
