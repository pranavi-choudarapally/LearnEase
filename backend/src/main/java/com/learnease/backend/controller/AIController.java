package com.learnease.backend.controller;

import com.learnease.backend.ai.OpenRouterService;
import com.learnease.backend.entity.StudyMaterial;
import com.learnease.backend.service.PdfService;
import com.learnease.backend.service.StudyMaterialService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
@CrossOrigin(origins = "*", allowedHeaders = "*") 
public class AIController {

    private final OpenRouterService openRouterService;
    private final PdfService pdfService;
    private final StudyMaterialService studyMaterialService;

    public AIController(OpenRouterService openRouterService,
                        PdfService pdfService,
                        StudyMaterialService studyMaterialService) {

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

        StudyMaterial file = studyMaterialService.getFileById(id);

        String extractedText = pdfService.extractText(file.getFilePath());

        return openRouterService.summarize(extractedText);

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