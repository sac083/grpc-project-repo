package com.inspiron.labs.app.service;

import com.google.protobuf.Descriptors;
import com.inspiron.labs.app.dto.StudentDto;
import com.inspiron.labs.app.dto.StudentUpdateDto;
import com.inspiron.labs.app.entity.Notification;
import com.inspiron.labs.app.entity.Student;
import com.inspiron.notification.NotificationPagesRequest;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface StudentService {

    public Student saveStudent(StudentDto studentDto);

    public Student updateStudent(StudentUpdateDto studentUpdateDto, String studentId);

    public Student fetchStudent(String studentId);

    public Boolean deleteStudent(String studentId);

    public List<String> getUniqueCities();

//    Map<Descriptors.FieldDescriptor, Object> getNotificationById(NotificationRequest notificationRequest);

    List<Notification> getNotificationPages(NotificationPagesRequest notificationPagesRequest);

    List<Notification> getAllNotifications();
}
