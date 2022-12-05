package com.demo.restapiusermanage.exception;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@ControllerAdvice
public class GlobalExceptionHandler {

    @Qualifier("messageSource")
    @Autowired
    private MessageSource msgSource;


    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request){
        ErrorMessage errorMessage = new ErrorMessage("NOK", new Date(), ex.getMessage());
        return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationFailException(MethodArgumentNotValidException ex){

        Map<String, List> listErrors = new HashMap<>();
        Map<String, Object> errorsDetail = new LinkedHashMap<>();

        BindingResult result = ex.getBindingResult();
        List<FieldError> fieldErrors = result.getFieldErrors();

        for (FieldError err : fieldErrors) {

            if (!listErrors.containsKey(err.getField())) {
                List<String> listErrorDefaultMessage = new ArrayList<>();

                listErrorDefaultMessage.add(err.getDefaultMessage());

                listErrors.put(err.getField(), listErrorDefaultMessage) ;
            } else {
                String key = err.getField();
                listErrors.get(key).add(err.getDefaultMessage());
            }

        }

        errorsDetail.put("status", "NOK");
        errorsDetail.put("message", "validation failed ");
        errorsDetail.put("errors",listErrors);
        return new ResponseEntity<>(errorsDetail, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> CommonException(){
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }




}
