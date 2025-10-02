package com.example.config;

import com.example.appserver.soap.FileServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import javax.xml.ws.Endpoint;
import org.springframework.beans.factory.annotation.Autowired;

@Configuration
public class WebServiceConfig {
    private static final String SOAP_URL = "http://localhost:8090/fileService";

    @Autowired
    private FileServiceImpl fileService;

    @Bean
    public Endpoint soapEndpoint() {
        Endpoint endpoint = Endpoint.create(fileService);
        endpoint.publish(SOAP_URL);
        System.out.println("SOAP Service published at: " + SOAP_URL);
        return endpoint;
    }
}