package com.example.demo;

import org.springframework.stereotype.Component;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

@Component
public class Receiver {

    String fileName;
    int fileChuncksNum;
    List<Byte> bytes=new ArrayList<>();

    private CountDownLatch latch = new CountDownLatch(1);

    public void receiveMessage(String message) {
        System.out.println("Received <" + message + ">");
        fileName=message;
        latch.countDown();
    }
    public void receiveMessage(int message) {
        System.out.println("Received int <" + message + ">");
        latch.countDown();
    }


    public void receiveMessage(byte[] message) throws IOException {



        System.out.println("Received byte <" +fileName + ">");
        BufferedOutputStream raf = new BufferedOutputStream(new FileOutputStream(fileName));

        raf.write(message);
        raf.close();


        latch.countDown();
    }


    public CountDownLatch getLatch() {
        return latch;
    }

}