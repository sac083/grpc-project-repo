package com.inspiron.labs.app.response;

import org.springframework.web.bind.annotation.ResponseBody;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

