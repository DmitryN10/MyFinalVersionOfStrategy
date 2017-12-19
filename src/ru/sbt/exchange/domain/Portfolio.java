package ru.sbt.exchange.domain;

import ru.sbt.exchange.domain.instrument.Instrument;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.joining;

public class Portfolio implements Serializable {
    private final Map<Instrument, Integer> countByInstrument;
    private double money;

    private final double acceptedOverdraft;
    private final double periodInterestRate; //range [0-100]
    private final double brokerFeeInPercents; //range [0-100]

    public Portfolio(Map<Instrument, Integer> countByInstrument, double money,
                     double acceptedOverdraft, double periodInterestRate, double brokerFeeInPercents) {
        this.acceptedOverdraft = acceptedOverdraft;
        this.periodInterestRate = periodInterestRate;
        this.brokerFeeInPercents = brokerFeeInPercents;
        this.countByInstrument = countByInstrument;
        this.money = money;
    }

    public double getAcceptedOverdraft() {
        return acceptedOverdraft;
    }

    public double getPeriodInterestRate() {
        return periodInterestRate;
    }

    public double getBrokerFeeInPercents() {
        return brokerFeeInPercents;
    }

    public Map<Instrument, Integer> getCountByInstrument() {
        return countByInstrument;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public boolean isDefaulter() {
        return money < -acceptedOverdraft;
    }

    public Portfolio prototype() {
        return new Portfolio(new LinkedHashMap<>(countByInstrument), money, acceptedOverdraft, periodInterestRate, brokerFeeInPercents);
    }

    @Override
    public String toString() {
        return "portfolio: " + countByInstrument + " | " + money;
    }

    public String toJson() {
        return "{" +
                "\"countByInstrument\": {" + countByInstrument.entrySet().stream().map(e -> "\"" + e.getKey().getName() + "\":" + e.getValue()).collect(joining(", ")) + "}" +
                ", \"money\":" + money +
                ", \"acceptedOverdraft\":" + acceptedOverdraft +
                ", \"periodInterestRate\":" + periodInterestRate +
                ", \"brokerFeeInPercents\":" + brokerFeeInPercents +
                '}';
    }
}