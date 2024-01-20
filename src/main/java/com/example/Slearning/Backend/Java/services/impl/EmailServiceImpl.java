package com.example.Slearning.Backend.Java.services.impl;

import com.example.Slearning.Backend.Java.services.EmailService;
import com.example.Slearning.Backend.Java.utils.ExcelUtils;
import jakarta.activation.DataSource;
import jakarta.activation.FileDataSource;
import jakarta.mail.Part;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import jakarta.mail.util.ByteArrayDataSource;
import lombok.RequiredArgsConstructor;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.time.YearMonth;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    @Value("${spring.mail.username}")
    private String fromEmail;

    private final JavaMailSender javaMailSender;

    @Override
    public void sendMonthlyPaymentMail(String subject, String[] to, String text, ByteArrayInputStream excelFile) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text + "<html><body><button style={background-color:black; color: white}>Click</button></body></html>",
                    true);

            // TODO: Them file excel hoa don vao day
            String attachFileName = String.format("Thanh_toan_thang_%s.xlsx", YearMonth.now());
            InputStreamSource attachment = new ByteArrayResource(excelFile.readAllBytes());
            helper.addAttachment(attachFileName, attachment, "application/vnd.ms-excel");
            javaMailSender.send(message);
        } catch (Exception exception) {
            throw new RuntimeException(exception.getMessage());
        }
    }
}
