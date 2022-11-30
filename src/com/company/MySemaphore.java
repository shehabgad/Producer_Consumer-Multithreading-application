package com.company;

import java.util.concurrent.Semaphore;

public class MySemaphore {
    private int count;
    public MySemaphore(int count)
    {
        this.count = count;
    }
    public void increment()
    {
        synchronized (this)
        {
            count++;
        }
    }

    public void decrement()
    {
        synchronized (this)
        {
            count--;
        }
    }

    public boolean isZero()
    {
        synchronized (this)
        {
            return (count == 0);
        }
    }
}
