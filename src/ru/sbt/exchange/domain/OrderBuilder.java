package ru.sbt.exchange.domain;

import ru.sbt.exchange.domain.instrument.Instrument;

import static ru.sbt.exchange.domain.Direction.BUY;
import static ru.sbt.exchange.domain.Direction.SELL;
import static ru.sbt.exchange.domain.OrderIdGenerator.generateOrderId;

public class OrderBuilder {
    private final Instrument instrument;
    private final Direction direction;
    private double price;
    private int quantity;
    private String id;

    public static OrderBuilder buy(Instrument instrument) {
        return new OrderBuilder(instrument, BUY);
    }

    public static OrderBuilder sell(Instrument instrument) {
        return new OrderBuilder(instrument, SELL);
    }

    private OrderBuilder(Instrument instrument, Direction direction) {
        this.instrument = instrument;
        this.direction = direction;
    }

    //you can omit price setting to force order to be executed by opposite orders in order book
    public OrderBuilder withPrice(double price) {
        this.price = price;
        return this;
    }

    public OrderBuilder withQuantity(int quantity) {
        this.quantity = quantity;
        return this;
    }

    //optional. id will be generated
    public OrderBuilder withId(String id) {
        this.id = id;
        return this;
    }

    public Order order() {
        return new Order(id == null ? generateOrderId() : id, instrument, direction, price, quantity);
    }
}
