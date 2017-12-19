package ru.sbt.exchange.domain.instrument;

import java.util.Arrays;
import java.util.List;

public final class Instruments {
    private static final Bond zeroCouponBond = new Bond("zeroCouponBond", 100, 5, new Bond.Coupon(0));
    private static final Bond fixedCouponBond = new Bond("fixedCouponBond", 100, 5, new Bond.Coupon(10));
    private static final Bond floatingCouponBond = new Bond("floatingCouponBond", 100, 5, new Bond.Coupon(5, 15));

    public static Bond zeroCouponBond() {
        return zeroCouponBond;
    }

    public static Bond fixedCouponBond() {
        return fixedCouponBond;
    }

    public static Bond floatingCouponBond() {
        return floatingCouponBond;
    }

    public static List<Instrument> supportedInstruments() {
        return Arrays.<Instrument>asList(zeroCouponBond, fixedCouponBond, floatingCouponBond);
    }

    @SuppressWarnings("unchecked")
    public static <T extends Instrument> T findByName(String name) {
        for (Instrument instrument : supportedInstruments()) {
            if (instrument.getName().equals(name)) return (T) instrument;
        }

        throw new IllegalArgumentException("Incorrect instrument name " + name);
    }
}