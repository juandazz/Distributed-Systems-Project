package com.example.model;

import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.*;

@Entity
@Table(name = "File")
public class File implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_file")
    private Integer idFile;
    
    @Column(name = "name", length = 45)
    private String name;
    
    @Column(name = "size_bytes")
    private Long sizeBytes;
    
    @Column(name = "mime_type", length = 45)
    private String mimeType;
    
    @Column(name = "created_at")
    private Timestamp createdAt;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "User_id_user", foreignKey = @ForeignKey(name = "fk_file_user"))
    private User user;
    
    @Transient
    private byte[] content; // No persistir en BD, solo en memoria

    // Constructors
    public File() {
    }

    public File(String name, Long sizeBytes, String mimeType, User user) {
        this.name = name;
        this.sizeBytes = sizeBytes;
        this.mimeType = mimeType;
        this.user = user;
    }

    // Getters and Setters
    public Integer getIdFile() {
        return idFile;
    }

    public void setIdFile(Integer idFile) {
        this.idFile = idFile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getSizeBytes() {
        return sizeBytes;
    }

    public void setSizeBytes(Long sizeBytes) {
        this.sizeBytes = sizeBytes;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
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

    // Método de conveniencia para obtener el ID del usuario
    public Integer getUserId() {
        return user != null ? user.getIdUser() : null;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }
}