package com.example.model;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "Node")
public class Node implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_node")
    private Integer idNode;
    
    @Column(name = "hostname", length = 45)
    private String hostname;
    
    @Column(name = "ip_address", length = 45)
    private String ipAddress;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 45)
    private NodeStatus status;
    
    @Column(name = "capacity_bytes")
    private Long capacityBytes;
    
    @Column(name = "used_bytes")
    private Long usedBytes;

    public enum NodeStatus {
        ACTIVE, INACTIVE, MAINTENANCE
    }

    public Node() {
    }

    public Node(String hostname, String ipAddress, Long capacityBytes) {
        this.hostname = hostname;
        this.ipAddress = ipAddress;
        this.capacityBytes = capacityBytes;
        this.usedBytes = 0L;
        this.status = NodeStatus.ACTIVE;
    }

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