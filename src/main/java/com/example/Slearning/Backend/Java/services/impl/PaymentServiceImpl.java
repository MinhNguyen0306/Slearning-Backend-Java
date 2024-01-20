package com.example.Slearning.Backend.Java.services.impl;

import com.example.Slearning.Backend.Java.configs.VNPAYConfig;
import com.example.Slearning.Backend.Java.domain.entities.*;
import com.example.Slearning.Backend.Java.domain.responses.PageResponse;
import com.example.Slearning.Backend.Java.exceptions.ResourceNotFoundException;
import com.example.Slearning.Backend.Java.repositories.*;
import com.example.Slearning.Backend.Java.services.PaymentService;
import com.example.Slearning.Backend.Java.utils.AppUtils;
import com.example.Slearning.Backend.Java.utils.ExcelUtils;
import com.example.Slearning.Backend.Java.utils.PageUtils;
import com.example.Slearning.Backend.Java.utils.enums.AdminPaymentStatus;
import com.example.Slearning.Backend.Java.utils.enums.PaymentStatus;
import com.example.Slearning.Backend.Java.utils.enums.VnpayPaymentStatus;
import com.google.gson.JsonObject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final VNPAYConfig vnpayConfig;

    private final CourseRepository courseRepository;

    private final UserRepository userRepository;

    private final PaymentRepository paymentRepository;

    private final VnpayPaymentRepository vnpayPaymentRepository;

    private final AdminPaymentRepository adminPaymentRepository;

    private final AccountBalanceRepository accountBalanceRepository;

    @Override
    public PageResponse<AdminPayment> getPendingAdminPayment(
            Integer pageNumber, Integer pageSize, String sortBy, String sortDir
    ) {
        Pageable pageable = PageUtils.getPageable(pageNumber, pageSize, sortBy, sortDir);
        Page<AdminPayment> page = this.adminPaymentRepository.getPendingAdminPayment(pageable);

        List<AdminPayment> adminPayments = page.getContent();
        PageResponse<AdminPayment> pageResponse = new PageResponse<>();
        pageResponse.setPageNumber(pageNumber);
        pageResponse.setPageSize(pageSize);
        pageResponse.setTotalPages(page.getTotalPages());
        pageResponse.setTotalElements(page.getTotalElements());
        pageResponse.setLast(page.isLast());
        pageResponse.setContent(adminPayments);
        return pageResponse;
    }

    @Override
    public PageResponse<AdminPayment> getSuccessAdminPayment(
            Integer pageNumber, Integer pageSize, String sortBy, String sortDir
    ) {
        Pageable pageable = PageUtils.getPageable(pageNumber, pageSize, sortBy, sortDir);
        Page<AdminPayment> page = this.adminPaymentRepository.getSuccessAdminPayment(pageable);

        List<AdminPayment> adminPayments = page.getContent();
        PageResponse<AdminPayment> pageResponse = new PageResponse<>();
        pageResponse.setPageNumber(pageNumber);
        pageResponse.setPageSize(pageSize);
        pageResponse.setTotalPages(page.getTotalPages());
        pageResponse.setTotalElements(page.getTotalElements());
        pageResponse.setLast(page.isLast());
        pageResponse.setContent(adminPayments);
        return pageResponse;
    }

    @Override
    public Payment getPaymentOfCourse(UUID userId, UUID courseId) {
        Optional<Payment> payment = this.paymentRepository.getPaymentOfCourse(userId, courseId);
        if(payment.isPresent()) {
            return payment.get();
        } else {
            throw new IllegalStateException("Người dùng chưa mua khóa học này!");
        }
    }

    @Override
    public PageResponse<Payment> getPaymentsOfUser(
            Integer pageNumber,
            Integer pageSize,
            String sortBy,
            String sortDir,
            UUID userId
    ) {
        this.userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId));
        Pageable pageable = PageUtils.getPageable(pageNumber, pageSize, sortBy, sortDir);
        Page<Payment> page = this.paymentRepository.getPaymentsOfUser(pageable,userId);

        List<Payment> payments = page.getContent();
        PageResponse<Payment> pageResponse = new PageResponse<>();
        pageResponse.setContent(payments);
        pageResponse.setTotalPages(page.getTotalPages());
        pageResponse.setTotalElements(page.getTotalElements());
        pageResponse.setPageSize(pageSize);
        pageResponse.setPageNumber(pageNumber);
        pageResponse.setLast(page.isLast());
        return pageResponse;
    }

    @Override
    public AdminPayment getMonthlyPaymentOfUser(UUID userId, String yearMonth) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId));
        List<AdminPayment> adminPayments = user.getAdminPayments();
        Optional<AdminPayment> now = adminPayments.stream()
                .filter(monthlyPayment -> AppUtils.formatYearMonthToString(monthlyPayment.getMonthOfYear()).equals(yearMonth))
                .findFirst();
        if(now.isPresent()) {
            return now.get();
        } else {
            return null;
        }
    }

    @Override
    public PageResponse<AdminPayment> getAllAdminPaymentsOfUser(
            Integer pageNumber,
            Integer pageSize,
            String sortBy,
            String sortDir,
            UUID userId
    ) {
        Pageable pageable = PageUtils.getPageable(pageNumber, pageSize, sortBy, sortDir);
        Page<AdminPayment> page = this.adminPaymentRepository.getAllAdminPaymentsOfUser(pageable,userId);

        List<AdminPayment> payments = page.getContent();
        PageResponse<AdminPayment> pageResponse = new PageResponse<>();
        pageResponse.setContent(payments);
        pageResponse.setTotalPages(page.getTotalPages());
        pageResponse.setTotalElements(page.getTotalElements());
        pageResponse.setPageSize(pageSize);
        pageResponse.setPageNumber(pageNumber);
        pageResponse.setLast(page.isLast());
        return pageResponse;
    }

    @Override
    public Double getCurrentRevenueOfMentor(UUID mentorId) {
        return paymentRepository.getCurrentRevenueOfMentor(mentorId);
    }

    @Override
    public String vnpayPayment(HttpServletRequest req, HttpServletResponse res) throws IOException {
        String vnp_Version = "2.1.0";
        String vnp_Command = "pay";
        String orderType = "other";
        long amount = Integer.parseInt(req.getParameter("amount"));
        String bankCode = req.getParameter("bankCode");
        UUID courseId = UUID.fromString(req.getParameter("courseId"));
        UUID userId = UUID.fromString(req.getParameter("userId"));

        // TODO: Tao vnpay data
        String vnp_TxnRef = UUID.randomUUID().toString();
        VnpayPayment vnpayPayment = new VnpayPayment();
        vnpayPayment.setId(vnp_TxnRef);
        vnpayPayment.setAmount(amount);
        vnpayPayment.setVnpayPaymentStatus(VnpayPaymentStatus.PENDING);
        vnpayPayment.setUserId(userId);
        vnpayPayment.setCourseId(courseId);
        this.vnpayPaymentRepository.save(vnpayPayment);

        String vnp_IpAddr = vnpayConfig.getIpAddress(req);

        String vnp_TmnCode = vnpayConfig.vnp_TmnCode;

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", vnp_Version);
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amount * 100));
        vnp_Params.put("vnp_CurrCode", "VND");

        if (bankCode != null && !bankCode.isEmpty()) {
            vnp_Params.put("vnp_BankCode", bankCode);
        }
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", "Thanh toan don hang:" + vnp_TxnRef);
        vnp_Params.put("vnp_OrderType", orderType);

        String locate = req.getParameter("language");
        if (locate != null && !locate.isEmpty()) {
            vnp_Params.put("vnp_Locale", locate);
        } else {
            vnp_Params.put("vnp_Locale", "vn");
        }
        vnp_Params.put("vnp_ReturnUrl", vnpayConfig.vnp_ReturnUrl);
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        cld.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        List fieldNames = new ArrayList(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = (String) itr.next();
            String fieldValue = (String) vnp_Params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                //Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                //Build query
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }
        String queryUrl = query.toString();
        String vnp_SecureHash = vnpayConfig.hmacSHA512(vnpayConfig.secretKey, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        String paymentUrl = vnpayConfig.vnp_PayUrl + "?" + queryUrl;
        com.google.gson.JsonObject job = new JsonObject();
        job.addProperty("code", "00");
        job.addProperty("message", "success");
        job.addProperty("data", paymentUrl);
        return paymentUrl;
    }

    @Override
    public void createPaymentVNPAY(HttpServletRequest request) {
        try {
            Map fields = new HashMap();
            for (Enumeration params = request.getParameterNames(); params.hasMoreElements();) {
                String fieldName = URLEncoder.encode((String) params.nextElement(), StandardCharsets.US_ASCII.toString());
                String fieldValue = URLEncoder.encode(request.getParameter(fieldName), StandardCharsets.US_ASCII.toString());
                if ((fieldValue != null) && (fieldValue.length() > 0)) {
                    fields.put(fieldName, fieldValue);
                }
            }

            String vnp_SecureHash = request.getParameter("vnp_SecureHash");
            if (fields.containsKey("vnp_SecureHashType")) {
                fields.remove("vnp_SecureHashType");
            }
            if (fields.containsKey("vnp_SecureHash")) {
                fields.remove("vnp_SecureHash");
            }

            String signValue = vnpayConfig.hashAllFields(fields);
            if (signValue.equals(vnp_SecureHash)) {
//                boolean checkOrderId = true; // vnp_TxnRef exists in your database
//                boolean checkAmount = true; // vnp_Amount is valid (Check vnp_Amount VNPAY returns compared to the amount of the code (vnp_TxnRef) in the Your database).
//                boolean checkOrderStatus = true; // PaymentStatus = 0 (pending)
                String vnp_TxRef = request.getParameter("vnp_TxnRef");

                VnpayPayment vnpayPayment = vnpayPaymentRepository.findById(vnp_TxRef)
                        .orElseThrow(() -> new ResourceNotFoundException("VnpayPayment", "Id", vnp_TxRef));

                long amount = Long.parseLong(request.getParameter("vnp_Amount")) / 100;

                if(vnpayPayment.getAmount() == amount) {
                    if (vnpayPayment.getVnpayPaymentStatus().equals(VnpayPaymentStatus.PENDING)) {
                        if ("00".equals(request.getParameter("vnp_ResponseCode"))) {
                            // Create payment
                            Course course = this.courseRepository.findById(vnpayPayment.getCourseId())
                                    .orElseThrow(() -> new ResourceNotFoundException("Course", "Id", vnpayPayment.getCourseId()));
                            User user = this.userRepository.findById(vnpayPayment.getUserId())
                                    .orElseThrow(() -> new ResourceNotFoundException("User", "Id", vnpayPayment.getUserId()));
                            Payment payment = new Payment();
                            payment.setAmount(amount);
                            payment.setCourse(course);
                            payment.setUser(user);
                            payment.setPaymentStatus(PaymentStatus.SUCCESS);
                            this.paymentRepository.save(payment);

                            // TODO: Cap nhat so du tai khoan mentor
                            User mentor = course.getUser();
                            AccountBalance accountBalance = null;
                            if(mentor.getAccountBalance() == null) {
                                accountBalance = new AccountBalance();
                                accountBalance.setUser(mentor);
                                accountBalance.setUserBalance(amount * 0.95);
                            } else {
                                accountBalance = mentor.getAccountBalance();
                                accountBalance.setUserBalance(accountBalance.getUserBalance() + amount * 0.95);
                            }

                            this.accountBalanceRepository.save(accountBalance);

//                            Optional<AdminPayment> adminPaymentOptional = user.getAdminPayments().stream()
//                                    .filter(adminPayment -> adminPayment.getMonthOfYear().equals(YearMonth.now()))
//                                    .findFirst();
//                            AdminPayment adminPayment;
//                            if(adminPaymentOptional.isPresent()) {
//                                adminPayment = adminPaymentOptional.get();
//                                adminPayment.setPrincipalAmount(adminPayment.getPrincipalAmount() + amount);
//                                adminPayment.setAmountPaid(adminPayment.getAmountPaid() + amount * 0.95);
//                                this.adminPaymentRepository.save(adminPayment);
//                            } else {
//                                adminPayment = new AdminPayment();
//                                adminPayment.setUser(payment.getCourse().getUser());
//                                adminPayment.setAdminPaymentStatus(AdminPaymentStatus.PENDING);
//                                adminPayment.setMonthOfYear(YearMonth.now());
//                                adminPayment.setPrincipalAmount(amount);
//                                adminPayment.setAmountPaid(amount * 0.95);
//                                this.adminPaymentRepository.save(adminPayment);
//                            }

                            // TODO: Luu lai trang thai thanh toan
                            vnpayPayment.setVnpayPaymentStatus(VnpayPaymentStatus.SUCCESS);
                            this.vnpayPaymentRepository.save(vnpayPayment);
                        } else {
                            vnpayPayment.setVnpayPaymentStatus(VnpayPaymentStatus.FAILED);
                            this.vnpayPaymentRepository.save(vnpayPayment);
                        }
                        System.out.print ("{\"RspCode\":\"00\",\"Message\":\"Confirm Success\"}");
                    } else {
                        System.err.print("{\"RspCode\":\"02\",\"Message\":\"Order already confirmed\"}");
                    }
                } else {
                    System.err.print("{\"RspCode\":\"04\",\"Message\":\"Invalid Amount\"}");
                }
            } else {
                System.err.print("{\"RspCode\":\"97\",\"Message\":\"Invalid Checksum\"}");
            }
        } catch(Exception e) {
            System.err.print(e.getMessage());
        }
    }

    @Override
    public AdminPayment withDrawMoney(UUID userId, Double amount) {
        User user = this.userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId));
        if(amount > user.getAccountBalance().getUserBalance()) {
            throw new IllegalStateException("Số dư tài khoản không đủ");
        }
        AccountBalance accountBalance = user.getAccountBalance();
        accountBalance.setLastedTransactionDate(LocalDateTime.now());

        AdminPayment adminPayment = new AdminPayment();
        adminPayment.setUser(user);
        adminPayment.setAdminPaymentStatus(AdminPaymentStatus.PENDING);
        adminPayment.setMonthOfYear(YearMonth.now());
        adminPayment.setAmount(amount);
        return this.adminPaymentRepository.save(adminPayment);
    }

    @Override
    public String adminVnpayPayment(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String vnp_Version = "2.1.0";
        String vnp_Command = "pay";
        String orderType = "other";
        long amount = Integer.parseInt(request.getParameter("amount"));
        String bankCode = request.getParameter("bankCode");
        UUID adminPaymentId = UUID.fromString(request.getParameter("adminPaymentId"));

        String vnp_TxnRef = UUID.randomUUID().toString();

        // TODO: Tao vnpay data
        VnpayPayment vnpayPayment = new VnpayPayment();
        vnpayPayment.setId(vnp_TxnRef);
        vnpayPayment.setAmount(amount);
        vnpayPayment.setVnpayPaymentStatus(VnpayPaymentStatus.PENDING);
        vnpayPayment.setAdminPaymentId(adminPaymentId);
        this.vnpayPaymentRepository.save(vnpayPayment);

        String vnp_IpAddr = vnpayConfig.getIpAddress(request);
        String vnp_TmnCode = vnpayConfig.vnp_TmnCode;

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", vnp_Version);
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amount * 100));
        vnp_Params.put("vnp_CurrCode", "VND");

        if (bankCode != null && !bankCode.isEmpty()) {
            vnp_Params.put("vnp_BankCode", bankCode);
        }
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", "Thanh toan don hang:" + vnp_TxnRef);
        vnp_Params.put("vnp_OrderType", orderType);

        String locate = request.getParameter("language");
        if (locate != null && !locate.isEmpty()) {
            vnp_Params.put("vnp_Locale", locate);
        } else {
            vnp_Params.put("vnp_Locale", "vn");
        }
        vnp_Params.put("vnp_ReturnUrl", vnpayConfig.vnp_ReturnUrl);
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        cld.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        List fieldNames = new ArrayList(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = (String) itr.next();
            String fieldValue = (String) vnp_Params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                //Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                //Build query
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }
        String queryUrl = query.toString();
        String vnp_SecureHash = vnpayConfig.hmacSHA512(vnpayConfig.secretKey, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        String paymentUrl = vnpayConfig.vnp_PayUrl + "?" + queryUrl;
        com.google.gson.JsonObject job = new JsonObject();
        job.addProperty("code", "00");
        job.addProperty("message", "success");
        job.addProperty("data", paymentUrl);
        return paymentUrl;
    }

    @Override
    public void resolveAdminVnpayPayment(HttpServletRequest request) {
        try {
            Map fields = new HashMap();
            for (Enumeration params = request.getParameterNames(); params.hasMoreElements();) {
                String fieldName = URLEncoder.encode((String) params.nextElement(), StandardCharsets.US_ASCII.toString());
                String fieldValue = URLEncoder.encode(request.getParameter(fieldName), StandardCharsets.US_ASCII.toString());
                if ((fieldValue != null) && (fieldValue.length() > 0)) {
                    fields.put(fieldName, fieldValue);
                }
            }

            String vnp_SecureHash = request.getParameter("vnp_SecureHash");
            if (fields.containsKey("vnp_SecureHashType")) {
                fields.remove("vnp_SecureHashType");
            }
            if (fields.containsKey("vnp_SecureHash")) {
                fields.remove("vnp_SecureHash");
            }

            String signValue = vnpayConfig.hashAllFields(fields);
            if (signValue.equals(vnp_SecureHash)) {
                String vnp_TxRef = request.getParameter("vnp_TxnRef");

                long amount = Long.parseLong(request.getParameter("vnp_Amount")) / 100;

                VnpayPayment vnpayPayment = this.vnpayPaymentRepository.findById(vnp_TxRef)
                        .orElseThrow(() -> new ResourceNotFoundException("VnpayAdminPayment", "Id", vnp_TxRef));

                if(vnpayPayment.getAmount() == amount) {
                    if (vnpayPayment.getVnpayPaymentStatus().equals(VnpayPaymentStatus.PENDING)) {
                        if ("00".equals(request.getParameter("vnp_ResponseCode"))) {
                            UUID adminPaymentId = vnpayPayment.getAdminPaymentId();
                            AdminPayment adminPayment = this.adminPaymentRepository.findById(adminPaymentId)
                                            .orElseThrow(() -> new ResourceNotFoundException("AdminPayment", "Id", adminPaymentId));
                            adminPayment.setAdminPaymentStatus(AdminPaymentStatus.SUCCESS);
                            adminPayment.setPaymentAt(LocalDateTime.now());
                            this.adminPaymentRepository.save(adminPayment);

                            // TODO: Cap nhat so du ngươi dung
                            AccountBalance accountBalance = adminPayment.getUser().getAccountBalance();
                            accountBalance.setUserBalance(accountBalance.getUserBalance() - adminPayment.getAmount());
                            this.accountBalanceRepository.save(accountBalance);

                            vnpayPayment.setVnpayPaymentStatus(VnpayPaymentStatus.SUCCESS);
                            this.vnpayPaymentRepository.save(vnpayPayment);
                        } else {
                            vnpayPayment.setVnpayPaymentStatus(VnpayPaymentStatus.FAILED);
                            this.vnpayPaymentRepository.save(vnpayPayment);
                        }
                        System.out.print("{\"RspCode\":\"00\",\"Message\":\"Confirm Success\"}");
                    } else {
                        System.err.print("{\"RspCode\":\"02\",\"Message\":\"Order already confirmed\"}");
                    }
                } else {
                    System.err.print("{\"RspCode\":\"04\",\"Message\":\"Invalid Amount\"}");
                }
            } else {
                System.err.print("{\"RspCode\":\"97\",\"Message\":\"Invalid Checksum\"}");
            }
        } catch(Exception e) {
            System.err.print(e.getMessage());
        }
    }

    @Override
    public List<String> getAllEmailMentorHasPaymentCurrentMonth() {
        List<Payment> payments = this.paymentRepository.findAll();
        List<Payment> paymentHasSuccess = payments.stream()
                .filter(payment -> payment.getPaymentStatus().equals(PaymentStatus.SUCCESS))
                .collect(Collectors.toList());
        List<String> listEmailToSend = paymentHasSuccess.stream()
                .map(payment -> payment.getCourse().getUser().getEmail())
                .collect(Collectors.toList());

        return listEmailToSend;
    }

    @Override
    public ByteArrayInputStream getExcelDataOfMentorPayment(String email) throws IOException {
        Optional<User> userNullable = this.userRepository.findByEmail(email);
        if(!userNullable.isPresent()) {
            return null;
        } else {
            User user = userNullable.get();
            if(!user.isInstructor() || user.isLock()) {
                return null;
            } else {
                List<Payment> payments = this.paymentRepository.findAll();
//                List<Payment> paymentsOfMentor = payments.stream()
//                        .filter(payment -> payment.getCourse().getUser().getId().equals(user.getId()))
//                        .collect(Collectors.toList());
                ByteArrayInputStream excelData = ExcelUtils.paymentsOfMentorToExcel(payments);
                return excelData;
            }
        }
    }

    @Override
    public ByteArrayInputStream getExcelPaymentsInMonthOfUser(UUID adminPaymentId) throws IOException {
        AdminPayment adminPayment = this.adminPaymentRepository.findById(adminPaymentId)
                .orElseThrow(() -> new ResourceNotFoundException("AdminPayment", "Id", adminPaymentId));
        List<Payment> payments = this.paymentRepository.findAll();
        int month = adminPayment.getMonthOfYear().getMonth().getValue();
        int year = adminPayment.getMonthOfYear().getYear();
        List<Payment> paymentsInMonth = payments.stream()
                .filter(
                        payment -> payment.getCreateAt().getMonth().getValue() == month
                        && payment.getCreateAt().getYear() == year
                        && payment.getCourse().getUser().getId().equals(adminPayment.getUser().getId())
                )
                .collect(Collectors.toList());
        long sumPayment = 0;
        long sumProfitPayment = 0;
        for(Payment payment : paymentsInMonth) {
            sumPayment += payment.getAmount();
            sumProfitPayment += payment.getAmount() * 0.95;
        }

        String sheetName = String.format("Giao dịch tháng %s", AppUtils.formatYearMonthToString(adminPayment.getMonthOfYear()));
        ByteArrayInputStream excelData = ExcelUtils.paymentsInMonthOfUserToExcel(
                paymentsInMonth, sumPayment, sumProfitPayment, sheetName );
        return excelData;
    }
}
