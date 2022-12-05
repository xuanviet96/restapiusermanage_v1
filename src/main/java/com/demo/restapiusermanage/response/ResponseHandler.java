package com.demo.restapiusermanage.response;

import com.demo.restapiusermanage.entity.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class ResponseHandler {
    public static ResponseEntity<Object> responseBuilder(String message, HttpStatus httpStatus, Object responseObject){
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("message:", message);
        response.put("status:", "OK");
        response.put("data", responseObject);

        return new ResponseEntity<Object>(response, httpStatus);
    }
}
