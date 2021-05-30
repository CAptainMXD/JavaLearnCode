package com.noah.common.misc;

public class LongHolder {
    private long value;

    public LongHolder() {
        this(0L);
    }

    public LongHolder(long value) {
        this.value = value;
    }

    public long getAndAdd(long addValue){
        long oldValue = value;
        this.value += addValue;
        return oldValue;
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "LongHolder{" +
                "value=" + value +
                '}';
    }
}
