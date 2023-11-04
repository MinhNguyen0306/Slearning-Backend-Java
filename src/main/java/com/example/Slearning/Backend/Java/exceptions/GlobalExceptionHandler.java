package com.example.Slearning.Backend.Java.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    protected ResponseEntity<ApiErrorResponse> handleMethodArgNotValidException(
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

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(value = {ResourceNotFoundException.class})
    protected ResponseEntity<ApiErrorResponse> handleResourceNotFoundException(
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
                .errorCode(HttpStatus.FORBIDDEN.value())
                .error(HttpStatus.FORBIDDEN.name())
                .errorMessage(exception.getMessage())
                .build();
        return new ResponseEntity(errorResponse, HttpStatus.FORBIDDEN);
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
}
