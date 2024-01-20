package com.example.Slearning.Backend.Java.controllers;

import com.example.Slearning.Backend.Java.configs.AppConstants;
import com.example.Slearning.Backend.Java.domain.entities.AdminPayment;
import com.example.Slearning.Backend.Java.domain.entities.Payment;
import com.example.Slearning.Backend.Java.domain.responses.PageResponse;
import com.example.Slearning.Backend.Java.services.PaymentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @GetMapping("/course/{courseId}")
    public ResponseEntity<Payment> getPaymentOfCourse(
            @PathVariable UUID userId,
            @RequestParam("courseId") UUID courseId
    ) {
        Payment payment = this.paymentService.getPaymentOfCourse(userId, courseId);
        return ResponseEntity.ok(payment);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<PageResponse<Payment>> getPaymentOfUser(
            @RequestParam(value = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.SORT_DIR, required = false) String sortDir,
            @PathVariable UUID userId
    ) {
        PageResponse<Payment> pageResponse = this.paymentService.getPaymentsOfUser(pageNumber, pageSize, sortBy, sortDir, userId);
        return ResponseEntity.ok(pageResponse);
    }

    @GetMapping("/user/{userId}/monthly-payment")
    public ResponseEntity<AdminPayment> getMonthlyPaymentOfUser(
            @PathVariable UUID userId,
            @RequestParam("yearMonth") String yearMonth
    ) {
        AdminPayment adminPayment = this.paymentService.getMonthlyPaymentOfUser(userId, yearMonth);
        if(adminPayment != null) {
            return ResponseEntity.ok(adminPayment);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/user/{userId}/admin-payment/all")
    public ResponseEntity<PageResponse<AdminPayment>> getAllAdminPaymentsOfUser(
            @RequestParam(value = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.SORT_DIR, required = false) String sortDir,
            @PathVariable UUID userId
    ) {
        PageResponse<AdminPayment> pageResponse = this.paymentService.getAllAdminPaymentsOfUser(pageNumber, pageSize, sortBy, sortDir, userId);
        return ResponseEntity.ok(pageResponse);
    }

    @GetMapping("/admin-payment/pending")
    public ResponseEntity<PageResponse<AdminPayment>> getPendingAdminPayment(
            @RequestParam(value = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.SORT_DIR, required = false) String sortDir
    ) {
        PageResponse<AdminPayment> pageResponse = this.paymentService.getPendingAdminPayment(pageNumber, pageSize, sortBy, sortDir);
        return ResponseEntity.ok(pageResponse);
    }

    @GetMapping("/admin-payment/success")
    public ResponseEntity<PageResponse<AdminPayment>> getSuccessAdminPayment(
            @RequestParam(value = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.SORT_DIR, required = false) String sortDir
    ) {
        PageResponse<AdminPayment> pageResponse = this.paymentService.getSuccessAdminPayment(pageNumber, pageSize, sortBy, sortDir);
        return ResponseEntity.ok(pageResponse);
    }

    @PostMapping("/vnpay")
    public ResponseEntity<String> vnpayPayment(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String paymentUrl = this.paymentService.vnpayPayment(request, response);
        return ResponseEntity.ok(paymentUrl);
    }

    @PostMapping("/create/vnpay-payment")
    public ResponseEntity<Void> createPaymentVNPAY(HttpServletRequest request) {
        this.paymentService.createPaymentVNPAY(request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/current-month-revenue")
    public ResponseEntity<Double> getCurrentRevenueOfMentor(@RequestParam("userId") UUID userId) {
        return ResponseEntity.ok(paymentService.getCurrentRevenueOfMentor(userId));
    }

    @PostMapping("/withdraw-money")
    public ResponseEntity<AdminPayment> withdrawMoney(
            @RequestParam("userId") UUID userId,
            @RequestParam("amount") Double amount
    ) {
        AdminPayment adminPayment = paymentService.withDrawMoney(userId, amount);
        return new ResponseEntity<>(adminPayment, HttpStatus.CREATED);
    }

    @PostMapping("/vnpay/admin")
    public ResponseEntity<String> adminVnpayPayment(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String paymentUrl = this.paymentService.adminVnpayPayment(request, response);
        return ResponseEntity.ok(paymentUrl);
    }

    @PostMapping("/create/vnpay-payment/admin")
    public ResponseEntity<Void> resolveAdminVnpayPayment(HttpServletRequest request) {
        this.paymentService.resolveAdminVnpayPayment(request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/payments-in-month/excel")
    public ResponseEntity<Resource> getExcelPaymentsInMonthOfUser(
            @RequestParam("monthlyPaymentId") UUID monthlyPaymentId
    ) throws IOException {
        String fileName = "Hoa_don_thang_" + monthlyPaymentId.toString() + ".xlsx";
        ByteArrayInputStream excelData = this.paymentService.getExcelPaymentsInMonthOfUser(monthlyPaymentId);
        InputStreamResource file = new InputStreamResource(excelData);

        ResponseEntity<Resource> body = ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename="+fileName)
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .body(file);
        return body;
    }
}
