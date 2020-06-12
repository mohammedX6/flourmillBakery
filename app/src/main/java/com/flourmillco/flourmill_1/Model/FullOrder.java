package com.flourmillco.flourmill_1.Model;

import java.util.List;

public class FullOrder {

    private final Order order;
    private final List<OrderProducts> orderProducts;

    public FullOrder(Order order, List<OrderProducts> orderProducts) {
        this.order = order;
        this.orderProducts = orderProducts;
    }
}
