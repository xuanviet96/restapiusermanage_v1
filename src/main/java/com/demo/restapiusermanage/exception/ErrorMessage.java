package com.demo.restapiusermanage.exception;

import org.springframework.http.HttpStatus;

import java.util.Date;

public class ErrorMessage {

    private Date timestamp;
    private String status;
    private String message;

    public ErrorMessage(String status, Date timestamp, String message) {
        this.status = status;
        this.timestamp = timestamp;
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }

}
