package com.example.model;

import java.io.Serializable;
import java.sql.Timestamp;

public class File implements Serializable {
    private Integer idFile;
    private String name;
    private Long sizeBytes;
    private String mimeType;
    private Timestamp createdAt;
    private Integer userId;
    private byte[] content;

    // Constructors
    public File() {
    }

    public File(String name, Long sizeBytes, String mimeType, Integer userId) {
        this.name = name;
        this.sizeBytes = sizeBytes;
        this.mimeType = mimeType;
        this.userId = userId;
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

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }
}