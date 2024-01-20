package com.example.Slearning.Backend.Java.configs;

import com.example.Slearning.Backend.Java.domain.converter.ResolveEnumConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new ResolveEnumConverter());
    }
}
