package com.noah.common.time;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtils {

    public static final String DEFAULT_PATTERN = "yyyy-MM-dd HH:mm:ss";

    public static String formatTimeToDate(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_PATTERN);
        Date date = new Date();
        date.setTime(time);
        return sdf.format(date);
    }
}
