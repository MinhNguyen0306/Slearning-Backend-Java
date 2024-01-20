package com.example.Slearning.Backend.Java.services.batch.payment;

import com.example.Slearning.Backend.Java.domain.entities.Payment;
import com.example.Slearning.Backend.Java.repositories.PaymentRepository;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@StepScope
public class PaymentWriter implements ItemWriter<Payment> {

    @Autowired
    private PaymentRepository paymentRepository;

    @Override
    public void write(Chunk<? extends Payment> chunk) throws Exception {
//        paymentRepository.saveAll(chunk);
    }
}
