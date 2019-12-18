package jx.com.thread;

import java.util.Optional;
import java.util.stream.IntStream;

public class WaitSet {
    public static Object lock = new Object();

    public static void main(String[] args) {
        IntStream.rangeClosed(1, 10).forEach(i -> new Thread(String.valueOf(i)) {
            @Override
            public void run() {
                synchronized (lock) {
                    try {
                        Optional.of(Thread.currentThread().getName() + "will be wait set").ifPresent(System.out::println);
                        lock.wait();
                        Optional.of(Thread.currentThread().getName() + "will be leave set").ifPresent(System.out::println);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
