package com.shop.project.util;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.shop.project.service.MediaService;

@Component
public class MediaInitializer implements CommandLineRunner {

    @Autowired
    private MediaService mediaService;
    
    @Override
    public void run(String... args) {
        // Define source and destination paths
        Path LogoSource = Paths.get("src\\main\\resources\\static\\airlines");
        Path LogoDestination = Paths.get(mediaService.getLogoDir());
        Path ProfilePicSource = Paths.get("src\\main\\resources\\static\\profile");
        Path ProfilePicDestination = Paths.get(mediaService.getProfilePicDir());

        try {
            mediaService.copyFolder(LogoSource, LogoDestination);
            mediaService.copyFolder(ProfilePicSource, ProfilePicDestination);
            System.out.println("Folder contents copied successfully!");
        } catch (Exception e) {
            System.err.println("Error during folder copy: " + e.getMessage());
        }
    }

}
