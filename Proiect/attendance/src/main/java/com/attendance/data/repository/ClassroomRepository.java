package com.attendance.data.repository;

import com.attendance.data.model.Classroom;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClassroomRepository extends MongoRepository<Classroom, String> {
    Optional<Classroom> findByCode(String code);
    List<Classroom> getClassroomByAuthor(String author);
}
