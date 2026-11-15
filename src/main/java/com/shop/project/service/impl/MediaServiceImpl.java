package com.shop.project.service.impl;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.shop.project.service.MediaService;



@Service
public class MediaServiceImpl implements MediaService{

    private final String logoDir;
    private final String profilePictureDir;

    public MediaServiceImpl(@Value("${spring.datasource.url}") String databaseDirPath){
        
        String resourcePath = getDatabaseDir(databaseDirPath);
        logoDir = resourcePath.concat("\\airlines");
        profilePictureDir = resourcePath.concat("\\profile");
        
    }
    

    @Override
    public String saveAirlineLogo(MultipartFile logo) {
         try {
            // Create upload directory if not exists
            Path uploadPath = Paths.get(logoDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            String originalFilename = logo.getOriginalFilename();
            String extension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
            String newFilename = getNewLogoId() + "." + extension;

            Path newFilePath = uploadPath.resolve(newFilename);
            logo.transferTo(newFilePath.toFile());

            return newFilename;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String savePassengerProfilePicture(MultipartFile profilePicture) {
        try {
            // Create upload directory if not exists
            Path uploadPath = Paths.get(profilePictureDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            String originalFilename = profilePicture.getOriginalFilename();
            String extension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
            String newFilename = getNewProfilePictureId() + "." + extension;

            Path newFilePath = uploadPath.resolve(newFilename);
            profilePicture.transferTo(newFilePath.toFile());

            return newFilename;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void copyFolder(Path source, Path destination) throws IOException {
        // Ensure the destination directory exists
        if (!Files.exists(destination)) {
            Files.createDirectories(destination);
        }

        // Walk through the file tree and copy each file and directory
        Files.walkFileTree(source, new SimpleFileVisitor<>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                // Create the corresponding directory in the destination
                Path targetDir = destination.resolve(source.relativize(dir));
                if (!Files.exists(targetDir)) {
                    Files.createDirectory(targetDir);
                }
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Path targetFile = destination.resolve(source.relativize(file));

                // Skip if the file already exists or has .csv or .txt extension
                String fileName = file.getFileName().toString().toLowerCase();
                if (Files.exists(targetFile) || fileName.endsWith(".csv") || fileName.endsWith(".txt")) {
                    return FileVisitResult.CONTINUE;
                }

                // Copy the file
                Files.copy(file, targetFile);
                return FileVisitResult.CONTINUE;
            }
        });
    }

    @Override
    public String getLogoDir() {
        return logoDir;
    }


    @Override
    public String getProfilePicDir() {
        return profilePictureDir;
    }

    
    @Override
    public String resolveLogoAddress(String logoName) {
        return "/media/logo/".concat(logoName);
    }


    @Override
    public String resolveProfilePicAddress(String profilePicName) {
        return "/media/profile/".concat(profilePicName);
    }
    

    private String getNewProfilePictureId(){
        int number;
        String  filePath = "src\\main\\resources\\static\\profile\\count.txt";
        try {
            // Read the number from the file
            Path path = Paths.get(filePath);
            String fileContent = Files.readString(path).trim();
            number = Integer.parseInt(fileContent);

            // Increment the number and write it back to the file
            int incrementedNumber = number + 1;
            Files.writeString(path, String.valueOf(incrementedNumber));

            // Convert the original number to hexadecimal and return it
            return Integer.toHexString(number);
        } catch (IOException e) {
            throw new RuntimeException("Error reading or writing the file", e);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("File content is not a valid integer", e);
        }
    }

    private String getNewLogoId(){
        int number;
        String  filePath = "src\\main\\resources\\static\\airlines\\count.txt";
        try {
            // Read the number from the file
            Path path = Paths.get(filePath);
            String fileContent = Files.readString(path).trim();
            number = Integer.parseInt(fileContent);

            // Increment the number and write it back to the file
            int incrementedNumber = number + 1;
            Files.writeString(path, String.valueOf(incrementedNumber));

            // Convert the original number to hexadecimal and return it
            return Integer.toHexString(number);
        } catch (IOException e) {
            throw new RuntimeException("Error reading or writing the file", e);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("File content is not a valid integer", e);
        }
    }

    private String getDatabaseDir(String databaseDirPath) {
        // Remove the prefix "jdbc:sqlite:" to get the file path
        String filePath = databaseDirPath.substring("jdbc:sqlite:\\".length());

        // Extract the directory path from the file path
        int lastSeparatorIndex = filePath.lastIndexOf("\\");
        filePath = filePath.substring(0, lastSeparatorIndex).concat("\\ImgDir");
        return filePath;
    }


}
