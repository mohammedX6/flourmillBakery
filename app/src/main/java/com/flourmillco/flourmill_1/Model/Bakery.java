package com.flourmillco.flourmill_1.Model;


public class Bakery {

    private int id;
    private String username;
    private String password;
    private String email;
    private String birthdate;
    private long nationalid;
    private String phoneNumber;
    private String JobNumber;
    private String address;
    private double latitude;
    private double longitude;

    public Bakery(String username, String password, String email, String birthdate, long nationalid, String PhoneNumber, String jobNumber, String address, double latitude, double longitude) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.birthdate = birthdate;
        this.nationalid = nationalid;
        phoneNumber = PhoneNumber;
        JobNumber = jobNumber;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Bakery(int id, String username, String password, String email, String birthdate, int nationalid, String PhoneNumber, String jobNumber, String address, double latitude, double longitude) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.birthdate = birthdate;
        this.nationalid = nationalid;
        phoneNumber = PhoneNumber;
        JobNumber = jobNumber;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public long getNationalid() {
        return nationalid;
    }

    public void setNationalid(long nationalid) {
        this.nationalid = nationalid;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String PhoneNumber) {
        phoneNumber = PhoneNumber;
    }

    public String getJobNumber() {
        return JobNumber;
    }

    public void setJobNumber(String jobNumber) {
        JobNumber = jobNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
