package com.example.Slearning.Backend.Java.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver;

import java.io.File;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgNotValidException(
        MethodArgumentNotValidException e
    ) {
        String errorMessage = e
                .getBindingResult()
                .getFieldErrors()
                .stream().map(fieldError -> String.format("%s: %s", fieldError.getField(), fieldError.getDefaultMessage()))
                .collect(Collectors.joining("/n"));
        ApiErrorResponse errorResponse = ApiErrorResponse
                .builder()
                .timestamp(LocalDateTime.now())
                .error(e.getMessage())
                .errorCode(e.getStatusCode().value())
                .errorMessage(errorMessage)
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ApiErrorResponse> handleIllegalStateException(IllegalStateException e) {
        ApiErrorResponse errorResponse = ApiErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .errorCode(HttpStatus.BAD_REQUEST.value())
                .error(HttpStatus.BAD_REQUEST.name())
                .errorMessage(e.getMessage())
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleResourceNotFoundException(
        ResourceNotFoundException e
    ) {
        ApiErrorResponse errorResponse = ApiErrorResponse
                .builder()
                .timestamp(LocalDateTime.now())
                .errorCode(HttpStatus.NOT_FOUND.value())
                .error(HttpStatus.NOT_FOUND.name())
                .errorMessage(e.getMessage())
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<?> handleApiException(ApiException exception) {
        ApiErrorResponse errorResponse = ApiErrorResponse
                .builder()
                .timestamp(LocalDateTime.now())
                .errorCode(HttpStatus.BAD_REQUEST.value())
                .error(HttpStatus.BAD_REQUEST.name())
                .errorMessage(exception.getMessage())
                .build();
        return new ResponseEntity(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DuplicateException.class)
    public ResponseEntity<?> handleDuplicateException(DuplicateException exception) {
        ApiErrorResponse errorResponse = ApiErrorResponse
                .builder()
                .timestamp(LocalDateTime.now())
                .errorCode(HttpStatus.BAD_REQUEST.value())
                .error(HttpStatus.BAD_REQUEST.name())
                .errorMessage(exception.getMessage())
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<?> handleFileSizeExcessException(MaxUploadSizeExceededException exception) {
        ApiErrorResponse errorResponse = ApiErrorResponse
                .builder()
                .timestamp(LocalDateTime.now())
                .errorCode(HttpStatus.BAD_REQUEST.value())
                .error(HttpStatus.BAD_REQUEST.name())
                .errorMessage(exception.getMessage())
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UploadFileException.class)
    public ResponseEntity<?> handleUploadFileException(UploadFileException exception) {
        ApiErrorResponse errorResponse = ApiErrorResponse
                .builder()
                .timestamp(LocalDateTime.now())
                .error(HttpStatus.INTERNAL_SERVER_ERROR.name())
                .errorCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .errorMessage(exception.getMessage())
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
