package com.flourmillco.flourmill_1.Model;

public class Basket {
    private int id;
    private String badgeName;
    private String url;
    private String TotalBadges;
    private String Destination;
    private String totalprice;


    public Basket(int id, String badgeName, String url, String totalBadges, String destination, String totalprice) {
        this.id = id;
        this.badgeName = badgeName;
        this.url = url;
        TotalBadges = totalBadges;
        Destination = destination;
        this.totalprice = totalprice;
    }

    public String getBadgeName() {
        return badgeName;
    }

    public void setBadgeName(String badgeName) {
        this.badgeName = badgeName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTotalprice() {
        return totalprice;
    }

    public void setTotalprice(String totalprice) {
        this.totalprice = totalprice;
    }

    public String getTotalBadges() {
        return TotalBadges;
    }

    public void setTotalBadges(String totalBadges) {
        TotalBadges = totalBadges;
    }

    public String getDestination() {
        return Destination;
    }

    public void setDestination(String destination) {
        Destination = destination;
    }
}
