package com.inspiron.labs.app.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "notificationTracker")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class Notification {
    @Id
    private String notificationId;
    private String notificationType;
    private Student payload;
    private Date createdOn;
}
