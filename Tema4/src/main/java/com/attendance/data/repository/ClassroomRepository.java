package com.attendance.data.repository;

import com.attendance.data.model.Classroom;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClassroomRepository extends MongoRepository<Classroom, String> {
    Classroom findByCode(String code);
    List<Classroom> getClassroomByAuthor(String author);
}
