package com.inspiron.labs.app.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class StudentDto {
    private String studentId;
    private String studentName;
    private Integer studentAge;
}
