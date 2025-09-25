package com.example;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IStorageNode extends Remote {
    boolean storeFile(String path, byte[] data) throws RemoteException;
    byte[] readFile(String path) throws RemoteException;
    boolean deleteFile(String path) throws RemoteException;
    boolean createDirectory(String path) throws RemoteException;
    String getStatus() throws RemoteException;
}