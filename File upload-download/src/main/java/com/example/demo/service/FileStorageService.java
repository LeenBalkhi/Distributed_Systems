package com.example.demo.service;

import com.example.demo.Repository.FileRepository;
import com.example.demo.domain.FileModle;
import com.example.demo.exception.FileStorageException;
import com.example.demo.exception.MyFileNotFoundException;
import com.example.demo.property.FileStorageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.*;
import java.net.MalformedURLException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.concurrent.CountDownLatch;

@Service
public class FileStorageService {

    private final Path fileStorageLocation;

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    public FileStorageService(FileStorageProperties fileStorageProperties) {
        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir())
                .toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    public String storeFile(MultipartFile file) {
        // Normalize file name
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            // Check if the file's name contains invalid characters
            if(fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }

            // Copy file to the target location (Replacing existing file with the same name)
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            FileModle fileModle=new FileModle(fileName, ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/downloadFile/")
                    .path(fileName)
                    .toUriString());
            fileRepository.save(fileModle);

            return fileName;
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }
    public String storeFile( String fileName, byte []initialArray) {

        try {
            // Check if the file's name contains invalid characters
            if(fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }

            // Copy file to the target location (Replacing existing file with the same name)
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            InputStream targetStream = new ByteArrayInputStream(initialArray);
            Files.copy(targetStream, targetLocation, StandardCopyOption.REPLACE_EXISTING);

            FileModle fileModle=new FileModle(fileName, "http://localhost:8084/downloadFile/"+fileName);
            fileRepository.save(fileModle);

            return fileName;
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }


    public Resource loadFileAsResource(String fileName) {
        try {
            FileModle fileModle=fileRepository.findByName(fileName);
            if(fileModle==null)
                throw new MyFileNotFoundException("File not found " + fileName);
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if(resource.exists()) {
                return resource;
            } else {
                throw new MyFileNotFoundException("File not found " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new MyFileNotFoundException("File not found " + fileName, ex);
        }
    }
    String fileName;
    int fileChuncksNum=0;
    byte[] bytes=new byte[0];

    private CountDownLatch latch = new CountDownLatch(1);

    public void receiveMessage(String message) {
        System.out.println("Received <" + message + ">");
        fileName=message;
        latch.countDown();
    }
    public void receiveMessage(int message) {
        fileChuncksNum=message;
        bytes=new byte[0];
        System.out.println("Received int <" + message + ">");
        latch.countDown();
    }


    public void receiveMessage(byte[] message) throws IOException {

        if (bytes.length>0)
        { byte[] combined = new byte[message.length + bytes.length];

        int b=bytes.length;
            for (int i = 0; i < b; i++) {
                combined[i]=bytes[i];

            }
            for (int i = 0; i <message.length ; i++) {

                combined[i+b]=message[i];
            }


        bytes=combined;}
        else {
            bytes=message;
        }
        System.out.println(fileChuncksNum +"    "+bytes.length);

        if (fileChuncksNum>0){
            if(fileChuncksNum==1){
                this.storeFile(fileName,bytes);

                fileChuncksNum=0;
                bytes=new byte[0];
            }
            else {
                fileChuncksNum--;
            }
        }
        else {
            bytes=new byte[0];
        }




        latch.countDown();
    }


    public CountDownLatch getLatch() {
        return latch;
    }



}
