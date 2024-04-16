package com.inspiron.labs.app.entity;

import lombok.*;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class Notification {
    private String notificationId;
    private String notificationType;
    private Student payload;
    private String createdOn;
}