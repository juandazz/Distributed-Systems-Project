package com.example.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class StartupListener {

    @Value("${server.port:8080}")
    private int serverPort;

    @Value("${node.id:unknown}")
    private String nodeId;

    @EventListener(ApplicationReadyEvent.class)
    public void onStartup() {
        System.out.println("\nSystem Status:");
        System.out.println("-------------");
        System.out.println("Node ID: " + nodeId);
        System.out.println("REST API: http://localhost:" + serverPort);
        System.out.println("SOAP Service: http://localhost:8090/fileService");
        System.out.println("Node RMI Port: 1098");
        System.out.println("File Services RMI Port: 1099");
        System.out.println("-------------\n");
        System.out.println("Storage Node system is running!");
    }
}