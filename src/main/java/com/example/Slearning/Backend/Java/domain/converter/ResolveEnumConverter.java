package com.example.Slearning.Backend.Java.domain.converter;

import com.example.Slearning.Backend.Java.utils.enums.ResolveStatus;
import org.springframework.core.convert.converter.Converter;

public class ResolveEnumConverter implements Converter<String, ResolveStatus> {

    @Override
    public ResolveStatus convert(String source) {
        return ResolveStatus.valueOf(source.toUpperCase());
    }
}
