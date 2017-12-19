package ru.sbt.exchange.domain;

import java.io.Serializable;
import java.util.Date;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

public class PeriodInfoImpl implements PeriodInfo, Serializable {
    private final Date startDate;
    private final int periodLengthInSec;
    private final int endPeriodNumber;

    public PeriodInfoImpl(Date startDate, int periodLengthInSec, int endPeriodNumber) {
        this.startDate = startDate;
        this.periodLengthInSec = periodLengthInSec;
        this.endPeriodNumber = endPeriodNumber;
    }

    @Override
    public int getPeriodLengthInSec() {
        return periodLengthInSec;
    }

    @Override
    public int getCurrentPeriodNumber() {
        return getPeriodNumber(new Date());
    }

    @Override
    public int getSecondsToNextPeriod() {
        return getSecondsToNextPeriod(new Date());
    }

    @Override
    public int getEndPeriodNumber() {
        return endPeriodNumber;
    }

    int getSecondsToNextPeriod(Date now) {
        int secondsToNextPeriodFromFirst = (getPeriodNumber(now) + 1) * periodLengthInSec;
        return secondsToNextPeriodFromFirst - getDiffInSeconds(now);
    }

    int getPeriodNumber(Date now) {
        int diffInSeconds = getDiffInSeconds(now);
        return diffInSeconds / periodLengthInSec;
    }

    private int getDiffInSeconds(Date now) {
        return (int) MILLISECONDS.toSeconds(now.getTime() - startDate.getTime());
    }
}