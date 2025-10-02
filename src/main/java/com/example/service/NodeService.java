package com.example.service;

import com.example.appserver.interfaces.NodeInterface;
import com.example.model.Node;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class NodeService extends UnicastRemoteObject implements NodeInterface {
    private final ConcurrentHashMap<Integer, Node> nodes = new ConcurrentHashMap<>();
    private static int nextNodeId = 1;

    public NodeService() throws RemoteException {
        super();
    }

    @Override
    public void registerNode(Node node) throws RemoteException {
        node.setIdNode(nextNodeId++);
        nodes.put(node.getIdNode(), node);
    }

    @Override
    public List<Node> getAvailableNodes() throws RemoteException {
        return new ArrayList<>(nodes.values());
    }

    @Override
    public void updateNodeStatus(Integer nodeId, Node.NodeStatus status) throws RemoteException {
        Node node = nodes.get(nodeId);
        if (node != null) {
            node.setStatus(status);
        }
    }

    @Override
    public Node getNodeById(Integer nodeId) throws RemoteException {
        return nodes.get(nodeId);
    }

    @Override
    public String getStorageReport(Integer nodeId) throws RemoteException {
        Node node = nodes.get(nodeId);
        if (node != null) {
            return "Node ID: " + node.getIdNode() +
                   ", Hostname: " + node.getHostname() +
                   ", IP: " + node.getIpAddress() +
                   ", Capacity: " + node.getStorageCapacity() +
                   ", Status: " + node.getStatus();
        } else {
            return "Node with ID " + nodeId + " not found.";
        }
    }   
}