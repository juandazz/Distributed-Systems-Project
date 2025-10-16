package com.example.config;

import com.example.appserver.soap.FileServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import javax.xml.ws.Endpoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

@Configuration
public class WebServiceConfig {
    
    @Value("${soap.port:8090}")
    private int soapPort;

    @Autowired
    private FileServiceImpl fileService;

    @Bean
    public Endpoint soapEndpoint() {
        String soapUrl = "http://localhost:" + soapPort + "/fileService";
        Endpoint endpoint = Endpoint.create(fileService);
        endpoint.publish(soapUrl);
        System.out.println("SOAP Service published at: " + soapUrl);
        return endpoint;
    }
}