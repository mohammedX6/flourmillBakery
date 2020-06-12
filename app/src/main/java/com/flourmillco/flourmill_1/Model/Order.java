package com.flourmillco.flourmill_1.Model;

public class Order {
    private int id;
    private double totalTons;

    private double totalPayment;
    private String OrderComment;
    private int shipmentPrice;
    private String destination;
    private String customerName;
    private String order_Date;
    private int orderStatues;
    private int AdministratorID;
    private int bakeryID;
    private int TruckDriverID;

    public Order() {
    }

    public Order(int id, double totalTons, double totalPayment, String orderComment, int shipmentPrice, String destination, String customerName, String order_Date, int orderStatues, int administratorID, int bakeryID, int truckDriverID) {
        this.id = id;
        this.totalTons = totalTons;

        this.totalPayment = totalPayment;
        OrderComment = orderComment;
        this.shipmentPrice = shipmentPrice;
        this.destination = destination;
        this.customerName = customerName;
        this.order_Date = order_Date;
        this.orderStatues = orderStatues;
        AdministratorID = administratorID;
        this.bakeryID = bakeryID;
        TruckDriverID = truckDriverID;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getTotalTons() {
        return totalTons;
    }

    public void setTotalTons(double totalTons) {
        this.totalTons = totalTons;
    }

    public double getTotalPayment() {
        return totalPayment;
    }

    public void setTotalPayment(double totalPayment) {
        this.totalPayment = totalPayment;
    }

    public String getOrderComment() {
        return OrderComment;
    }

    public void setOrderComment(String orderComment) {
        OrderComment = orderComment;
    }

    public int getShipmentPrice() {
        return shipmentPrice;
    }

    public void setShipmentPrice(int shipmentPrice) {
        this.shipmentPrice = shipmentPrice;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getOrder_Date() {
        return order_Date;
    }

    public void setOrder_Date(String order_Date) {
        this.order_Date = order_Date;
    }

    public int getOrderStatues() {
        return orderStatues;
    }

    public void setOrderStatues(int orderStatues) {
        this.orderStatues = orderStatues;
    }

    public int getAdministratorID() {
        return AdministratorID;
    }

    public void setAdministratorID(int administratorID) {
        AdministratorID = administratorID;
    }

    public int getBakeryID() {
        return bakeryID;
    }

    public void setBakeryID(int bakeryID) {
        this.bakeryID = bakeryID;
    }

    public int getTruckDriverID() {
        return TruckDriverID;
    }

    public void setTruckDriverID(int truckDriverID) {
        TruckDriverID = truckDriverID;
    }
}
