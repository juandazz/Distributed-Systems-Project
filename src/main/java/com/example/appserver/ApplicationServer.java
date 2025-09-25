package com.example.appserver;

import com.example.appserver.soap.FileService;
import com.example.appserver.soap.FileServiceImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import javax.xml.ws.Endpoint;

@SpringBootApplication
public class ApplicationServer {
    private static final String SOAP_SERVICE_URL = "http://localhost:8088/fileservice";

    public static void main(String[] args) {
        SpringApplication.run(ApplicationServer.class, args);
    }

    @Bean(initMethod = "startService")
    public FileServiceImpl fileService() {
        FileServiceImpl service = new FileServiceImpl();
        Endpoint.publish(SOAP_SERVICE_URL, service);
        System.out.println("Servicio SOAP publicado en: " + SOAP_SERVICE_URL);
        return service;
    }
}