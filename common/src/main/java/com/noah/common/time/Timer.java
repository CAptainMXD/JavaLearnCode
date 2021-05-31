package com.noah.common.time;

import java.util.function.BiConsumer;

public class Timer<T> implements Comparable<Timer<T>> {
    public long id;
    public boolean canceled = false;
    public long expiredTime;
    public long periodTime;
    public BiConsumer<Long, T> consumer = null;
    public T param = null;

    Timer(long id) {
        this.id = id;
    }

    void execute() {
        if (consumer != null) {
            consumer.accept(id,param);
        }
    }

    long getTimeLeft(long now) {
        return Math.max(0, expiredTime - now);
    }

    @Override
    public int compareTo(Timer o) {
        return Long.compare(expiredTime, o.expiredTime);
    }
}
