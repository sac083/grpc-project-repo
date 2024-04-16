package com.inspiron.labs.app.exception.handler;
import com.inspiron.labs.app.response.ErrorResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class NotificationControllerAdvice {
    public ErrorResponse handler(RuntimeException exe){
        return ErrorResponse.builder().error(true).message(exe.getMessage()).build();
    }
}
