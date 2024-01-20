package com.example.Slearning.Backend.Java.services.batch.payment;

import com.example.Slearning.Backend.Java.domain.entities.Payment;
import com.example.Slearning.Backend.Java.services.EmailService;
import com.example.Slearning.Backend.Java.services.PaymentService;
import com.example.Slearning.Backend.Java.utils.AppUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.time.YearMonth;
import java.util.List;

@StepScope
@Component
@Slf4j
public class PaymentProcessor implements ItemProcessor<Payment, Payment> {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private EmailService emailService;

    @Override
    public Payment process(Payment item) throws Exception {
//        log.debug(String.valueOf(item.getUser().getEmail()));

        String yearMonth = AppUtils.formatYearMonthToString(YearMonth.now());
        List<String> listEmailToSend = this.paymentService.getAllEmailMentorHasPaymentCurrentMonth();

        if(listEmailToSend.size() > 0) {
            String[] array = listEmailToSend.toArray(new String[0]);
            String subject = String.format("Thanh toán tháng %s từ hệ thống", yearMonth);
            String text = "Bạn có các hóa đơn cần thanh toán tháng này từ hệ thống. Tải file excel bên dưới" +
                    "về để xem chi tiết các khóa học cần thanh toán";
            ByteArrayInputStream excelData = this.paymentService.getExcelDataOfMentorPayment("minhnguyen183385@gmail.com");
            emailService.sendMonthlyPaymentMail(subject, array, text, excelData);
        } else {
            System.err.println("Khong co thanh toan nao thang nay");
        }
        return item;
    }
}
