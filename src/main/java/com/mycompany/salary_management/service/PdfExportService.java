package com.mycompany.salary_management.service;

import com.mycompany.salary_management.dto.PayrollDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

@Service
public class PdfExportService {

    @Autowired
    private SpringTemplateEngine templateEngine;

    private String loadLogoAsBase64() {
        try {
            var resource = new ClassPathResource("static/images/logo-transparent.png");
            byte[] bytes = resource.getInputStream().readAllBytes();
            return Base64.getEncoder().encodeToString(bytes);
        } catch (IOException e) {
            throw new RuntimeException("Erreur lors du chargement du logo", e);
        }
    }

    public byte[] generatePayrollPdf(PayrollDTO dto) {
        var context = new Context();
        context.setVariable("payroll", dto);
        context.setVariable("today", LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        context.setVariable("logo", loadLogoAsBase64());

        var html = templateEngine.process("payroll-report", context);

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            var renderer = new ITextRenderer();
            renderer.setDocumentFromString(html);
            renderer.layout();  
            renderer.createPDF(out);
            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Erreur PDF : " + e.getMessage(), e);
        }
    }
}
