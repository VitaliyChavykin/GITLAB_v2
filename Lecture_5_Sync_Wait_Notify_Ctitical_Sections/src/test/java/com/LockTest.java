package com;

import com.mycompany.prepare.utils.Utils;
import org.junit.Test;

import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;

public class LockTest {

    private static volatile int counter = 0;
    private static ReentrantLock locker = new ReentrantLock();

    public static void change() {
        locker.lock();
        try {
        synchronized (LockTest.class) {
            counter++;
        }}
        finally {
            locker.unlock();
        }
    }

    private static final  Object object = new Object();

    public static void changeX() {
        locker.lock();
        try {
        synchronized (object) {
            counter++;
        }}
        finally {
            locker.unlock();
        }
    }

    @Test
    public void testSync() {
        new Thread(() -> {
            IntStream.range(0, 1000).forEach((x) -> change());
        }).start();
        new Thread(() -> {
            IntStream.range(0, 1000).forEach((x) -> changeX());
        }).start();


        Utils.sleep(1000);



        assertEquals(2 * 1000, counter);
    }
}
