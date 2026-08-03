package com.learnease.backend.service;

import com.learnease.backend.entity.StudyMaterial;
import com.learnease.backend.repository.StudyMaterialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class StudyMaterialService {

    @Autowired
    private StudyMaterialRepository repository;

    @Autowired
    private CloudinaryService cloudinaryService;

    public String uploadFile(MultipartFile file, String uploadedBy) throws IOException {

        // Upload PDF to Cloudinary
        String fileUrl = cloudinaryService.uploadPdf(file);

        // Save metadata
        StudyMaterial material = new StudyMaterial();

        material.setFileName(file.getOriginalFilename());
        material.setFileType(file.getContentType());
        material.setFileSize(file.getSize());

        // Store Cloudinary URL instead of local path
        material.setFilePath(fileUrl);

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

        // Delete only from database for now.
        // (We'll add Cloudinary deletion later.)

        StudyMaterial material = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("File not found"));

        repository.delete(material);
    }

    public List<StudyMaterial> searchFiles(String keyword) {
        return repository.findByFileNameContainingIgnoreCase(keyword);
    }
}