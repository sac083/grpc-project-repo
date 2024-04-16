package com.inspiron.labs.app.controller;

import com.inspiron.labs.app.entity.Notification;
import com.inspiron.labs.app.entity.Student;
import com.inspiron.labs.app.response.SuccessResponse;
import com.inspiron.labs.app.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/notification", produces = "application/json")
public class NotificationController {
    private final NotificationService notificationService;
    @PostMapping("/sendNotification")
    public ResponseEntity<SuccessResponse<String>> sendNotification(@RequestBody Student student) {
        return ResponseEntity.ok(SuccessResponse.<String>builder().error(false).data(notificationService.sendNotification(student))
                .message("Student saved successfully").build());

    }
    @GetMapping("/get")
    public ResponseEntity<SuccessResponse<List<Notification>>> getAllNotification() {
        return ResponseEntity.ok(SuccessResponse.<List<Notification>>builder().error(false).data(notificationService.getAllNotification())
                .message("Student saved successfully").build());
    }
    @GetMapping("/getPagination")
    public ResponseEntity<SuccessResponse<Page<Notification>>> getAllNotificationPaginaton(@RequestParam(required = false) Integer pageNo, @RequestParam(required = false) Integer pageSize, @RequestParam(required = false) String sortBy, @RequestParam(required = false) String sortOrder) {
        return ResponseEntity.ok(SuccessResponse.<Page<Notification>>builder().error(false).data(notificationService.getAllNotificationPaginaton(pageNo, pageSize, sortBy, sortOrder))
                .message("Student saved successfully").build());

    }
    @PutMapping("/same-groupId")
    public ResponseEntity<SuccessResponse<String>> listenWithSameGroupId(@RequestBody Student student) {
        return ResponseEntity.ok(SuccessResponse.<String>builder().error(false).data(notificationService.listenWithSameGroupId(student))
                .message("Student saved successfully").build());
    }
    @PutMapping("/same-groupId-two")
    public ResponseEntity<SuccessResponse<String>> listenWithSameGroupIdSecond(@RequestBody Student student) {
        return ResponseEntity.ok(SuccessResponse.<String>builder().error(false).data(notificationService.listenWithSameGroupIdSecond(student))
                .message("Student saved successfully").build());
    }
    @PutMapping("/listen-student-update")
    public ResponseEntity<SuccessResponse<String>> listenStudentUpdateMethod(@RequestBody Student student) {
        return ResponseEntity.ok(SuccessResponse.<String>builder().error(false).data(notificationService.listenStudentUpdateMethod(student))
                .message("Student saved successfully").build());
    }
    @PutMapping("/listen-student-delete")
    public ResponseEntity<SuccessResponse<String>> listenStudentDeleteMethod(@RequestBody Student student) {
        return ResponseEntity.ok(SuccessResponse.<String>builder().error(false).data(notificationService.listenStudentDeleteMethod(student))
                .message("Student saved successfully").build());
    }
}
