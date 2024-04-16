package com.inspiron.labs.app.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class StudentDto<T> {
    private String studentName;
    private Integer studentAge;
    private List<AddressDto> addresses;
}

