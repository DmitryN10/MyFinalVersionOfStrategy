package ru.sbt.exchange.domain;

import ru.sbt.exchange.domain.instrument.Instrument;

import java.io.Serializable;

import static java.lang.Double.isInfinite;
import static java.lang.Double.isNaN;
import static java.lang.Math.max;
import static java.util.Objects.requireNonNull;
import static ru.sbt.exchange.domain.Direction.BUY;
import static ru.sbt.exchange.domain.Direction.SELL;
import static ru.sbt.exchange.domain.OrderIdGenerator.generateOrderId;

public class Order implements Serializable {
    private final String orderId;
    private final Instrument instrument;
    private final Direction direction;
    private final double price;
    private final int quantity;

    public static OrderBuilder buy(Instrument instrument) {
        return OrderBuilder.buy(instrument);
    }

    public static OrderBuilder sell(Instrument instrument) {
        return OrderBuilder.sell(instrument);
    }

    public static Order periodEnd(double moneyDiff, int period) {
        return new Order(moneyDiff, period);
    }

    public static Order order(String orderId, Instrument instrument, Direction direction, double price, int quantity) {
        return new Order(orderId, instrument, direction, price, quantity);
    }

    Order(String orderId, Instrument instrument, Direction direction, double price, int quantity) {
        this.orderId = requireNonNull(orderId, "Order id is null");
        this.instrument = requireNonNull(instrument, "Order instrument is null");
        this.direction = requireNonNull(direction, "Order direction is null");
        this.price = requireGreaterThenZero(price, "price");
        this.quantity = requireGreaterThenZero(quantity, "quantity");
    }

    Order(double moneyDiff, int period) {
        this.orderId = "period";
        this.price = moneyDiff;
        this.quantity = period;
        this.instrument = null;
        this.direction = null;
    }

    private <T extends Number> T requireGreaterThenZero(T value, String paramName) {
        if (value.doubleValue() < 0) {
            throw new IllegalArgumentException("Order " + paramName + " should be >= 0");
        }
        return value;
    }

    public static boolean incorrectOrder(Order order) {
        return order == null || isNaN(order.getPrice()) || isInfinite(order.getPrice()) || order.getPrice() < 0 || order.getQuantity() <= 0;
    }

    public Instrument getInstrument() {
        return instrument;
    }

    public Direction getDirection() {
        return direction;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public double calcVolume() {
        return price * quantity;
    }

    public String getOrderId() {
        return orderId;
    }

    public boolean matches(Order order) {
        return !isClosed()
                && !order.isClosed()
                && direction != order.getDirection()
                && (order.getPrice() == 0 || direction == BUY ? price >= order.getPrice() : price <= order.getPrice());
    }

    public boolean isClosed() {
        return quantity == 0;
    }

    public Order minus(Order another) {
        return new Order(orderId, instrument, direction, price, max(0, quantity - another.getQuantity()));
    }

    public Order withPrice(Order order) {
        return withPrice(order.getPrice());
    }

    public Order withPrice(double newPrice) {
        return new Order(orderId, instrument, direction, newPrice, quantity);
    }

    public Order withQuantity(int newQuantity) {
        return new Order(orderId, instrument, direction, price, newQuantity);
    }

    public Order withId(String orderId) {
        return new Order(orderId, instrument, direction, price, quantity);
    }

    public Order withNewId() {
        return new Order(generateOrderId(), instrument, direction, price, quantity);
    }

    public Order opposite() {
        return new Order(generateOrderId(), instrument, direction == BUY ? SELL : BUY, price, quantity);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Order)) return false;

        Order order = (Order) o;

        if (Double.compare(order.price, price) != 0) return false;
        if (quantity != order.quantity) return false;
        if (direction != order.direction) return false;
        if (!instrument.equals(order.instrument)) return false;
        if (!orderId.equals(order.orderId)) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = orderId.hashCode();
        result = 31 * result + instrument.hashCode();
        result = 31 * result + direction.hashCode();
        temp = Double.doubleToLongBits(price);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + quantity;
        return result;
    }

    @Override
    public String toString() {
        if (instrument == null) return "PERIOD=" + quantity + ", PnL=" + price;
        return "d=" + direction + ", i=" + instrument.getName() + ", p=" + price + ", q=" + quantity + ", id=" + orderId;
    }
}