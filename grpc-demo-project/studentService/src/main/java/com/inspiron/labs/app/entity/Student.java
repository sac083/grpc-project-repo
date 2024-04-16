package com.inspiron.labs.app.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "students")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class Student {
    @Id
    private String studentId;
    private String studentName;
    private Integer studentAge;
    private List<Address> addresses;
}