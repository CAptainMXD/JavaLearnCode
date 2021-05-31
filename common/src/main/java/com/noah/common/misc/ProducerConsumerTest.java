package com.noah.common.misc;

class Resource {
    private int number = 0;
    private boolean isCreated = false;

    public synchronized void produce(){
        if (isCreated){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Resource.produce number = " + number);
        isCreated = true;
        number++;
        notify();
    }

    public synchronized void consume(){
        if (!isCreated){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Resource.consume number = " + number);
        isCreated = false;
        notify();
    }
}

class Produce implements Runnable{
    private Resource resource;

    public Produce(Resource resource) {
        this.resource = resource;
    }

    @Override
    public void run() {
        while (true){
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            resource.produce();
        }
    }
}

class Consumer implements Runnable{
    private Resource resource;

    public Consumer(Resource resource) {
        this.resource = resource;
    }

    @Override
    public void run() {
        while(true){
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            resource.consume();
        }
    }
}
public class ProducerConsumerTest {
    public static void main(String[] args) {
        Resource resource = new Resource();
        ExecutorProcessPool.getExecutorProcessPool().execute(new Produce(resource));
        ExecutorProcessPool.getExecutorProcessPool().execute(new Consumer(resource));
    }
}
