package com.example.model;

import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.*;

@Entity
@Table(name = "FileReplica")
public class FileReplica implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_replica")
    private Integer idReplica;
    
    @Column(name = "path_in_node", length = 255)
    private String pathInNode;
    
    @Column(name = "created_at")
    private Timestamp createdAt;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "File_id_file", foreignKey = @ForeignKey(name = "fk_replica_file"))
    private File file;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "File_User_id_user", foreignKey = @ForeignKey(name = "fk_replica_user"))
    private User user;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Node_id_node", foreignKey = @ForeignKey(name = "fk_replica_node"))
    private Node node;

    // Constructors
    public FileReplica() {
    }

    public FileReplica(String pathInNode, File file, User user, Node node) {
        this.pathInNode = pathInNode;
        this.file = file;
        this.user = user;
        this.node = node;
    }

    // Getters and Setters
    public Integer getIdReplica() {
        return idReplica;
    }

    public void setIdReplica(Integer idReplica) {
        this.idReplica = idReplica;
    }

    public String getPathInNode() {
        return pathInNode;
    }

    public void setPathInNode(String pathInNode) {
        this.pathInNode = pathInNode;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }
}