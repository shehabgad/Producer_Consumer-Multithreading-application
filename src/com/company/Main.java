package com.company;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

// PLEASE NOTE THAT WE START THE PRIME NUMBERS FROM 2 NOT 1 SINCE WE DON'T CONSIDER 1 AS A PRIME NUMBER...


public class Main {
    public static MySemaphore producerCount;
    public static MySemaphore emptyCount;
    public  static BufferedWriter writer;
    public  static IntegerWrapper largestPrime = new IntegerWrapper(0);
    public static IntegerWrapper produced = new IntegerWrapper(0);

    public static ArrayList<String> produceAndConsume(int n, int bufferSize, String fileName)
    {
        largestPrime = new IntegerWrapper(0);
        produced = new IntegerWrapper(0);
        long startTime = System.currentTimeMillis();

        try {
            writer = new BufferedWriter(new FileWriter(fileName));
        } catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }
        producerCount = new MySemaphore(0);
        emptyCount = new MySemaphore(bufferSize);
        ArrayList<Integer> buffer = new ArrayList<Integer>();
        for (int i = 0; i< bufferSize; i++)
            buffer.add(0);

        Thread producerThread = new Thread(new Producer(buffer,bufferSize,n,producerCount,emptyCount,produced,largestPrime));
        Thread consumerThread = new Thread(new Consumer(buffer,bufferSize,n,producerCount,emptyCount,writer));
        producerThread.start();
        consumerThread.start();

        while (producerThread.isAlive() && consumerThread.isAlive());


        long stopTime =  System.currentTimeMillis();
        long timeTaken = stopTime - startTime;

        ArrayList<String> result = new ArrayList<String>();
        result.add(Integer.toString(largestPrime.x));
        result.add(Integer.toString(produced.x));
        result.add(Long.toString(timeTaken));

        return result;
    }
    public static void main(String[] args) {

        Box box = Box.createVerticalBox();
        final JFrame jframe = new JFrame("Main Form");
        jframe.setVisible(true);
        jframe.setSize(600,600);
        jframe.setLayout(new GridLayout(9,3,10,10));
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JLabel nLabel = new JLabel("N: ");
        jframe.add(nLabel);

        final JTextField nInput = new JTextField(20);
        jframe.add(nInput);

        JLabel bufferSizeLabel = new JLabel("Buffer size: ");
        jframe.add(bufferSizeLabel);

        final JTextField bufferSizeInput = new JTextField(20);
        jframe.add(bufferSizeInput);

        JLabel fileNameLabel = new JLabel("file name: ");
        jframe.add(fileNameLabel);

        final JTextField fileNameInput = new JTextField(20);
        jframe.add(fileNameInput);

        JButton produceBtn = new JButton("Produce");
        jframe.add(produceBtn);

        final JLabel itemsProducedLabel = new JLabel("items produced: ");
        jframe.add(itemsProducedLabel);

        final JLabel timeTakenLabel = new JLabel("time taken: ");
        jframe.add(timeTakenLabel);

        final JLabel largestPrimeLabel = new JLabel("Largest Prime:  ");
        jframe.add(largestPrimeLabel);
        largestPrimeLabel.setText("Largest Prime");

        produceBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String nstr = nInput.getText();
                String bufferSizeStr = bufferSizeInput.getText();
                String fileName = fileNameInput.getText();
                ArrayList<String> result;

                try {
                    int n = Integer.parseInt(nstr);
                    int bufferSize = Integer.parseInt(bufferSizeStr);
                    if (bufferSize <= 0 || n < 0)
                    {
                        System.out.println("wrong numbers in the input");
                        return;
                    }
                    result = produceAndConsume(n, bufferSize, fileName);
                } catch (Exception ex)
                {
                    System.out.println("Wrong input format");
                    return;
                }
                largestPrimeLabel.setText("Largest Prime: " + result.get(0));
                itemsProducedLabel.setText("items produced: " + result.get(1));
                timeTakenLabel.setText("time taken: " + result.get(2) + " ms");

            }
        });

    }
}
