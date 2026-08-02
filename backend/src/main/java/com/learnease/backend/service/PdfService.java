package com.learnease.backend.service;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Service
public class PdfService {

    public String extractText(String filePath) {

        try (PDDocument document = Loader.loadPDF(new File(filePath))) {

            PDFTextStripper stripper = new PDFTextStripper();

            String text = stripper.getText(document);

            // Prevent sending huge documents to the AI
            if (text.length() > 15000) {
                text = text.substring(0, 15000);
            }

            return text;

        } catch (IOException e) {
            throw new RuntimeException("Unable to read PDF", e);
        }
    }
}