package com.example.Slearning.Backend.Java.services;

import com.example.Slearning.Backend.Java.domain.entities.AdminPayment;
import com.example.Slearning.Backend.Java.domain.entities.Payment;
import com.example.Slearning.Backend.Java.domain.responses.PageResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface PaymentService {

    PageResponse<AdminPayment> getPendingAdminPayment(
            Integer pageNumber,
            Integer pageSize,
            String sortBy,
            String sortDir
    );

    PageResponse<AdminPayment> getSuccessAdminPayment(
            Integer pageNumber,
            Integer pageSize,
            String sortBy,
            String sortDir
    );

    Payment getPaymentOfCourse(UUID userId, UUID courseId);

    PageResponse<Payment> getPaymentsOfUser(
            Integer pageNumber,
            Integer pageSize,
            String sortBy,
            String sortDir,
            UUID userId
    );

    AdminPayment getMonthlyPaymentOfUser(UUID userId, String yearMonth);

    PageResponse<AdminPayment> getAllAdminPaymentsOfUser(
            Integer pageNumber,
            Integer pageSize,
            String sortBy,
            String sortDir,
            UUID userId
    );

    Double getCurrentRevenueOfMentor(UUID mentorId);

    String vnpayPayment(HttpServletRequest request, HttpServletResponse response) throws IOException;

    void createPaymentVNPAY(HttpServletRequest request);

    AdminPayment withDrawMoney(UUID userId, Double amount);

    String adminVnpayPayment(HttpServletRequest request, HttpServletResponse response) throws IOException;

    void resolveAdminVnpayPayment(HttpServletRequest request);

    List<String> getAllEmailMentorHasPaymentCurrentMonth();

    ByteArrayInputStream getExcelDataOfMentorPayment(String email) throws IOException;

    ByteArrayInputStream getExcelPaymentsInMonthOfUser(UUID monthlyPaymentId) throws IOException;
}
