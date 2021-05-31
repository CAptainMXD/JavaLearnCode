package com.noah.common.time;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.function.BiConsumer;
import java.util.function.Consumer;


/**
 * @author mxd
 *
 * timer队列
 */
public class TimerQueue<T> {
    /** id 分配*/
    private long idAlloc = 0;
    /** timer队列*/
    private Queue<Timer> timers = new PriorityQueue<>();

    /** timerid -> timer*/
    private Map<Long, Timer> id2Timer = new HashMap<>();

    public long createTimer(long delay, long period, BiConsumer<Long,T> consumer, T param) {
        if (delay <= 0L) {
            delay = 20L;
        }

        Timer<T> timer = new Timer(++idAlloc);
        timer.canceled = false;
        timer.expiredTime = System.currentTimeMillis() + delay;
        timer.periodTime = period;
        timer.consumer = consumer;
        timer.param = param;

        timers.add(timer);
        id2Timer.put(timer.id, timer);

        return timer.id;
    }

    public void cancelTimer(long timerId) {
        Timer timer = id2Timer.get(timerId);
        if (timer != null) {
            timer.canceled = true;
        }
    }

    public void update(long now) {
        while (!timers.isEmpty()) {
            Timer timer = timers.peek();
            if (now < timer.expiredTime) {
                break;
            }

            timers.poll();

            if (!timer.canceled) {
                timer.execute();

                if (timer.periodTime > 0 && !timer.canceled) {
                    timer.expiredTime = now + timer.periodTime;
                    timers.add(timer);
                } else {
                    id2Timer.remove(timer.id);
                }
            } else {
                id2Timer.remove(timer.id);
            }
        }
    }

    public long getTimeLeft(long timerId, long now) {
        Timer timer = id2Timer.get(timerId);
        return timer == null ? -1 : timer.getTimeLeft(now);
    }

    public static void main(String[] args) throws InterruptedException {
        BiConsumer<Long,Long> consumer = (a,b)->{
            System.out.println("TimerQueue.main b = " + b);
        };

        TimerQueue<Long> timerQueue = new TimerQueue<>();

        timerQueue.createTimer(2000L,0,consumer,10l);
        while (true){
            Thread.sleep(200L);
            timerQueue.update(System.currentTimeMillis());
        }
    }

}

