package com.shop.project.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.shop.project.service.MediaService;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private MediaService mediaService;
    

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");

        String logoPath = "file:".concat(mediaService.getLogoDir()).concat("\\");
        registry.addResourceHandler("/media/logo/**").addResourceLocations(logoPath);

        String profilePicDir = "file:".concat(mediaService.getProfilePicDir()).concat("\\");
        registry.addResourceHandler("/media/profile/**").addResourceLocations(profilePicDir);
    }
}

