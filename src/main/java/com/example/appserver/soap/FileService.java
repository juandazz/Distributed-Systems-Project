package com.example.appserver.soap;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

@WebService
public interface FileService {
    @WebMethod String createDirectory(@WebParam(name = "token") String token,
                                    @WebParam(name = "path") String path);
    @WebMethod String uploadFile(@WebParam(name = "token") String token,
                               @WebParam(name = "path") String path, 
                               @WebParam(name = "data") String data);
    @WebMethod byte[] downloadFile(@WebParam(name = "token") String token,
                                 @WebParam(name = "path") String path);
    @WebMethod String deleteFile(@WebParam(name = "token") String token,
                                @WebParam(name = "path") String path);
    @WebMethod String getStorageReport(@WebParam(name = "token") String token);
    
    // Método para login via SOAP
    @WebMethod String authenticateUser(@WebParam(name = "username") String username,
                                     @WebParam(name = "password") String password);
}