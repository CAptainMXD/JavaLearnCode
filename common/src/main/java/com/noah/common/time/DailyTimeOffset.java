package com.noah.common.time;

import com.noah.common.utils.StringUtils;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DailyTimeOffset implements TimeOffSet{

    public static final DateTimeFormatter HH_MM_SS = DateTimeFormatter.ofPattern("HH:mm:ss");

    public static final DateTimeFormatter HH_MM = DateTimeFormatter.ofPattern("HH:mm");

    private final LocalTime time;

    public DailyTimeOffset(int hour, int min) {
        this(hour, min, 0);
    }

    public DailyTimeOffset(int hour, int min, int sec) {
        this(LocalTime.of(hour, min, sec));
    }

    public DailyTimeOffset(LocalTime time) {
        this.time = time;
    }


    @Override
    public long offSet() {
        return time.toSecondOfDay() * TimeUtils.SEC;
    }

    /**
     * 从配置中解析出小时和分钟
     * @param confParam 表格配置，HH:mm 或 HH:mm:ss
     * @return HourAndMin
     */
    public static DailyTimeOffset parseFromConf(String confParam) throws Exception {
        int count = StringUtils.countMatches(confParam, ":");
        if (count == 1){
            // count == 1 表示分为两部分
            return new DailyTimeOffset(LocalTime.parse(confParam, HH_MM));
        } else if (count == 2){
            // count == 2 表示分为三部分
            return new DailyTimeOffset(LocalTime.parse(confParam, HH_MM_SS));
        } else {
            throw new Exception("Unsupported LocalTime format " + confParam);
        }
    }

    @Override
    public String toString() {
        return "DailyTimeOffset{" +
                "time=" + time +
                '}';
    }
}
