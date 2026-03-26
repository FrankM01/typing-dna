package com.typingdna.config;

import com.typingdna.dto.response.ErrorResponse;
import com.typingdna.exception.AppException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolation(
            ConstraintViolationException ex, HttpServletRequest request) {
        List<String> errors = ex.getConstraintViolations()
                .stream()
                .map(v -> {
                    String[] parts = v.getPropertyPath().toString().split("\\.");
                    return parts[parts.length - 1] + ": " + v.getMessage();
                })
                .sorted()
                .toList();
        return build(request, HttpStatus.BAD_REQUEST, "Validación incorrecta", errors);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpServletRequest request) {
        List<String> errors = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .toList();
        return build(request, HttpStatus.BAD_REQUEST, "Validación incorrecta", errors);
    }

    @ExceptionHandler(AppException.class)
    public ResponseEntity<ErrorResponse> handleAppException(
            AppException ex, HttpServletRequest request) {
        return build(request, ex.getStatus(), ex.getMessage(), null);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentials(
            BadCredentialsException ex, HttpServletRequest request) {
        return build(request, HttpStatus.UNAUTHORIZED, "Usuario o contraseña incorrectos", null);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(
            Exception ex, HttpServletRequest request) {
        return build(request, HttpStatus.INTERNAL_SERVER_ERROR, "Error interno del servidor", null);
    }

    private ResponseEntity<ErrorResponse> build(HttpServletRequest request,
                                                HttpStatus status,
                                                String message,
                                                List<String> errors) {
        String path = request.getRequestURI();
        String query = request.getQueryString();
        String fullPath = query != null ? path + "?" + query : path;

        return ResponseEntity.status(status).body(ErrorResponse.builder()
                .status(status.value())
                .message(message)
                .errors(errors)
                .timestamp(LocalDateTime.now())
                .path(request.getMethod() + ": " + fullPath)
                .build());
    }
}
