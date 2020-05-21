package com.attendance.data.repository;

import com.attendance.data.model.Person;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PersonRepository extends MongoRepository<Person, String> {
    Optional<Person> findByEmail(String email);

    List<Person> findByClassromsContaining(final String id);

    List<Person> findByIdIn(List<String> ids);
}
