package ru.sbt.exchange.client;

import ru.sbt.exchange.domain.Order;
import ru.sbt.exchange.domain.PeriodInfo;
import ru.sbt.exchange.domain.Portfolio;
import ru.sbt.exchange.domain.TopOrders;
import ru.sbt.exchange.domain.instrument.Instrument;

import java.util.List;

public interface Broker {
    /**
     * Send request to exchange to add new order
     *
     * @param order - order to Send to exchange
     */
    void addOrder(Order order);

    /**
     * Send request to exchange to cancel order by id
     *
     * @param id - order id
     */
    void cancelOrderById(String id);


    /**
     * Send request to exchange to cancel order by ids
     *
     * @param ids - order ids
     */
    void cancelOrderByIds(List<String> ids);

    /**
     * Send request to exchange to cancel all order with passed instrument
     *
     * @param instrument - order instrument
     */
    void cancelOrdersByInstrument(Instrument instrument);


    /**
     * @return info about top10 buy and top10 sell orders
     */
    TopOrders getTopOrders(Instrument instrument);

    /**
     * @return info about current client live orders
     */
    List<Order> getMyLiveOrders();

    /**
     * @return info about current client portfolio
     */
    Portfolio getMyPortfolio();

    /**
     * @return info about current exchange period
     */
    PeriodInfo getPeriodInfo();
}