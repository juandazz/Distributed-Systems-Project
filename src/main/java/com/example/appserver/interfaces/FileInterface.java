package com.example.appserver.interfaces;

import com.example.model.File;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface FileInterface extends Remote {
    File uploadFile(byte[] fileContent, String fileName, String mimeType, Integer userId) throws RemoteException;
    File getFile(Integer fileId) throws RemoteException;
    boolean deleteFile(Integer fileId) throws RemoteException;
    String getStorageReport(int username ) throws RemoteException;
    byte[] downloadFile(String path , int username) throws RemoteException;

}