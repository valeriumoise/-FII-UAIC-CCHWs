package com.attendance.data.repository;

import com.attendance.data.model.Presences;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IPresenceRepository extends MongoRepository<Presences, String> {
    Presences findByCode(String code);
}
