package com.learnease.backend.service;

import com.learnease.backend.entity.StudyMaterial;
import com.learnease.backend.repository.StudyMaterialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;

@Service
public class StudyMaterialService {

    @Autowired
    private StudyMaterialRepository repository;

    private final String uploadDir = "uploads/";

    public String uploadFile(MultipartFile file, String uploadedBy) throws IOException {

        // Create uploads folder if it doesn't exist
        Path uploadPath = Paths.get(uploadDir);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // File path
        Path filePath = uploadPath.resolve(file.getOriginalFilename());

        // Save file
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        // Save metadata
        StudyMaterial material = new StudyMaterial();

        material.setFileName(file.getOriginalFilename());
        material.setFileType(file.getContentType());
        material.setFileSize(file.getSize());
        material.setFilePath(filePath.toString());
        material.setUploadedBy(uploadedBy);

        repository.save(material);

        return "Upload Successful";
    }
    public List<StudyMaterial> getAllFiles() {
    return repository.findAll();
}
public StudyMaterial getFileById(Long id) {

    return repository.findById(id)
            .orElseThrow(() -> new RuntimeException("File not found"));

}
public void deleteFile(Long id) throws IOException {

    StudyMaterial material = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("File not found"));

    Path path = Paths.get(material.getFilePath());

    Files.deleteIfExists(path);

    repository.delete(material);
}
public List<StudyMaterial> searchFiles(String keyword){

    return repository.findByFileNameContainingIgnoreCase(keyword);

}
}