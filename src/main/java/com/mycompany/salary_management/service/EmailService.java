package com.mycompany.salary_management.service;

import java.util.Map;

import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private SpringTemplateEngine templateEngine;

    public void sendPayrollEmailWithTemplate(String to, String subject, String templateName, Map<String, Object> variables, byte[] pdfAttachment, String fileName) {
        try {
            Context context = new Context();
            context.setVariables(variables);
            String htmlBody = templateEngine.process(templateName, context);

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom("ton.email@gmail.com", "NoReply - Paie RH");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlBody, true);

            helper.addAttachment(fileName, new ByteArrayResource(pdfAttachment));

            mailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException("Erreur envoi email : " + e.getMessage(), e);
        }
    }
}

