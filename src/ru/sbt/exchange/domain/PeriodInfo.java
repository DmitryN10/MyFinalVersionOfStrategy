package ru.sbt.exchange.domain;

/**
 * First period number is 0. Each PeriodLengthInSec it grows by 1.
 * In the end of periods clients get premiums. It can be bond coupons, bond notional, etc
 * ThreadSafe. Each method always returns actual value. For example, you should not reQuery PeriodInfo to get actual value for getSecondsToNextPeriod()
 */
public interface PeriodInfo {
    int getPeriodLengthInSec();

    int getSecondsToNextPeriod();

    int getCurrentPeriodNumber();

    int getEndPeriodNumber();
}
