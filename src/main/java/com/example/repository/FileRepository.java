package com.example.repository;

import com.example.model.File;
import com.example.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface FileRepository extends JpaRepository<File, Integer> {
    List<File> findByUser(User user);
    List<File> findByUser_IdUser(Integer userId);
    List<File> findByName(String name);
    List<File> findByMimeType(String mimeType);
}