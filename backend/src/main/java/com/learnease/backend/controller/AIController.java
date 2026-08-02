package com.learnease.backend.controller;

import com.learnease.backend.ai.OpenRouterService;
import com.learnease.backend.entity.StudyMaterial;
import com.learnease.backend.service.PdfService;
import com.learnease.backend.service.StudyMaterialService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
// 🌟 FORCE WIDE OPEN ACCESS SPECIFICALLY FOR YOUR PRODUCTION DOMAINS
@CrossOrigin(
    origins = "*", 
    allowedHeaders = "*", 
    methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS}
)
public class AIController {

    private final OpenRouterService openRouterService;
    private final PdfService pdfService;
    private final StudyMaterialService studyMaterialService;

    public AIController(OpenRouterService openRouterService, PdfService pdfService, StudyMaterialService studyMaterialService) {
        this.openRouterService = openRouterService;
        this.pdfService = pdfService;
        this.studyMaterialService = studyMaterialService;
    }

    @GetMapping("/test")
    public String test() {
        return "OpenRouter Controller Working!";
    }

    @PostMapping("/summarize/{id}")
    public String summarize(@PathVariable Long id) {
        try {
            StudyMaterial file = studyMaterialService.getFileById(id);
            if (file == null) {
                return "Error: Study material not found in database.";
            }
            String extractedText = pdfService.extractText(file.getFilePath());
            if (extractedText == null || extractedText.trim().isEmpty()) {
                return "Error: Could not extract readable text strings out of your PDF file document.";
            }
            return openRouterService.summarize(extractedText);
        } catch (Exception e) {
            e.printStackTrace();
            return "Internal Server Error during AI summary compilation processing: " + e.getMessage();
        }
    }

    @PostMapping("/quiz/{id}")
    public String generateQuiz(@PathVariable Long id) throws Exception {
        return openRouterService.generateQuizFromPdf(id);
    }

    @PostMapping("/resources/{id}")
    public String generateResources(@PathVariable Long id) throws Exception {
        return openRouterService.generateResourcesFromPdf(id);
    }
}
