package com.company;

import java.util.ArrayList;

public class Producer implements Runnable{
    ArrayList<Integer> buffer;
    int bufferSize;
    int N;
    int prime;
    int in;
    MySemaphore producerCount;
    MySemaphore emptyCount;
    IntegerWrapper produced;
    IntegerWrapper largerstPrime;


    public Producer(ArrayList<Integer> buffer, int bufferSize, int N, MySemaphore producerCount, MySemaphore emptyCount, IntegerWrapper produced,IntegerWrapper largerstPrime)
    {
        this.buffer = buffer;
        this.N = N;
        this.producerCount = producerCount;
        this.emptyCount = emptyCount;
        this.in = 0;
        this.prime = 1;
        this.bufferSize = bufferSize;
        this.largerstPrime = largerstPrime;
        this.produced = produced;
    }

    public boolean isPrime(int number)
    {
        for(int i = 2; i*i <= number; i++)
        {
            if (number % i == 0)
            {
                return false;
            }
        }
        return true;
    }

    public int nextPrime(int number)
    {
        for(int i = number + 1;  ;i++)
        {
            if (isPrime(i))
                return i;
        }
    }
    public void run()
    {
        do {
            if (emptyCount.isZero())
                continue;
            prime = nextPrime(prime);
            buffer.set(in % bufferSize,prime);
            in++;
            emptyCount.decrement();
            producerCount.increment();
            if (prime > N)
                break;
            this.largerstPrime.x = prime;
            this.produced.x++;
        } while (true);
    }
}
