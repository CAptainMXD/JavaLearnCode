package com.noah.common.time;

import java.time.LocalTime;

public class LocalTimeTest {
    public static void main(String[] args) {
        LocalTime localTime = LocalTime.parse("12:00");
        System.out.println(localTime.getHour());
        System.out.println(localTime.getMinute());
    }
}
