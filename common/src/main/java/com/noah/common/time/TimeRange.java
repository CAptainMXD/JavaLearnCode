package com.noah.common.time;

public class TimeRange {

    /** 开始时间(inclusive) */
    public final long startTime;

    /** 开始时间(inclusive) */
    public final long endTime;

    public TimeRange(long startTime, long endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public boolean isBetweenTimeRange(long curTimeMs) {
        return curTimeMs >= startTime && curTimeMs <= endTime;
    }

    @Override
    public String toString() {
        return "TimeRange{" +
                "startTime=" + startTime + " " +TimeUtils.formatTimeToDate(startTime)+
                ", endTime=" + endTime + " " +TimeUtils.formatTimeToDate(endTime)+
                '}';
    }
}
