package com.example.model;

import java.io.Serializable;

public class Node implements Serializable {
    private Integer idNode;
    private String hostname;
    private String ipAddress;
    private NodeStatus status;
    private Long capacityBytes;
    private Long usedBytes;

    public enum NodeStatus {
        ACTIVE, INACTIVE, MAINTENANCE
    }

    // Constructors
    public Node() {
    }

    public Node(String hostname, String ipAddress, Long capacityBytes) {
        this.hostname = hostname;
        this.ipAddress = ipAddress;
        this.capacityBytes = capacityBytes;
        this.usedBytes = 0L;
        this.status = NodeStatus.ACTIVE;
    }

    // Getters and Setters
    public Integer getIdNode() {
        return idNode;
    }

    public void setIdNode(Integer idNode) {
        this.idNode = idNode;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public NodeStatus getStatus() {
        return status;
    }

    public void setStatus(NodeStatus status) {
        this.status = status;
    }

    public Long getCapacityBytes() {
        return capacityBytes;
    }

    public void setCapacityBytes(Long capacityBytes) {
        this.capacityBytes = capacityBytes;
    }

    public Long getUsedBytes() {
        return usedBytes;
    }

    public void setUsedBytes(Long usedBytes) {
        this.usedBytes = usedBytes;
    }

    public int getStorageCapacity() {
        return (int) (capacityBytes / (1024 * 1024)); // Convert bytes to MB
    }
}