package com.example.demo;

import java.io.*;
import java.nio.file.*;
import java.util.zip.*;

/**
 * This Java program demonstrates how to compress a file in ZIP format.
 *
 * @author www.codejava.net
 */
public class ZipFile {

    private static void zipFile(String filePath,byte[] bytes) {
        try {
            File file = new File(filePath);
            String zipFileName = file.getName().concat(".zip");

            FileOutputStream fos = new FileOutputStream(zipFileName);
            ZipOutputStream zos = new ZipOutputStream(fos);

            zos.putNextEntry(new ZipEntry(file.getName()));

            zos.write(bytes, 0, bytes.length);
            zos.closeEntry();
            zos.close();

        } catch (FileNotFoundException ex) {
            System.err.format("The file %s does not exist", filePath);
        } catch (IOException ex) {
            System.err.println("I/O error: " + ex);
        }
    }

    public static void main(String[] args) throws IOException {
        RandomAccessFile bw = new RandomAccessFile("MBA_HRM103_HW_F20_C4_mohammad_138086.docx", "r");
        byte [] bytes=new byte[(int) bw.length()];
        int v=bw.read(bytes);
        zipFile("MBA_HRM103_HW_F20_C4_mohammad_138086.docx",bytes);
    }
}