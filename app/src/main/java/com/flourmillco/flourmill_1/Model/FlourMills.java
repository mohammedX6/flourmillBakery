package com.flourmillco.flourmill_1.Model;

public class FlourMills {
    private String jobNumber;
    private String username;
    private String email;
    private String phoneNumber;
    private int id;


    public FlourMills(String jobNumber, String username, String email, String phoneNumber, int id) {
        this.jobNumber = jobNumber;
        this.username = username;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.id = id;
    }

    public FlourMills() {
    }

    public String getJobNumber() {
        return jobNumber;
    }

    public void setJobNumber(String jobNumber) {
        this.jobNumber = jobNumber;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}