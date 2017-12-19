package ru.sbt.exchange.domain;


import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * order          - order that has been put to OrderBook by some trader
 * exchangeEventType - event type
 * date - event creation date
 */
public class ExchangeEvent implements Serializable {
    private final ExchangeEventType exchangeEventType;
    private final Order order;
    private final Date date = new Date();

    public static ExchangeEvent event(Order order, ExchangeEventType exchangeEventType) {
        return new ExchangeEvent(order, exchangeEventType);
    }

    private ExchangeEvent(Order order, ExchangeEventType exchangeEventType) {
        this.order = order;
        this.exchangeEventType = exchangeEventType;
    }

    public Order getOrder() {
        return order;
    }

    public ExchangeEventType getExchangeEventType() {
        return exchangeEventType;
    }

    public Date getDate() {
        return date;
    }

    @Override
    public String toString() {
        return "{type=" + exchangeEventType +
                ", order=" + order +
                ", date=" + new SimpleDateFormat("dd.MM.yyyy HH:mm:ss.SSS").format(date) +
                '}';
    }
}