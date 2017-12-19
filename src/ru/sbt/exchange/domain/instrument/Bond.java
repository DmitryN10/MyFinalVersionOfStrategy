package ru.sbt.exchange.domain.instrument;

public class Bond extends NamedInstrument {
    private final double nominal;
    private final int maturityPeriod;
    private final Coupon couponInPercents;

    public Bond(String name, double nominal, int maturityPeriod, Coupon couponInPercents) {
        super(name);
        this.nominal = nominal;
        this.maturityPeriod = maturityPeriod;
        this.couponInPercents = couponInPercents;
    }

    public double getNominal() {
        return nominal;
    }

    public int getMaturityPeriod() {
        return maturityPeriod;
    }

    public Coupon getCouponInPercents() {
        return couponInPercents;
    }

    public static class Coupon {
        private final double min;
        private final double max;

        public Coupon(double value) {
            this(value, value);
        }

        public Coupon(double min, double max) {
            if (min > max) throw new IllegalArgumentException("Coupon.min should be lower then max");
            this.min = checkRange(min);
            this.max = checkRange(max);
        }

        private double checkRange(double value) {
            if (value < 0 || value > 100) throw new IllegalArgumentException("Coupon value should be from 0 to 100");
            return value;
        }

        public double getMin() {
            return min;
        }

        public double getMax() {
            return max;
        }

        public double withSeed(double seed) {
            if (seed < 0 || seed > 1) throw new IllegalArgumentException("Incorrect seed");
            return min + (max - min) * seed;
        }
    }
}