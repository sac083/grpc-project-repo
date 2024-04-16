package com.inspiron.labs.app.dto;

import lombok.*;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class NotificationDto {
    private String notificationType;
    private StudentDto studentDto;
    private Date createdOn;
}
