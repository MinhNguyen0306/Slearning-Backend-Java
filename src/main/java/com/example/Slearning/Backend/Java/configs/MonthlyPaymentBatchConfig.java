package com.example.Slearning.Backend.Java.configs;

import com.example.Slearning.Backend.Java.domain.entities.Payment;
import com.example.Slearning.Backend.Java.domain.mappers.PaymentBatchMapper;
import com.example.Slearning.Backend.Java.services.batch.payment.PaymentProcessor;
import com.example.Slearning.Backend.Java.services.batch.payment.PaymentWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Slf4j
@Component
public class MonthlyPaymentBatchConfig {

    @Autowired
    private PaymentWriter paymentWriter;

    @Autowired
    private PaymentProcessor paymentProcessor;

    @Bean
    public Job emailSenderJob(JobRepository jobRepository, Step emailSenderStep) {
        return new JobBuilder("emailSenderJob", jobRepository)
                .start(emailSenderStep)
                .build();
    }

    @Bean
    public Step emailSenderStep(JobRepository jobRepository, PlatformTransactionManager transactionManager,
                                DataSource dataSource) {
        return new StepBuilder("emailSenderStep", jobRepository)
                .<Payment, Payment>chunk(100, transactionManager)
                .reader(paymentItemReader(dataSource))
                .processor(paymentProcessor)
                .writer(paymentWriter)
                .build();
    }

    @Bean
    public ItemReader<Payment> paymentItemReader(DataSource dataSource) {
        String sql = "SELECT * FROM payments p WHERE p.user_payment_course_status = 'SUCCESS'";
        return new JdbcCursorItemReaderBuilder<Payment>()
                .name("paymentItemReader")
                .sql(sql)
                .dataSource(dataSource)
                .rowMapper(new PaymentBatchMapper())
                .build();
    }
}
