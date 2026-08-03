package com.learnease.backend.ai;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.learnease.backend.entity.StudyMaterial;
import com.learnease.backend.service.PdfService;
import com.learnease.backend.service.StudyMaterialService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import java.util.List;
import java.util.Map;

@Service
public class OpenRouterService {

    private final RestClient restClient;
    private final PdfService pdfService;
    private final StudyMaterialService studyMaterialService;

    @Value("${openrouter.api.key}")
    private String apiKey;

    @Value("${openrouter.api.url}")
    private String apiUrl;

    @Value("${openrouter.model}")
    private String model;

    public OpenRouterService(RestClient restClient, PdfService pdfService, StudyMaterialService studyMaterialService) {
        this.restClient = restClient;
        this.pdfService = pdfService;
        this.studyMaterialService = studyMaterialService;
    }

    // 🌟 HELPER METHOD TO ESCAPE JSON CHARACTERS SAFELY
    private String escapeJsonString(String input) {
        if (input == null) return "";
        return input.replace("\\", "\\\\")
                    .replace("\"", "\\\"")
                    .replace("\b", "\\b")
                    .replace("\f", "\\f")
                    .replace("\n", "\\n")
                    .replace("\r", "\\r")
                    .replace("\t", "\\t");
    }

    // ========================= SUMMARY =========================
    public String summarize(String extractedText) {
        try {
            String safeText = escapeJsonString(extractedText);
            
           Map<String, Object> body = Map.of(
                "model", model,// Using stable free model
                "messages", List.of(
                    Map.of(
                        "role", "user",
                        "content", "You are an expert study assistant. Summarize the following study notes. Return the answer ONLY in Markdown. Do NOT return HTML. Study Notes: " + safeText
                    )
                )
            );

            String response = callOpenRouter(body);
            
            // 🌟 CHECK IF RESPONSE IS HTML BEFORE PARSING AS JSON
            if (response != null && response.trim().startsWith("<")) {
                return "API Gateway Error: Received an HTML error response from OpenRouter. Please verify your API Key and credits on your OpenRouter dashboard panel.";
            }

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response);
            return root.path("choices").get(0).path("message").path("content").asText();
        } catch (Exception e) {
            e.printStackTrace();
            return "Error : " + e.getMessage();
        }
    }

    public String summarizePdf(Long id) throws Exception {
        StudyMaterial file = studyMaterialService.getFileById(id);
        String extractedText = pdfService.extractText(file.getFilePath());
        return summarize(extractedText);
    }

    // ========================= QUIZ =========================
    public String generateQuizFromPdf(Long id) throws Exception {
        StudyMaterial file = studyMaterialService.getFileById(id);
        String extractedText = pdfService.extractText(file.getFilePath());
        return generateQuiz(extractedText);
    }

    public String generateQuiz(String extractedText) {
        try {
            String safeText = escapeJsonString(extractedText);
            
            Map<String, Object> body = Map.of(
                "model", model,// Using stable free model
                "messages", List.of(
                    Map.of(
                        "role", "user",
                        "content", "Generate exactly 10 multiple-choice questions from the following study notes as a valid JSON object. Study Notes: " + safeText
                    )
                )
            );

            String response = callOpenRouter(body);
            
            if (response != null && response.trim().startsWith("<")) {
                return "{\"error\": \"Received HTML response from API\"}";
            }

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response);
            return root.path("choices").get(0).path("message").path("content").asText();
        } catch (Exception e) {
            e.printStackTrace();
            return "Error : " + e.getMessage();
        }
    }

    public String generateResourcesFromPdf(Long id) throws Exception {
        StudyMaterial file = studyMaterialService.getFileById(id);
        String extractedText = pdfService.extractText(file.getFilePath());
        return generateResources(extractedText);
    }

    public String generateResources(String extractedText) {
        try {
            String safeText = escapeJsonString(extractedText);
            
            Map<String, Object> body = Map.of(
                "model", model,// Using stable free model
                "messages", List.of(
                    Map.of(
                        "role", "user",
                        "content", "Recommend useful learning resources based on these notes: " + safeText
                    )
                )
            );

            String response = callOpenRouter(body);
            
            if (response != null && response.trim().startsWith("<")) {
                return "API Gateway Error: Received an HTML error response from OpenRouter.";
            }

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response);
            return root.path("choices").get(0).path("message").path("content").asText();
        } catch (Exception e) {
            e.printStackTrace();
            return "Error : " + e.getMessage();
        }
    }

    // ========================= COMMON =========================
    private String callOpenRouter(Map<String, Object> body) {
        return restClient.post()
                .uri(apiUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + apiKey)
                 .header("HTTP-Referer", "https://eloquent-banoffee-515181.netlify.app") 
                .header("X-Title", "LearnEase")
                .body(body)
                .retrieve()
                .body(String.class);
    }
}
