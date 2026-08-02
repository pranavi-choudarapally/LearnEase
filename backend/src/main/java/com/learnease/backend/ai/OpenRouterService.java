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

    public OpenRouterService(
            RestClient restClient,
            PdfService pdfService,
            StudyMaterialService studyMaterialService) {

        this.restClient = restClient;
        this.pdfService = pdfService;
        this.studyMaterialService = studyMaterialService;
    }

    // ========================= SUMMARY =========================

    public String summarize(String extractedText) {

        try {

            Map<String, Object> body = Map.of(

                    "model", model,

                    "messages", List.of(

                            Map.of(

                                    "role", "user",

                                    "content",

                                    """
                                    You are an expert study assistant.

                                    Summarize the following study notes.

                                    Return the answer ONLY in Markdown.

                                    Use this exact structure:

                                    # 1. Short Summary

                                    Write one concise paragraph.

                                    # 2. Important Concepts

                                    ## Platform Independence

                                    Explanation.

                                    ## Object-Oriented Design

                                    Explanation.

                                    ## JVM, JDK, JRE

                                    Explanation.

                                    # 3. Important Definitions

                                    ## JVM

                                    Definition.

                                    ## JDK

                                    Definition.

                                    ## JRE

                                    Definition.

                                    # 4. Quick Revision Points

                                    - Point 1
                                    - Point 2
                                    - Point 3
                                    - Point 4

                                    Do NOT return HTML.
                                    Do NOT use code blocks.
                                    Do NOT wrap the answer in triple backticks.

                                    Study Notes:

                                    """ + extractedText

                            )

                    )

            );

            String response = callOpenRouter(body);

            ObjectMapper mapper = new ObjectMapper();

            JsonNode root = mapper.readTree(response);

            return root.path("choices")
                    .get(0)
                    .path("message")
                    .path("content")
                    .asText();

        }

        catch (Exception e) {

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

            Map<String, Object> body = Map.of(

                    "model", model,

                    "messages", List.of(

                            Map.of(

                                    "role", "user",

                                    "content",

                                    """
                                    Generate exactly 10 multiple-choice questions from the following study notes.

                                    Rules:

                                    - Return ONLY valid JSON.
                                    - No markdown.
                                    - No explanations.
                                    - No code blocks.
                                    - Exactly 10 questions.
                                    - Every question must have exactly 4 options.
                                    - answer must be 0,1,2 or 3.

                                    Format:

                                    {
                                      "questions":[
                                        {
                                          "question":"...",
                                          "options":[
                                            "...",
                                            "...",
                                            "...",
                                            "..."
                                          ],
                                          "answer":0
                                        }
                                      ]
                                    }

                                    Study Notes:

                                    """ + extractedText

                            )

                    )

            );

            String response = callOpenRouter(body);

            ObjectMapper mapper = new ObjectMapper();

            JsonNode root = mapper.readTree(response);

            return root.path("choices")
                    .get(0)
                    .path("message")
                    .path("content")
                    .asText();

        }

        catch (Exception e) {

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

        Map<String, Object> body = Map.of(

                "model", model,

                "messages", List.of(

                        Map.of(

                                "role", "user",

                                "content",

                                """
                                You are an expert learning mentor.

                                Based ONLY on the uploaded study notes, recommend useful learning resources.

                                Return ONLY Markdown.

                                Use EXACTLY this structure:

                                # 📺 YouTube Videos

                                Recommend 3-5 YouTube channels or playlists.

                                For each recommendation include a clickable Markdown search link.

                                Example:

                                - [Programming with Mosh](https://www.youtube.com/results?search_query=Programming+with+Mosh+Java)
                                - [Bro Code](https://www.youtube.com/results?search_query=Bro+Code+Java)
                                - [Java Brains](https://www.youtube.com/results?search_query=Java+Brains)

                                # 📚 Books

                                Recommend 2-4 books.

                                Whenever possible include an official publisher or Google Books search link.

                                Example:

                                - [Head First Java](https://www.google.com/search?q=Head+First+Java+Book)
                                - [Effective Java](https://www.google.com/search?q=Effective+Java+Book)

                                # 🌐 Websites

                                Recommend 3-5 useful websites.

                                Always provide clickable Markdown links.

                                Example:

                                - [Oracle Java Documentation](https://docs.oracle.com/javase/)
                                - [Baeldung](https://www.baeldung.com/)
                                - [GeeksforGeeks Java](https://www.geeksforgeeks.org/java/)
                                - [Java Code Geeks](https://www.javacodegeeks.com/)

                                # 💻 Practice Platforms

                                Recommend platforms related to this topic.

                                Always provide clickable links.

                                Example:

                                - [LeetCode](https://leetcode.com/)
                                - [CodeChef](https://www.codechef.com/)
                                - [HackerRank](https://www.hackerrank.com/)
                                - [Codeforces](https://codeforces.com/)

                                # 🎯 Learning Tips

                                Give exactly 5 practical tips.

                                Rules:

                                - Keep recommendations directly related to the uploaded notes.
                                - Do NOT recommend unrelated technologies.
                                - Return ONLY Markdown.
                                - Do NOT return HTML.
                                - Do NOT use code blocks.
                                - Do NOT wrap the response in triple backticks.
                                - Use clickable Markdown links wherever possible.

                                Study Notes:

                                """ + extractedText

                        )

                )

        );

        String response = callOpenRouter(body);

        ObjectMapper mapper = new ObjectMapper();

        JsonNode root = mapper.readTree(response);

        return root.path("choices")
                .get(0)
                .path("message")
                .path("content")
                .asText();

    }

    catch (Exception e) {

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
        // 🌟 FIXED: Points straight to your active production Netlify application layout
        .header("HTTP-Referer", "https://eloquent-banoffee-515181.netlify.app") 
        .header("X-Title", "LearnEase") 
        .body(body) 
        .retrieve() 
        .body(String.class); 
}

}