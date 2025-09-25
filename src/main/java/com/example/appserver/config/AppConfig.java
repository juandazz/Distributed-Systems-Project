package com.example.appserver.config;

import com.example.appserver.soap.FileService;
import com.example.appserver.soap.FileServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import javax.xml.ws.Endpoint;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;

@Configuration
public class AppConfig {
    private static final String SOAP_SERVICE_URL = "http://localhost:8088/fileservice";

    @Bean
    public FileService fileService() {
        return new FileServiceImpl();
    }

    @EventListener(ApplicationStartedEvent.class)
    public void init() {
        // Publicar el servicio SOAP después de que la aplicación haya iniciado completamente
        FileServiceImpl service = (FileServiceImpl) fileService();
        Endpoint.publish(SOAP_SERVICE_URL, service);
        System.out.println("Servicio SOAP publicado en: " + SOAP_SERVICE_URL);
    }
}