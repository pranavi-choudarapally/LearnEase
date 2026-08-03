package com.learnease.backend.service;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

@Service
public class PdfService {

    public String extractText(String fileUrl) {

        try (InputStream inputStream = new URL(fileUrl).openStream();
             PDDocument document = Loader.loadPDF(inputStream.readAllBytes())) {

            PDFTextStripper stripper = new PDFTextStripper();

            String text = stripper.getText(document);

            if (text.length() > 15000) {
                text = text.substring(0, 15000);
            }

            return text;

        } catch (IOException e) {
            throw new RuntimeException("Unable to read PDF", e);
        }
    }
}