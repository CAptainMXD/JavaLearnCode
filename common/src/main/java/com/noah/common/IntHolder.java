package com.noah.common;

public class IntHolder {
    private int value;

    public IntHolder() {
        this(0);
    }

    public IntHolder(int value) {
        this.value = value;
    }

    public int getAndAdd(int addValue){
        int oldValue = value;
        this.value += addValue;
        return oldValue;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "IntHolder{" +
                "value=" + value +
                '}';
    }
}
