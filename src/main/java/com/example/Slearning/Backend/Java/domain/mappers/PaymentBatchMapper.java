package com.example.Slearning.Backend.Java.domain.mappers;

import com.example.Slearning.Backend.Java.domain.entities.Payment;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PaymentBatchMapper implements RowMapper<Payment> {

    @Override
    public Payment mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Payment.builder()
                .amount(rs.getLong("amount"))
                .build();
    }
}
