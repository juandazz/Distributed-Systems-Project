package com.example.model;

public class StorageNode {
    private String name;
    private String url;
    private NodeStatus status;
    private long totalSpace;
    private long usedSpace;
    private int currentLoad; // Número de operaciones activas
    private long lastHealthCheck;
    
    public enum NodeStatus {
        ONLINE,
        OFFLINE,
        MAINTENANCE,
        DEGRADED
    }
    
    public StorageNode() {
    }
    
    public StorageNode(String name, String url) {
        this.name = name;
        this.url = url;
        this.status = NodeStatus.OFFLINE;
        this.currentLoad = 0;
        this.lastHealthCheck = 0;
    }
    
    // Getters and Setters
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getUrl() {
        return url;
    }
    
    public void setUrl(String url) {
        this.url = url;
    }
    
    public NodeStatus getStatus() {
        return status;
    }
    
    public void setStatus(NodeStatus status) {
        this.status = status;
    }
    
    public long getTotalSpace() {
        return totalSpace;
    }
    
    public void setTotalSpace(long totalSpace) {
        this.totalSpace = totalSpace;
    }
    
    public long getUsedSpace() {
        return usedSpace;
    }
    
    public void setUsedSpace(long usedSpace) {
        this.usedSpace = usedSpace;
    }
    
    public int getCurrentLoad() {
        return currentLoad;
    }
    
    public void setCurrentLoad(int currentLoad) {
        this.currentLoad = currentLoad;
    }
    
    public long getLastHealthCheck() {
        return lastHealthCheck;
    }
    
    public void setLastHealthCheck(long lastHealthCheck) {
        this.lastHealthCheck = lastHealthCheck;
    }
    
    public long getFreeSpace() {
        return totalSpace - usedSpace;
    }
    
    public double getUsagePercentage() {
        if (totalSpace == 0) return 0;
        return (double) usedSpace / totalSpace * 100;
    }
    
    public boolean isAvailable() {
        return status == NodeStatus.ONLINE;
    }
    
    public void incrementLoad() {
        this.currentLoad++;
    }
    
    public void decrementLoad() {
        if (this.currentLoad > 0) {
            this.currentLoad--;
        }
    }
    
    @Override
    public String toString() {
        return "StorageNode{" +
                "name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", status=" + status +
                ", currentLoad=" + currentLoad +
                ", freeSpace=" + getFreeSpace() +
                '}';
    }
}
