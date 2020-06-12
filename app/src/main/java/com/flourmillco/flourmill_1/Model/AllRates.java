package com.flourmillco.flourmill_1.Model;

public class AllRates {


    private int id;
    private int value;
    private String username;
    private String RateDate;
    private String rateText;


    public AllRates(int id, int value, String username, String rateDate, String rateText) {
        this.id = id;
        this.value = value;
        this.username = username;
        RateDate = rateDate;
        this.rateText = rateText;
    }

    public AllRates() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRateDate() {
        return RateDate;
    }

    public void setRateDate(String rateDate) {
        RateDate = rateDate;
    }

    public String getRateText() {
        return rateText;
    }

    public void setRateText(String rateText) {
        this.rateText = rateText;
    }
}
