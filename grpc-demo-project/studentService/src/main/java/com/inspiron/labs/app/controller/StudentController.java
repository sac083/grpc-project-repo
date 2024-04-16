package com.inspiron.labs.app.controller;

import com.google.protobuf.Descriptors;
import com.google.protobuf.Int32Value;
import com.inspiron.labs.app.dto.StudentDto;
import com.inspiron.labs.app.dto.StudentUpdateDto;
import com.inspiron.labs.app.entity.Notification;
import com.inspiron.labs.app.entity.Student;
import com.inspiron.labs.app.response.SuccessResponse;
import com.inspiron.labs.app.service.StudentService;
import com.inspiron.notification.Empty;
import com.inspiron.notification.NotificationPagesRequest;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/student")
public class StudentController {
    private final StudentService studentService;

    @PostMapping("/save")
    public ResponseEntity<SuccessResponse<Student>> saveStudent(@RequestBody StudentDto studentDto) {
        return ResponseEntity.ok(SuccessResponse.<Student>builder().error(false).data(studentService.saveStudent(studentDto))
                .message("Student saved successfully").build());

    }
    @PutMapping("/update/{studentId}")
    public ResponseEntity<SuccessResponse<Student>> updateStudent(@RequestBody StudentUpdateDto studentUpdateDto, @PathVariable("studentId") String studentId) {
        return ResponseEntity.ok(SuccessResponse.<Student>builder().error(false).data(studentService.updateStudent(studentUpdateDto, studentId))
                .message("Student updated successfully").build());

    }

    @GetMapping("/get/{studentId}")
    public ResponseEntity<SuccessResponse<Student>> fetchStudent(@PathVariable("studentId") String studentId) {
        return ResponseEntity.ok(SuccessResponse.<Student>builder().error(false).data(studentService.fetchStudent(studentId))
                .message("Student fetched successfully").build());

    }

    @DeleteMapping("/delete/{studentId}")
    public ResponseEntity<SuccessResponse<Boolean>> deleteStudent(@PathVariable("studentId") String studentId) {
        return ResponseEntity.ok(SuccessResponse.<Boolean>builder().error(false).data(studentService.deleteStudent(studentId))
                .message("Student delete successfully").build());
    }
    @GetMapping("/get/unique-cities")
    public ResponseEntity<SuccessResponse<List<String>>> getUniqueCities() {
        return ResponseEntity.ok(SuccessResponse.<List<String>>builder().error(false).data(studentService.getUniqueCities())
                .message("Cities fetched successfully").build());
    }
//    @GetMapping("/get-notificationById/{notificationId}")
//    public Map<Descriptors.FieldDescriptor, Object> getNotificationById(@PathVariable("notificationId") String notificationId) {
//        NotificationRequest notificationRequest = NotificationRequest.newBuilder().setNotificationId(notificationId).build();
//        return studentService.getNotificationById(notificationRequest);
//    }
    @GetMapping("/pages")
    public List<Notification> getNotificationPages(@RequestParam Integer pageNo, @RequestParam Integer pageSize, @RequestParam String sortBy, @RequestParam String sortOrder) {
        NotificationPagesRequest notificationPagesRequest = NotificationPagesRequest.newBuilder().setPageNo(Int32Value.of(pageNo)).setPageSize(Int32Value.of(pageSize)).setSortBy(sortBy).setSortOrder(sortOrder).build();
        return studentService.getNotificationPages(notificationPagesRequest);
    }
    @GetMapping("/notifications")
    public List<Notification> getAllNotifications() {
        return studentService.getAllNotifications();
    }
}