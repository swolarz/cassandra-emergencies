package pl.put.srds.emergenciesclient;

import java.util.concurrent.atomic.AtomicInteger;

public final class RequestCounter {
    private static AtomicInteger counter;

    public static void increment() {
        counter.incrementAndGet();
    }

    public static void initialize() {
        counter = new AtomicInteger(0);

        new Thread("Counter"){
            public void run(){
                try {
                    while (true) {
                        Thread.sleep(1000);
                        System.out.println(String.format("Actual request/sec %d", counter.getAndSet(0)));
                    }
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}
