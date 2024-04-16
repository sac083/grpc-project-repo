package com.inspiron.labs.app.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.protobuf.Int32Value;
import com.inspiron.labs.app.constants.AppConstants;
import com.inspiron.labs.app.dto.NotificationDto;
import com.inspiron.labs.app.dto.StudentDto;
import com.inspiron.labs.app.entity.Notification;
import com.inspiron.labs.app.entity.Student;
import com.inspiron.labs.app.exception.NotificationNotFoundException;
import com.inspiron.labs.app.repository.NotificationRepository;
import com.inspiron.notification.*;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Configuration
@GrpcService
@ComponentScan("com.inspiron.na")
public class NotificationServiceImpl extends NotificationServiceGrpc.NotificationServiceImplBase implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final ObjectMapper objectMapper;
    private final KafkaTemplate<String, NotificationDto> kafkaTemplate;


    @KafkaListener(topics = AppConstants.NEW_STUDENT_OBJECT, groupId = "user-group")
    @Override
    public String sendNotification(Student student) {
        log.info("Sending notification here {}", student);
        Notification notification = new Notification();
        notification.setNotificationType("add Student");
        notification.setPayload(student);
        notification.setCreatedOn(new Date());
        log.info("Printing notifications here {}", notification);
        notificationRepository.save(notification);
        return "Notification sent";
    }

    @Override
    public List<Notification> getAllNotification() {
        return Optional.of(notificationRepository.findAll())
                .orElseThrow(() -> new NotificationNotFoundException("Notification not found"));
    }

//    @Override
//    public void getAllNotification(Empty request, StreamObserver<NotificationList> responseObserver) {
//        super.getAllNotification(request, responseObserver);
//        List<Notification> allNotification = notificationServiceImpl.getAllNotification();
//        NotificationList notificationList = NotificationList.newBuilder().addAllNotifications(allNotification).build();
//        responseObserver.onNext(notificationList);
//        responseObserver.onCompleted();
//    }

    @Override
    public Page<Notification> getAllNotificationPaginaton(Integer pageNo, Integer pageSize, String sortBy, String sortOrder) {
        Sort.Direction direction = sortOrder.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Page<Notification> notificationPagesList = notificationRepository.findAll(PageRequest.of(pageNo, pageSize, Sort.by(direction, sortBy)));
        if (!notificationPagesList.isEmpty()) {
            return notificationPagesList;
        } else {
            throw new NotificationNotFoundException("Notification not found");
        }
    }

    @KafkaListener(topics = AppConstants.SAVE_STUDENT_OBJECT, groupId = "user-group1")
    @Override
    public String listenWithSameGroupId(Student student) {
        StudentDto studentDto = new StudentDto();
        BeanUtils.copyProperties(student, studentDto);
        log.info("Printing student details with same groupId One {}", studentDto);
        return "Listening with same groupId two";
    }

    @KafkaListener(topics = AppConstants.SAVE_STUDENT_OBJECT, groupId = "user-group2")
    @Override
    public String listenWithSameGroupIdSecond(Student student) {
        StudentDto studentDto = new StudentDto();
        BeanUtils.copyProperties(student, studentDto);
        log.info("Printing student details with same groupId two {}", studentDto);
        return "Listening with same groupId two";
    }

    @KafkaListener(topics = AppConstants.UPDATE_STUDENT_OBJECT, groupId = "user-group3")
    @Override
    public String listenStudentUpdateMethod(Student student) {
        StudentDto studentDto = new StudentDto();
        BeanUtils.copyProperties(student, studentDto);
        log.info("Printing student update object {}", studentDto);
        return null;
    }

    @KafkaListener(topics = AppConstants.DELETE_STUDENT_OBJECT, groupId = "user-group")
    @Override
    public String listenStudentDeleteMethod(Student student) {
        StudentDto studentDto = new StudentDto();
        BeanUtils.copyProperties(student, studentDto);
        log.info("Printing student delete object {}", studentDto);
        return null;
    }

    /* the getAllNotification() method serves as a server-side implementation for handling requests to
    retrieve all notifications. It retrieves notifications from a repository, transforms them into
    protobuf messages, and sends them back to the client. This method ensures efficient communication
    between the server and the client in a gRPC-based application.*/
    @Override
    public void getAllNotification(Empty request, StreamObserver<NotificationList> responseObserver) {
        // Create an Empty message using the generated Empty.Builder class.
        Empty empty = Empty.newBuilder().build();

        // Retrieve all notifications from the repository.
        List<Notification> notifications = notificationRepository.findAll();

        // Create a builder for NotificationList, which will contain the list of NotificationResponse.
        NotificationList.Builder notifictionListBuilder = NotificationList.newBuilder();

        // Map each Notification object to a NotificationResponse object.
        List<NotificationResponse> notificationResponseList = notifications.stream().map(notification -> {
            // Extract the Student object from the Notification.
            Student student = notification.getPayload();

            // Map each Address object in the Student's addresses list to an AddressResponse object.
            List<AddressResponse> addressResponses = student.getAddresses().stream()
                    .map(address -> AddressResponse.newBuilder()
                            .setCity(address.getCity())
                            .setPincode(address.getPinCode())
                            .build())
                    .collect(Collectors.toList());

            // Create a StudentResponse object using the mapped AddressResponse objects.
            StudentResponse studentResponse = StudentResponse.newBuilder()
                    .setStudentName(student.getStudentName())
                    .setStudentId(student.getStudentId())
                    .setStudentAge(student.getStudentAge())
                    .addAllAddressResponses(addressResponses)
                    .build();

            // Create a NotificationResponse object using the Notification's data and the constructed StudentResponse.
            return NotificationResponse.newBuilder()
                    .setNotificationId(notification.getNotificationId())
                    .setNotificationType(notification.getNotificationType())
                    .setPayload(studentResponse)
                    .setCreatedOn(notification.getCreatedOn().toString())
                    .build();
        }).toList();

        // Add all constructed NotificationResponse objects to the NotificationList builder.
        NotificationList notificationList = notifictionListBuilder.addAllNotifications(notificationResponseList).build();

        // Send the constructed NotificationList to the client through the responseObserver.
        responseObserver.onNext(notificationList);

        // Notify the client that the response is complete.
        responseObserver.onCompleted();
    }

    @Override
    public Notification getNotificationById() {
        return null;
    }

//    @Override
//    public Notification getNotificationById() {
//        Optional<Notification> notificationByNotificationId = notificationRepository.findNotificationByNotificationId(notificationId);
//        if(notificationByNotificationId.isPresent()){
//            Notification notification = notificationByNotificationId.get();
//            return notification;
//        }
//        else{
//            throw new NotificationNotFoundException("Notification not found");
//        }
//    }

//    @Override
//    public void getNotificationById(com.inspiron.notification.NotificationRequest request, StreamObserver<com.inspiron.notification.NotificationResponse> responseObserver) {
//        String notificationId = request.getNotificationId();
//        Optional<Notification> notificationByNotificationId = notificationRepository.findNotificationByNotificationId(notificationId);
//        if(notificationByNotificationId.isPresent()){
//            Notification notification = notificationByNotificationId.get();
//            NotificationResponse notificationResponse = NotificationResponse.newBuilder().setNotificationId(notificationId).setStudentName(notification.getPayload().getStudentName()).build();
//            responseObserver.onNext(notificationResponse);
//        responseObserver.onCompleted();
//        }


//        Notification notification = notificationServiceImpl.getNotificationById(notificationId);
//        com.inspiron.notification.NotificationResponse notificationResponse = com.inspiron.notification.NotificationResponse.newBuilder().setNotificationId(notification.getNotificationId())
//                .setStudentName(notification.getPayload().getStudentName()).build();
//        responseObserver.onNext(notificationResponse);
//        responseObserver.onCompleted();
//    }

    @Override
    public void getAllNotificationsPagination(NotificationPagesRequest request, StreamObserver<NotificationPagesResponse> responseObserver) {
        int pageNo = request.getPageNo().getValue();
        int pageSize = request.getPageSize().getValue();
        String sortBy = request.getSortBy();
        String sortOrder = request.getSortOrder();
        Sort.Direction direction = sortOrder.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;

        Page<Notification> notificationPages = notificationRepository.findAll(PageRequest.of(pageNo, pageSize, Sort.by(direction, sortBy)));
        List<Notification> notifications = notificationPages.getContent();

        NotificationPagesResponse.Builder responseBuilder = NotificationPagesResponse.newBuilder();
        NotificationList.Builder notificationList = NotificationList.newBuilder();

        notifications.forEach(notification -> {
            Student student = notification.getPayload();

            List<AddressResponse> addressResponses = student.getAddresses().stream()
                    .map(address -> AddressResponse.newBuilder()
                            .setCity(address.getCity())
                            .setPincode(address.getPinCode())
                            .build())
                    .collect(Collectors.toList());

            StudentResponse studentResponse = StudentResponse.newBuilder()
                    .setStudentName(student.getStudentName())
                    .setStudentId(student.getStudentId())
                    .setStudentAge(student.getStudentAge())
                    .addAllAddressResponses(addressResponses)
                    .build();

            NotificationResponse notificationResponse = NotificationResponse.newBuilder()
                    .setNotificationId(notification.getNotificationId())
                    .setNotificationType(notification.getNotificationType())
                    .setPayload(studentResponse)
                    .setCreatedOn(notification.getCreatedOn().toString())
                    .build();

            notificationList.addNotifications(notificationResponse);
        });

        responseBuilder.setNotificationLists(notificationList).setTotalPages(Int32Value.of(4));
        responseObserver.onNext(responseBuilder.build());
        responseObserver.onCompleted();
    }
}

