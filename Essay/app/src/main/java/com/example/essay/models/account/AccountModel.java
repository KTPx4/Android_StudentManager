package com.example.essay.models.account;

public class AccountModel {
    private String user;
    private String pass;
    private String name;
    private String phone;
    private String birthDay;
    private String role;
    private String linkAvt;
    private String status;
    private String email;

    public AccountModel(){}
    public  AccountModel(String username, String password, String name, String phone, String birthDay, String role, String email)
    {
        this.user = username;
        this.pass = password;
        this.name = name;
        this.phone = phone;
        this.birthDay = birthDay;
        this.role = role;
        this.linkAvt = "/";
        this.status = "normal";
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(String birthDay) {
        this.birthDay = birthDay;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
