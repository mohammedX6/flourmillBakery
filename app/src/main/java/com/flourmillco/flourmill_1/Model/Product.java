package com.flourmillco.flourmill_1.Model;

public class Product {


    private String url;
    private String badgeName;
    private String badgeType;
    private String badgeSize;
    private String productionDate;
    private String expireDate;
    private String usage;
    private String productDescription;
    private int price;
    private int id;


    public Product(String url, String badgeName, String badgeType, String badgeSize, String productionDate, String expireDate, String usage, String productDescription, int price, int id) {
        this.url = url;
        this.badgeName = badgeName;
        this.badgeType = badgeType;
        this.badgeSize = badgeSize;
        this.productionDate = productionDate;
        this.expireDate = expireDate;
        this.usage = usage;
        this.productDescription = productDescription;
        this.price = price;
        this.id = id;
    }

    public String getUrl() {
        return url;
    }


    public String getBadgeName() {
        return badgeName;
    }


    public String getProductionDate() {
        return productionDate;
    }


    public String getUsage() {
        return usage;
    }


    public String getProductDescription() {
        return productDescription;
    }


    public int getPrice() {
        return price;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}