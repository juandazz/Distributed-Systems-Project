package com.example.appserver.rest;

import com.example.appserver.soap.FileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

@RestController
@RequestMapping("/api/files")
public class FileController {
    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("username") String username,
            @RequestParam("path") String path) {
        try {
            byte[] fileData = file.getBytes();
            String result = fileService.uploadFile(username, path, fileData);
            return ResponseEntity.ok(result);
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("Error al procesar el archivo: " + e.getMessage());
        }
    }

    @GetMapping("/download")
    public ResponseEntity<byte[]> downloadFile(
            @RequestParam("username") String username,
            @RequestParam("path") String path) {
        byte[] fileData = fileService.downloadFile(username, path);
        if (fileData != null) {
            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename=" + path)
                    .body(fileData);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/report")
    public ResponseEntity<String> getStorageReport(@RequestParam("username") String username) {
        String report = fileService.getStorageReport(username);
        return ResponseEntity.ok(report);
    }
}