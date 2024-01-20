package com.example.Slearning.Backend.Java.controllers;

import com.example.Slearning.Backend.Java.configs.MonthlyPaymentBatchConfig;
import com.example.Slearning.Backend.Java.services.EmailService;
import com.example.Slearning.Backend.Java.services.PaymentService;
import com.example.Slearning.Backend.Java.utils.AppUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Date;
import java.util.List;

@Component
@EnableScheduling
//@RestController
//@RequestMapping("api/v1/mail")
@RequiredArgsConstructor
public class MailController {

    private final PaymentService paymentService;

    private final EmailService emailService;

    private final JobLauncher jobLauncher;

    private final MonthlyPaymentBatchConfig monthlyPaymentBatchConfig;

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final DataSource dataSource;

    @Scheduled(cron = "@hourly")
    public void sendEmail() throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        jobLauncher.run(monthlyPaymentBatchConfig.emailSenderJob(jobRepository,
                monthlyPaymentBatchConfig.emailSenderStep(
                        jobRepository, transactionManager, dataSource)), new JobParametersBuilder()
                .addDate("launchDate", new Date()).toJobParameters());
    }

//    @Scheduled(cron = "8 45 9 * * ?")
//    @GetMapping
//    public void sendMonthlyPaymentEmail() throws IOException {
//        String yearMonth = AppUtils.formatYearMonthToString(YearMonth.now());
//        List<String> listEmailToSend = this.paymentService.getAllEmailMentorHasPaymentCurrentMonth();
//
//        if(listEmailToSend.size() > 0) {
//            String[] array = listEmailToSend.toArray(new String[0]);
//            String subject = String.format("Thanh toán tháng %s từ hệ thống", yearMonth);
//            String text = "Bạn có các hóa đơn cần thanh toán tháng này từ hệ thống. Tải file excel bên dưới" +
//                    "về để xem chi tiết các khóa học cần thanh toán";
//            ByteArrayInputStream excelData = this.paymentService.getExcelDataOfMentorPayment("minhnguyen183385@gmail.com");
//            emailService.sendMonthlyPaymentMail(subject, array, text, excelData);
//        } else {
//            System.err.println("Khong co thanh toan nao thang nay");
//        }
//    }
}
