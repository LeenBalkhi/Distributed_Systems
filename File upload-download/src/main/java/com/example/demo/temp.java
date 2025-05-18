package com.example.demo;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

public class temp {

    ArrayList<Byte> bytes=new ArrayList<>();

    public static void main(String[] args) throws Exception
    {
        try {
            combine();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    static void combine()throws IOException {
        BufferedOutputStream raf = new BufferedOutputStream(new FileOutputStream("Untitled2.png"));
        long numSplits = 11; //from user input, extract it from args


        int maxReadBufferSize = 8 * 1024; //8KB
        for(int destIx=1; destIx <= numSplits; destIx++) {
            RandomAccessFile bw = new RandomAccessFile("split." + destIx, "r");
            readWrite( bw,raf,bw.length());
            bw.close();
        }
        raf.close();
    }
    static void divide() throws IOException {
        RandomAccessFile raf = new RandomAccessFile("Untitled.png", "r");
        long numSplits = 10; //from user input, extract it from args
        long sourceSize = raf.length();
        long bytesPerSplit = sourceSize/numSplits ;
        long remainingBytes = sourceSize % numSplits;

        int maxReadBufferSize = 8 * 1024; //8KB
        for(int destIx=1; destIx <= numSplits; destIx++) {
            BufferedOutputStream bw = new BufferedOutputStream(new FileOutputStream("split."+destIx));
            if(bytesPerSplit > maxReadBufferSize) {
                long numReads = bytesPerSplit/maxReadBufferSize;
                long numRemainingRead = bytesPerSplit % maxReadBufferSize;
                for(int i=0; i<numReads; i++) {
                    readWrite(raf, bw, maxReadBufferSize);
                }
                if(numRemainingRead > 0) {
                    readWrite(raf, bw, numRemainingRead);
                }
            }else {
                readWrite(raf, bw, bytesPerSplit);
            }
            bw.close();

        }
        if(remainingBytes > 0) {
            BufferedOutputStream bw = new BufferedOutputStream(new FileOutputStream("split."+(numSplits+1)));
            readWrite(raf, bw, remainingBytes);
            bw.close();
        }
        raf.close();

    }
    static void readWrite(RandomAccessFile raf, BufferedOutputStream bw, long numBytes) throws IOException {
        byte[] buf = new byte[(int) numBytes];
        int val = raf.read(buf);
        if(val != -1) {
            bw.write(buf);
        }
    }
}
