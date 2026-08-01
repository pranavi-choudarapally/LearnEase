package com.learnease.backend.repository;

import com.learnease.backend.entity.StudyMaterial;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface StudyMaterialRepository
        extends JpaRepository<StudyMaterial, Long> {
            List<StudyMaterial> findByFileNameContainingIgnoreCase(String fileName);
}