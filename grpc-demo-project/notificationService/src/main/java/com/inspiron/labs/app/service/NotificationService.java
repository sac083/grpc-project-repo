package com.inspiron.labs.app.service;

import com.inspiron.labs.app.entity.Notification;
import com.inspiron.labs.app.entity.Student;
import org.springframework.data.domain.Page;

import java.util.List;

public interface NotificationService {
    public String sendNotification(Student student);

    public List<Notification> getAllNotification();

    public Page<Notification> getAllNotificationPaginaton(Integer pageNo, Integer pageSize, String sortBy, String sortOrder);

    public String listenWithSameGroupId(Student student);

    public String listenWithSameGroupIdSecond(Student student);
    public String listenStudentUpdateMethod(Student student);

    public String listenStudentDeleteMethod(Student student);

    Notification getNotificationById();
}
