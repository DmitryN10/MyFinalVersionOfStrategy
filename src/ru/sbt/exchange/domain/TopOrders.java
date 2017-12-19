package ru.sbt.exchange.domain;

import java.io.Serializable;
import java.util.List;

public class TopOrders implements Serializable {
    private final List<Order> buyOrders;
    private final List<Order> sellOrders;

    public TopOrders(List<Order> buyOrders, List<Order> sellOrders) {
        this.buyOrders = buyOrders;
        this.sellOrders = sellOrders;
    }

    public List<Order> getBuyOrders() {
        return buyOrders;
    }

    public List<Order> getSellOrders() {
        return sellOrders;
    }
}
