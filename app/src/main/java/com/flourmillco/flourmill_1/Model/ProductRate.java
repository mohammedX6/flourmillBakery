package com.flourmillco.flourmill_1.Model;

public class ProductRate {

    private int Id;
    private int value;
    private String rateDate;
    private String rateText;
    private int productID;
    private int administratorID;
    private int bakeryID;


    public ProductRate(int id, int value, String rateDate, String rateText, int productID, int administratorID, int bakeryID) {
        Id = id;
        this.value = value;
        this.rateDate = rateDate;
        this.rateText = rateText;
        this.productID = productID;
        this.administratorID = administratorID;
        this.bakeryID = bakeryID;
    }

    public ProductRate() {
    }

    public String getRateText() {
        return rateText;
    }

    public void setRateText(String rateText) {
        this.rateText = rateText;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getRateDate() {
        return rateDate;
    }

    public void setRateDate(String rateDate) {
        this.rateDate = rateDate;
    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public int getAdministratorID() {
        return administratorID;
    }

    public void setAdministratorID(int administratorID) {
        this.administratorID = administratorID;
    }

    public int getBakeryID() {
        return bakeryID;
    }

    public void setBakeryID(int bakeryID) {
        this.bakeryID = bakeryID;
    }
}
