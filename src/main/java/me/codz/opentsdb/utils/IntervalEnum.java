package me.codz.opentsdb.utils;

/**
 * <p>Created with IDEA
 * <p>Author: laudukang
 * <p>Date: 2019/10/17
 * <p>Time: 16:32
 * <p>Version: 1.0
 */
public enum IntervalEnum {
    ONE_MINUTE("1m"),
    ONE_HOUR("1h"),
    ONE_DAY("1d");

    private String interval;

    IntervalEnum(String interval) {
        this.interval = interval;
    }

    public String getInterval() {
        return interval;
    }

    @Override
    public String toString() {
        return getInterval();
    }
}
