package com.learnease.backend.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryService {

    @Autowired
    private Cloudinary cloudinary;

    public String uploadPdf(MultipartFile file) throws IOException {

    Map<?, ?> result = cloudinary.uploader().upload(
            file.getInputStream(),
            ObjectUtils.asMap(
                    "resource_type", "raw",
                    "folder", "learnease-pdfs",
                    "use_filename", true,
                    "unique_filename", true
            )
    );

    return result.get("secure_url").toString();
}
}