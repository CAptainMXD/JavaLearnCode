package com.noah.common.time;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtils {
    /** 秒 */
    public static final long SEC = 1000L;
    /** 分 */
    public static final long MIN = 60 * SEC;
    /** 小时 */
    public static final long HOUR = 60 * MIN;
    /** 天 */
    public static final long DAY = 24 * HOUR;
    /** 年 */
    public static final long YEAR = 365 * DAY;
    /** 十年(最大持续时间，可以当永久时间用，游戏运营不了十年的) */
    public static final long TEN_YEAR = 10 * YEAR;

    /** 默认日期格式 */
    public static final String DEFAULT_PATTERN = "yyyy-MM-dd HH:mm:ss";

    public static String formatTimeToDate(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_PATTERN);
        Date date = new Date();
        date.setTime(time);
        return sdf.format(date);
    }
}
