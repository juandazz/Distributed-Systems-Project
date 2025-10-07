package com.example.model;

import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.*;

@Entity
@Table(name = "Directory")
public class Directory implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_directory")
    private Integer idDirectory;
    
    @Column(name = "name", length = 45)
    private String name;
    
    @Column(name = "parent_id", length = 45)
    private String parentId;
    
    @Column(name = "owner_id", length = 45)
    private String ownerId;
    
    @Column(name = "created_at")
    private Timestamp createdAt;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "User_id_user", foreignKey = @ForeignKey(name = "fk_directory_user"))
    private User user;

    // Constructors
    public Directory() {
    }

    public Directory(String name, String parentId, String ownerId, User user) {
        this.name = name;
        this.parentId = parentId;
        this.ownerId = ownerId;
        this.user = user;
    }

    // Getters and Setters
    public Integer getIdDirectory() {
        return idDirectory;
    }

    public void setIdDirectory(Integer idDirectory) {
        this.idDirectory = idDirectory;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}