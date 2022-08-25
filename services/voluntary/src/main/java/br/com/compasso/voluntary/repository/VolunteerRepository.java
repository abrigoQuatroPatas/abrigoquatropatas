package com.compass.volunteer.repository;

import com.compass.volunteer.entity.VolunteerEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VolunteerRepository extends MongoRepository<VolunteerEntity, String> {
}
