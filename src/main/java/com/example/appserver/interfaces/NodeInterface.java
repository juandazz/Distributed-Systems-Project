package com.example.appserver.interfaces;

import com.example.model.Node;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface NodeInterface extends Remote {
    void registerNode(Node node) throws RemoteException;
    List<Node> getAvailableNodes() throws RemoteException;
    void updateNodeStatus(Integer nodeId, Node.NodeStatus status) throws RemoteException;
    Node getNodeById(Integer nodeId) throws RemoteException;
    String getStorageReport(Integer nodeId) throws RemoteException;
}