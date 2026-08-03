package com.learnease.backend.controller;

import com.learnease.backend.entity.StudyMaterial;
import com.learnease.backend.service.StudyMaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/api/materials")
@CrossOrigin(
    origins = {
        "http://127.0.0.1:5500",
        "https://eloquent-banoffee-515181.netlify.app"
    }
)
public class StudyMaterialController {

    @Autowired
    private StudyMaterialService studyMaterialService;

 @PostMapping("/upload")
public ResponseEntity<String> uploadFile(
        @RequestParam("file") MultipartFile file,
        @RequestParam("uploadedBy") String uploadedBy) throws IOException {

    // Allow only PDF files
    if (!"application/pdf".equals(file.getContentType())) {
        return ResponseEntity.badRequest()
                .body("Only PDF files are allowed.");
    }

    String response = studyMaterialService.uploadFile(file, uploadedBy);

    return ResponseEntity.ok(response);
}

    @GetMapping("/all")
    public ResponseEntity<List<StudyMaterial>> getAllFiles() {

        return ResponseEntity.ok(
                studyMaterialService.getAllFiles()
        );

    }

    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long id) throws Exception {

        StudyMaterial file = studyMaterialService.getFileById(id);

        Path path = Paths.get(file.getFilePath());

        Resource resource = new UrlResource(path.toUri());

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + file.getFileName() + "\""
                )
                .body(resource);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteFile(@PathVariable Long id) throws IOException {

        studyMaterialService.deleteFile(id);

        return ResponseEntity.ok("File Deleted Successfully");

    }

    @GetMapping("/search")
    public List<StudyMaterial> searchFiles(
            @RequestParam String keyword) {

        return studyMaterialService.searchFiles(keyword);

    }

}