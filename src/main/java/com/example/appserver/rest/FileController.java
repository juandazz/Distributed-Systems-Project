package com.example.appserver.rest;

import com.example.appserver.interfaces.FileInterface;
import com.example.appserver.soap.FileServiceImpl;
import com.example.model.File;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

@RestController
@RequestMapping("/api/files")
public class FileController {

    private final FileServiceImpl soapFileService;
    private static final String RMI_SERVER = "localhost";
    private static final int RMI_PORT = 1099;
    private static final String RMI_SERVICE_NAME = "FileService";

    public FileController(FileServiceImpl soapFileService) {
        this.soapFileService = soapFileService;
    }

    @GetMapping("/test")
    public ResponseEntity<String> testEndpoint() {
        return ResponseEntity.ok("File service is running!");
    }

    // ======================= UPLOAD ===========================
    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(
            @RequestParam(value = "file", required = false) MultipartFile file,
            @RequestParam("username") String username,
            @RequestParam("path") String path,
            @RequestParam(value = "useRmi", defaultValue = "false") boolean useRmi) {
        try {
            // Para pruebas sin archivo
            if (file == null) {
                return ResponseEntity.ok("Endpoint working! Ready to receive files.");
            }

            byte[] fileData = file.getBytes();

            if (useRmi) {
                try {
                    Registry registry = LocateRegistry.getRegistry(RMI_SERVER, RMI_PORT);
                    FileInterface rmi = (FileInterface) registry.lookup(RMI_SERVICE_NAME);
                    File uploadedFile = rmi.uploadFile(fileData, path, file.getContentType(), Integer.parseInt(username));
                    return ResponseEntity.ok(uploadedFile);
                } catch (Exception e) {
                    System.err.println("Error connecting to RMI service: " + e.getMessage());
                    return ResponseEntity.status(500).body("Error connecting to RMI service: " + e.getMessage());
                }
            } else {
                // SOAP fallback - convertir byte[] a Base64
                String base64Data = java.util.Base64.getEncoder().encodeToString(fileData);
                String result = soapFileService.uploadFile(username, path, base64Data);
                return ResponseEntity.ok(result);
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al procesar el archivo: " + e.getMessage());
        }
    }

    // ======================= DOWNLOAD ===========================
    @GetMapping("/download")
    public ResponseEntity<byte[]> downloadFile(
            @RequestParam("username") String username,
            @RequestParam("path") String path,
            @RequestParam(value = "useRmi", defaultValue = "false") boolean useRmi) {

        try {
            byte[] fileData;

            if (useRmi) {
                try {
                    Registry registry = LocateRegistry.getRegistry(RMI_SERVER, RMI_PORT);
                    FileInterface rmi = (FileInterface) registry.lookup(RMI_SERVICE_NAME);
                    // ⚠️ Ajusta esta línea si tu interfaz tiene otra firma
                    fileData = rmi.downloadFile(path, Integer.parseInt(username));
                } catch (Exception e) {
                    System.err.println("Error connecting to RMI service (download): " + e.getMessage());
                    return ResponseEntity.status(500).build();
                }
            } else {
                fileData = soapFileService.downloadFile(username, path);
            }

            if (fileData != null) {
                return ResponseEntity.ok()
                        .header("Content-Disposition", "attachment; filename=" + path)
                        .body(fileData);
            }

            return ResponseEntity.notFound().build();

        } catch (Exception e) {
            System.err.println("Error en downloadFile: " + e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    // ======================= REPORT ===========================
    @GetMapping("/report")
    public ResponseEntity<String> getStorageReport(
            @RequestParam("username") String username,
            @RequestParam(value = "useRmi", defaultValue = "false") boolean useRmi) {

        try {
            if (useRmi) {
                try {
                    Registry registry = LocateRegistry.getRegistry(RMI_SERVER, RMI_PORT);
                    FileInterface rmi = (FileInterface) registry.lookup(RMI_SERVICE_NAME);
                    String report = rmi.getStorageReport(Integer.parseInt(username));
                    System.out.println("RMI Report: " + report);
                    return ResponseEntity.ok(report);
                } catch (Exception e) {
                    System.err.println("Error connecting to RMI service (report): " + e.getMessage());
                    return ResponseEntity.status(500).body("Error RMI: " + e.getMessage());
                }
            } else {
                String report = soapFileService.getStorageReport(username);
                return ResponseEntity.ok(report);
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
}
