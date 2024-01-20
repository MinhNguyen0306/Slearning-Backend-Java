package com.example.Slearning.Backend.Java.services;

import java.io.ByteArrayInputStream;

public interface EmailService {
    void sendMonthlyPaymentMail(String subject, String[] to, String text, ByteArrayInputStream excelFile);
}
