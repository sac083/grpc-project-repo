package com.inspiron.labs.app.repository;

import java.util.List;
import java.util.Optional;

import com.inspiron.labs.app.entity.Student;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends MongoRepository<Student, String> {

    public Optional<Student> findByStudentNameAndStudentAge(String studentName, Integer studentAge);

    public Optional<Student> findByStudentId(String studentId);

    public Student deleteByStudentId(String studentId);

    @Query(value = "{'addresses.city' : {$exists : true}}")
    List<Student> findAllUniqueCities();
}
