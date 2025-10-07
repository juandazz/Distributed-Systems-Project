package com.example.repository;

import com.example.model.Node;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface NodeRepository extends JpaRepository<Node, Integer> {
    List<Node> findByStatus(Node.NodeStatus status);
    Optional<Node> findByHostname(String hostname);
    Optional<Node> findByIpAddress(String ipAddress);
}