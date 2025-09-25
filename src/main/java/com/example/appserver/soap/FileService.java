package com.example.appserver.soap;
import javax.jws.WebMethod;
import javax.jws.WebService;

@WebService
public interface FileService {
    @WebMethod String createDirectory(String username, String path);
    @WebMethod String uploadFile(String username, String path, byte[] data);
    @WebMethod byte[] downloadFile(String username, String path);
    @WebMethod String deleteFile(String username, String path);
    @WebMethod String getStorageReport(String username);
}