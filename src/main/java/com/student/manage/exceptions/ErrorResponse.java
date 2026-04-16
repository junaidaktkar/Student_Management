package com.student.manage.exceptions;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

public class ErrorResponse {

    private String message;
    private HttpStatus status;
    private LocalDateTime timestamp;

    public ErrorResponse(String message, HttpStatus status, LocalDateTime timestamp) {
        this.message = message;
        this.status = status;
        this.timestamp = timestamp;
    }

    public String getMessage() { return message; }
    public HttpStatus getStatus() { return status; }
    public LocalDateTime getTimestamp() { return timestamp; }
}