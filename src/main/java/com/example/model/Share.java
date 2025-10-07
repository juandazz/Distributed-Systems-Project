package com.example.model;

import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.*;

@Entity
@Table(name = "Share")
public class Share implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_share")
    private Integer idShare;
    
    @Column(name = "permission", length = 45)
    private String permission;
    
    @Column(name = "created_at")
    private Timestamp createdAt;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "User_id_user", foreignKey = @ForeignKey(name = "fk_share_user"))
    private User user;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "File_id_file", foreignKey = @ForeignKey(name = "fk_share_file"))
    private File file;

    // Constructors
    public Share() {
    }

    public Share(String permission, User user, File file) {
        this.permission = permission;
        this.user = user;
        this.file = file;
    }

    // Getters and Setters
    public Integer getIdShare() {
        return idShare;
    }

    public void setIdShare(Integer idShare) {
        this.idShare = idShare;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
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

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }
}