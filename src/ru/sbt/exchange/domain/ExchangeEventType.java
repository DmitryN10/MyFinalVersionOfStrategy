package ru.sbt.exchange.domain;

public enum ExchangeEventType {
    ORDER_NEW,
    ORDER_CANCEL,
    ORDER_EVICT,
    ORDER_EXECUTION,

    NEW_PERIOD_START,
    STRATEGY_START
}