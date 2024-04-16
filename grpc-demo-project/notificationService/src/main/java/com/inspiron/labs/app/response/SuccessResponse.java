package com.inspiron.labs.app.response;

import lombok.*;
import org.springframework.web.bind.annotation.ResponseBody;

@ResponseBody
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class SuccessResponse<T> {
    private boolean error;
    private String message;
    private T data;
}