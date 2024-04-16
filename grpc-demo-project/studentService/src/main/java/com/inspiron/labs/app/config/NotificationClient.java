package com.inspiron.labs.app.config;

import com.inspiron.labs.app.entity.Notification;
import com.inspiron.labs.app.response.SuccessResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

//@FeignClient(value = "notificationFeign", url = "${notification.url}")
//public interface NotificationClient {
//    @GetMapping("/get")
//    ResponseEntity<SuccessResponse<List<Notification>>> getAllNotification();
//    @GetMapping("/getPagination")
//    ResponseEntity<SuccessResponse<Page<Notification>>> getAllNotificationPaginaton(@RequestParam(required = false) Integer pageNo, @RequestParam(required = false) Integer pageSize, @RequestParam(required = false) String sortBy, @RequestParam(required = false) String sortOrder);
//}
