package com.electrostore.studentapi.repository;

import com.electrostore.studentapi.entity.University;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UniversityRepository extends JpaRepository<University, Long> {

    List<University> findTop10ByNameContainingIgnoreCaseOrCityContainingIgnoreCase(
            String name,
            String city
    );
}