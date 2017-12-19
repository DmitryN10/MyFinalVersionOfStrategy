package ru.sbt.exchange.client;

import ru.sbt.exchange.domain.ExchangeEvent;
import ru.sbt.exchange.domain.Order;
import ru.sbt.exchange.domain.Portfolio;
import ru.sbt.exchange.domain.instrument.Instrument;
import ru.sbt.exchange.domain.instrument.Instruments;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.lang.Math.pow;

public class MyAlgoStrategy implements AlgoStrategy {
    private static final double BUY_COEF = 0.9;
    private static final double SELL_COEF = 1.1;

    private static double bestFixPrice;
    private static double bestFloatPrice;
    private static double bestZeroPrice;
    private static double brokerPerсents;

    private static LinkedList<String> lastMyOrdersId = new LinkedList<>();

    @Override
    public void onEvent(ExchangeEvent event, Broker broker) {
        switch (event.getExchangeEventType()) {
            case STRATEGY_START:
                updatePricesOnStartStrategy(event, broker);
                brokerPerсents = broker.getMyPortfolio().getBrokerFeeInPercents() / 100;
                generateOrdersInStart(event, broker);
                break;

            case NEW_PERIOD_START:
                if (broker.getPeriodInfo().getCurrentPeriodNumber() != 0) {
                    updatePricesOnPeriodStar(event, broker);
                    broker.cancelOrderByIds(getLiveOrderIdList(broker));
                    generateOrdersInStart(event, broker);
                }
                break;

            case ORDER_NEW:
                generateCounterEvent(event, broker);
                break;
        }
    }

    private void generateOrdersInStart(ExchangeEvent event, Broker broker) {
        Portfolio myPortfolio = broker.getMyPortfolio();
        double money = (myPortfolio.getMoney() + myPortfolio.getAcceptedOverdraft()) / 3;

        Map<Instrument, Integer> countByInstrument = broker.getMyPortfolio().getCountByInstrument();
        Order buyOrderZ = Order
                .buy(Instruments.zeroCouponBond())
                .withPrice(bestZeroPrice * BUY_COEF)
                .withQuantity(((int) (money / (bestZeroPrice * BUY_COEF))) - 1)
                .order();
        broker.addOrder(buyOrderZ);

        Order sellOrderZ = Order
                .sell(Instruments.zeroCouponBond())
                .withPrice(bestZeroPrice * SELL_COEF)
                .withQuantity(countByInstrument.get(Instruments.zeroCouponBond()))
                .order();
        broker.addOrder(sellOrderZ);

        Order buyOrderF = Order
                .buy(Instruments.fixedCouponBond())
                .withPrice(bestFixPrice * BUY_COEF)
                .withQuantity(((int) (money / (bestFixPrice * BUY_COEF))) - 1)
                .order();
        broker.addOrder(buyOrderF);

        Order sellOrderF = Order
                .sell(Instruments.zeroCouponBond())
                .withPrice(bestFixPrice * SELL_COEF)
                .withQuantity(countByInstrument.get(Instruments.fixedCouponBond()))
                .order();
        broker.addOrder(sellOrderF);

        Order buyOrderFloat = Order
                .buy(Instruments.floatingCouponBond())
                .withPrice(bestFixPrice * BUY_COEF)
                .withQuantity(((int) (money / (bestFixPrice * BUY_COEF))) - 1)
                .order();
        broker.addOrder(buyOrderFloat);

        Order sellOrderFloat = Order
                .sell(Instruments.floatingCouponBond())
                .withPrice(bestFixPrice * SELL_COEF)
                .withQuantity(countByInstrument.get(Instruments.floatingCouponBond()))
                .order();
        broker.addOrder(sellOrderFloat);
    }

    private void generateCounterEvent(ExchangeEvent event, Broker broker) {
        Order newOrder = event.getOrder();
        String idNewOrder = newOrder.getOrderId();
        if (!lastMyOrdersId.isEmpty()) {
            if (idNewOrder != lastMyOrdersId.getFirst()) {
                switch (newOrder.getDirection()) {
                    case BUY: {
                        if (isProfitEventForSell(newOrder)) {
                            reactOnBuy(broker, newOrder);
                        }
                        break;
                    }
                    case SELL: {
                        if (isProfitEventForBuy(newOrder)) {
                            reactOnSell(broker, newOrder);
                        }
                        break;
                    }
                }
            } else lastMyOrdersId.removeFirst();
            lastMyOrdersId.add(idNewOrder);
        } else {
            lastMyOrdersId.add(idNewOrder);
        }
    }

    private boolean isProfitEventForSell(Order order) {
        String nameInstrument = order.getInstrument().getName();
        double price = order.getPrice();
        switch (nameInstrument) {
            case "zeroCouponBond":
                return price > bestZeroPrice;
            case "fixedCouponBond":
                return price > bestFixPrice;
            default:
                return price > bestFloatPrice;
        }
    }

    private boolean isProfitEventForBuy(Order order) {
        return !isProfitEventForSell(order);
    }

    private void reactOnBuy(Broker broker, Order newOrder) {
        Order myOrder = Order
                .sell(newOrder.getInstrument())
                .withPrice(newOrder.getPrice())
                .withQuantity(newOrder.getQuantity())
                .order();
        broker.addOrder(myOrder);
    }

    private void reactOnSell(Broker broker, Order newOrder) {
        Order myOrder = Order
                .buy(newOrder.getInstrument())
                .withPrice(newOrder.getPrice())
                .withQuantity(newOrder.getQuantity())
                .order();
        broker.addOrder(myOrder);
    }

    private void updatePricesOnPeriodStar(ExchangeEvent event, Broker broker) {
        int p = broker.getPeriodInfo().getCurrentPeriodNumber();
        bestFixPrice = bestPriceForFix(event, broker, p);
        bestFloatPrice = bestFixPrice;
        bestZeroPrice = bestPriceForZero(event, broker, p);
    }

    private void updatePricesOnStartStrategy(ExchangeEvent event, Broker broker) {
        bestFixPrice = bestPriceForFix(event, broker, 0);
        bestFloatPrice = bestFixPrice;
        bestZeroPrice = bestPriceForZero(event, broker, 0);
    }

    // ==== Utility methods =================================
    private List<String> getLiveOrderIdList(Broker broker) {
        return broker
                .getMyLiveOrders()
                .stream()
                .map(o -> o.getOrderId())
                .collect(Collectors.toList());
    }

    // ==== Price calculators =====================
    private double bestPriceForZero(ExchangeEvent event, Broker broker, int period) {
        final double i = broker.getMyPortfolio().getPeriodInterestRate() / 100;
        return 100 / (pow((1 + i), (5 - period)) + brokerPerсents);
    }

    private double bestPriceForFix(ExchangeEvent event, Broker broker, int period) {
        final double i = broker.getMyPortfolio().getPeriodInterestRate() / 100;
        return 100 * pow(1.1, (5 - period)) / (pow((1 + i), (5 - period)) + brokerPerсents);
    }


}