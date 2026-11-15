package com.shop.project.service;

import java.io.IOException;
import java.nio.file.Path;

import org.springframework.web.multipart.MultipartFile;

public interface MediaService {
    
    String saveAirlineLogo(MultipartFile logo);

    String savePassengerProfilePicture(MultipartFile profilePicture);

    void copyFolder(Path source, Path destination) throws IOException;

    String getLogoDir();

    String getProfilePicDir();

    String resolveLogoAddress(String logoName);

    String resolveProfilePicAddress(String profilePicName);

    
}
