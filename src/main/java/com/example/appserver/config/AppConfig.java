package com.example.appserver.config;

import com.example.appserver.soap.FileService;
import com.example.appserver.soap.FileServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    

    @Bean
    public FileService fileService() {
        return new FileServiceImpl();
    }
}