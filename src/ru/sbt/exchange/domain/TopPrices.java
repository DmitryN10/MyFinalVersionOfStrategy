package ru.sbt.exchange.domain;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class TopPrices implements Serializable {
    private final Map<Double, Integer> buyPrices;
    private final Map<Double, Integer> sellPrices;

    public TopPrices(Map<Double, Integer> buyPrices, Map<Double, Integer> sellPrices) {
        this.buyPrices = buyPrices;
        this.sellPrices = sellPrices;
    }

    public Map<Double, Integer> getBuyPrices() {
        return buyPrices;
    }

    public Map<Double, Integer> getSellPrices() {
        return sellPrices;
    }
}
