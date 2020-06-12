package com.flourmillco.flourmill_1.Model;

public class Wishlist {


    private int id;
    private int price;
    private String badgename;
    private String url;
    private int administratorId;
    private int BakeryId;
    private int productId;

    public Wishlist(int id, int price, String badgename, String url, int adminID, int bakeryId, int productid) {
        this.id = id;
        this.price = price;
        this.badgename = badgename;
        this.url = url;
        administratorId = adminID;
        BakeryId = bakeryId;
        productId = productid;
    }

    public Wishlist() {
    }

    public int getAdminID() {
        return administratorId;
    }

    public void setAdminID(int adminID) {
        administratorId = adminID;
    }

    public int getProductid() {
        return productId;
    }

    public void setProductid(int productid) {
        productId = productid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getBadgename() {
        return badgename;
    }

    public void setBadgename(String badgename) {
        this.badgename = badgename;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getBakeryId() {
        return BakeryId;
    }

    public void setBakeryId(int bakeryId) {
        BakeryId = bakeryId;
    }
}
