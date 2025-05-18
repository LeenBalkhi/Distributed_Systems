package com.example.demo.service;

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
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class FileStorageService {

    private final Path fileStorageLocation;



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
    public  void zipFile(String name,byte[] bytes) {
        try {
            String zipFileName = name.concat(".zip");

            FileOutputStream fos = new FileOutputStream(zipFileName);
            ZipOutputStream zos = new ZipOutputStream(fos);


            zos.putNextEntry(new ZipEntry(name));

            zos.write(bytes, 0, bytes.length);
            zos.closeEntry();
            zos.close();


        } catch (FileNotFoundException ex) {
            System.err.format("The file %s does not exist", name);
        } catch (IOException ex) {
            System.err.println("I/O error: " + ex);
        }
    }


    public Resource loadFileAsResource(MultipartFile file) {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            // Check if the file's name contains invalid characters
            if(fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }

            // Copy file to the target location (Replacing existing file with the same name)
            this.zipFile(file.getOriginalFilename(),file.getBytes());
            File f=new File(file.getOriginalFilename()+".zip");
            Path targetLocation = this.fileStorageLocation.resolve(fileName+".zip");
            InputStream targetStream = new FileInputStream(f);

            Files.copy(targetStream, targetLocation, StandardCopyOption.REPLACE_EXISTING);


            Resource resource = new UrlResource(targetLocation.toUri());

            targetStream.close();
           System.out.println( f.delete());
          //  targetLocation.toFile().delete();
            if(resource.exists()) {

                return resource;
            } else {
                throw new MyFileNotFoundException("File not found " + fileName);
            }
        } catch (Exception ex) {
            throw new MyFileNotFoundException("File not found " + fileName, ex);
        }
    }
    public Resource loadFileAsResource2(String fileName) {
        try {

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

}
