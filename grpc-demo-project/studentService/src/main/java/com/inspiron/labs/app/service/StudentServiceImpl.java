package com.inspiron.labs.app.service;

import com.inspiron.labs.app.constants.AppConstants;
import com.inspiron.labs.app.dto.StudentDto;
import com.inspiron.labs.app.dto.StudentUpdateDto;
import com.inspiron.labs.app.entity.Address;
import com.inspiron.labs.app.entity.Notification;
import com.inspiron.labs.app.entity.Student;
import com.inspiron.labs.app.exception.StudentAlreadyExistsException;
import com.inspiron.labs.app.exception.StudentNotFoundException;
import com.inspiron.labs.app.repository.StudentRepository;
import com.inspiron.notification.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Slf4j
@RequiredArgsConstructor
@Service
public class StudentServiceImpl implements StudentService{

    private final StudentRepository studentRepository;
    @Autowired
    private final RestTemplate restTemplate;
    private final KafkaTemplate<String, Student> kafkaTemplate;
//    private final NotificationClient feignConfig;
    private final MongoTemplate mongoTemplate;
    private final NotificationServiceGrpc.NotificationServiceBlockingStub blockingStub;

    @Override
    public Student saveStudent(StudentDto studentDto) {
        Optional<Student> studentOptional = studentRepository.findByStudentNameAndStudentAge(studentDto.getStudentName(), studentDto.getStudentAge());
//		notifyViaController(studentDto);
//		List<Notification> notifications1 = fetchNotifications(0, 3, "notificationId", "desc");
//		log.info("Notifications Pages {}", notifications1);
        if(studentOptional.isPresent()) {
            throw new StudentAlreadyExistsException("Student already exists");
        }
        else {
            Student student = new Student();
            BeanUtils.copyProperties(studentDto, student);
//			List<Notification> notifications = Objects.requireNonNull(this.restTemplate.exchange(
//                    "http://localhost:9090/api/notification/get",
//                    HttpMethod.GET,
//                    null,
//                    new ParameterizedTypeReference<SuccessResponse<List<Notification>>>() {
//                    }).getBody()).getData();
//			log.info("Logging all notifications {}",notifications);
            List<Address> addresses = studentDto.getAddresses().stream()
                    .map(addressDto -> {
                        Address address = new Address();
                        BeanUtils.copyProperties(addressDto, address);
                        return address;
                    })
                    .toList();
            student.setAddresses(addresses);
            studentRepository.save(student);
            char c = student.getStudentName().toLowerCase().charAt(0);
            if(c>='a'&& c<='p'){
                kafkaTemplate.send(AppConstants.STUDENT_OBJECT_NEW,0,"atop",student);
            }
            if(c>='q'&& c<='z'){
                kafkaTemplate.send(AppConstants.STUDENT_OBJECT_NEW,1,"atop",student);
            }
//            List<Notification> data = feignConfig.getAllNotification().getBody().getData();
//            log.info("All notifications here {}", data);
//            getNotificationByIdFromAnotherService();
//			getAllNotificationsFromAnotherService();
//			Page<Notification> notificationPages = Objects.requireNonNull(feignConfig.getAllNotificationPaginaton(0, 3, "notificationId", "desc").getBody()).getData();
//			log.info("Printing notifications here {},{},{},{}", notificationPages.getContent(),notificationPages.getTotalPages(),notificationPages.getSize(),notificationPages.getSort());
            return student;
        }
    }
//	public List<Notification> fetchNotifications(int pageNo, int pageSize, String sortBy, String sortOrder) {
//		ResponseEntity<SuccessResponse<List<Notification>>> responseEntity = restTemplate.exchange(
//				"http://localhost:9090/api/notification/getPagination?page=" + pageNo + "&size=" + pageSize+"&sortBy"+sortBy+"&sortOrder"+sortOrder,
//				HttpMethod.GET,
//				null,
//				new ParameterizedTypeReference<SuccessResponse<List<Notification>>>() {
//				});
//
//		return responseEntity.getBody().getData();
//	}

//     Example method to fetch notifications with default page and size
//	public Page<Notification> fetchNotifications() {
//		return fetchNotifications(0, 10); // Default page = 0, size = 10
//	}
//	public void notifyViaController(StudentDto studentDto)
//	{
//		String apiUrl = "http://localhost:9090/api/notification/sendNotification";
//		HttpHeaders headers = new HttpHeaders();
//		headers.setContentType(MediaType.APPLICATION_JSON);
//		HttpEntity<StudentDto> requestEntity = new HttpEntity<>(studentDto,headers);
//		restTemplate.postForObject(apiUrl,requestEntity,StudentDto.class);
//	}

//    	public void getAllNotificationsFromAnotherService(){
//		Empty request = Empty.newBuilder().build();
//		NotificationList notificationList = blockingStub.getAllNotification(request);
//		// Process the received notifications
//		log.info("grpc getAllNotifications {}",notificationList);
//	}

//    @Override
//    public Student saveStudent(StudentDto studentDto) {
//        return null;
//    }

    @Override
    public Student updateStudent(StudentUpdateDto studentUpdateDto, String studentId) {
        Optional<Student> studentOptional = studentRepository.findByStudentId(studentId);
        if(studentOptional.isPresent()) {
            Student student = studentOptional.get();
            BeanUtils.copyProperties(studentUpdateDto, student);
            student.setStudentName(studentUpdateDto.getStudentName());
            student.setStudentAge(studentUpdateDto.getStudentAge());
            Student studentFromDb = studentRepository.save(student);
            kafkaTemplate.send(AppConstants.UPDATE_STUDENT_OBJECT,studentFromDb);
            return studentFromDb;
        }
        else {
            throw new StudentNotFoundException("Student not found");
        }
    }

    @Override
    public Student fetchStudent(String studentId) {
        Optional<Student> studentOptional = studentRepository.findByStudentId(studentId);
        if(studentOptional.isPresent()) {
            return studentOptional.get();
        }
        else {
            throw new StudentNotFoundException("Student not found");
        }
    }

    @Override
    @Transactional
    public Boolean deleteStudent(String studentId) {
        Optional<Student> studentOptional = studentRepository.findByStudentId(studentId);
        if(studentOptional.isPresent()) {
            Student student = studentOptional.get();
            log.info("Deleting student with id {}", student.getStudentId());
            studentRepository.delete(student);
            kafkaTemplate.send(AppConstants.DELETE_STUDENT_OBJECT,student);
            return true;
        }
        else {
            throw new StudentNotFoundException("Student not found");
        }
    }

    @Override
    public List<String> getUniqueCities() {
        List<Student> students = studentRepository.findAllUniqueCities();
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.unwind("addresses"),
                Aggregation.group("addresses.city").addToSet("addresses.city").as("uniqueCities"),
                Aggregation.project().andExclude("_id")
        );

        AggregationResults<String> student = mongoTemplate.aggregate(aggregation, "students", String.class);
        List<String> mappedResults = student.getMappedResults();
        return mappedResults;
    }

//    @Override
//    public Map<Descriptors.FieldDescriptor, Object> getNotificationById(NotificationRequest notificationRequest) {
//        NotificationResponse response = blockingStub.getNotificationById(notificationRequest);
//        return response.getAllFields();
//    }

    @Override
    public List<Notification> getNotificationPages(NotificationPagesRequest notificationPagesRequest) {
        NotificationPagesResponse allNotificationsPagination = blockingStub.getAllNotificationsPagination(notificationPagesRequest);

        return allNotificationsPagination.getNotificationLists().getNotificationsList().stream()
                .map(response -> {
                    Notification notification = new Notification();
                    notification.setNotificationId(response.getNotificationId());
                    notification.setNotificationType(response.getNotificationType());
                    notification.setCreatedOn(response.getCreatedOn().toString());

                    StudentResponse studentResponse = response.getPayload();
                    Student student = new Student();
                    student.setStudentId(studentResponse.getStudentId());
                    student.setStudentAge(studentResponse.getStudentAge());
                    student.setStudentName(studentResponse.getStudentName());

                    List<Address> addresses = studentResponse.getAddressResponsesList().stream()
                            .map(addressResponse -> {
                                Address address = new Address();
                                address.setCity(addressResponse.getCity());
                                address.setPinCode(addressResponse.getPincode());
                                return address;
                            })
                            .collect(Collectors.toList());

                    student.setAddresses(addresses);

                    notification.setPayload(student);
                    return notification;
                })
                .collect(Collectors.toList());
    }

    /*The getAllNotifications() method acts as the client-side interface for fetching all
    notifications from a gRPC server. It sends a request to the server using a blocking stub,
    receives a response containing a list of notifications, and maps these protobuf messages to Java objects.
    The method constructs Notification objects, including student information and addresses,
    and returns them as a list. Overall, it facilitates seamless communication with the server,
    processing of responses, and presentation of notifications in the client application.*/
    @Override
    public List<Notification> getAllNotifications() {

        // Create an Empty message using the generated Empty.Builder class.
        Empty empty = Empty.newBuilder().build();

        // Call the gRPC blocking stub's getAllNotification() method to retrieve all notifications.
        // This method sends an RPC request to the server and waits for the response.
        NotificationList allNotification = blockingStub.getAllNotification(empty);

        // Map the received NotificationList to a list of Notification objects.
        return allNotification.getNotificationsList().stream().map(notificationResponse -> {

            // For each NotificationResponse in the list, map it to a Notification object.
            Notification notification = new Notification();

            // Set notification ID and type from the NotificationResponse.
            notification.setNotificationId(notificationResponse.getNotificationId());
            notification.setNotificationType(notificationResponse.getNotificationType());

            // Create a Student object and set its properties from the payload in NotificationResponse.
            Student student = new Student();
            student.setStudentId(notificationResponse.getPayload().getStudentId());
            student.setStudentAge(notificationResponse.getPayload().getStudentAge());
            student.setStudentName(notificationResponse.getPayload().getStudentName());

            // Map AddressResponse objects in the payload to Address objects in the Student's addresses list.
            List<Address> addresses = notificationResponse.getPayload().getAddressResponsesList().stream()
                    .map(addressResponse -> {
                        Address address = new Address();
                        address.setCity(addressResponse.getCity());
                        address.setPinCode(addressResponse.getPincode());
                        return address;
                    })
                    .toList();

            // Set the addresses list in the Student object.
            student.setAddresses(addresses);

            // Set the Student object as the payload of the Notification.
            notification.setPayload(student);

            // Set the creation timestamp of the notification.
            notification.setCreatedOn(notificationResponse.getCreatedOn());

            // Return the constructed Notification object.
            return notification;
        }).toList(); // Collect the mapped Notification objects into a list and return.
    }

}
