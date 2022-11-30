package com.company;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Consumer implements Runnable{
    ArrayList<Integer> buffer;
    int bufferSize;
    int N;
    int out;
    MySemaphore producerCount;
    MySemaphore emptyCount;
    BufferedWriter writer;
    public Consumer(ArrayList<Integer> buffer, int bufferSize, int N, MySemaphore producerCount, MySemaphore emptyCount, BufferedWriter writer)
    {
        this.buffer = buffer;
        this.bufferSize = bufferSize;
        this.producerCount = producerCount;
        this.emptyCount = emptyCount;
        this.N = N;
        this.out = 0;
        this.writer = writer;
    }
    public void run()
    {
        do {
            if (producerCount.isZero())
                continue;
            int consumed = buffer.get(out % bufferSize);
            out++;
            if (consumed > N)
                break;

            try {
                String consumedStr = Integer.toString(consumed);
                writer.write(consumedStr + "\n");
            } catch (IOException e)
            {
                e.printStackTrace();
                return;
            }
            producerCount.decrement();
            emptyCount.increment();
        } while (true);

        try {
            writer.close();
        } catch (IOException e)
        {
            e.printStackTrace();
            return;
        }
    }
}
