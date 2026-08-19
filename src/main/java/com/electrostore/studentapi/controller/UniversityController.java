package com.electrostore.studentapi.controller;

import com.electrostore.studentapi.service.UniversityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/universities")
@CrossOrigin
public class UniversityController {

    private final UniversityService universityService;

    public UniversityController(
            UniversityService universityService
    ) {
        this.universityService = universityService;
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchUniversities(
            @RequestParam String query
    ) {

        try {

            List<UniversityService.UniversityResponse> result =
                    universityService.searchUniversities(query);

            return ResponseEntity.ok(result);

        } catch (RuntimeException e) {

            return ResponseEntity
                    .internalServerError()
                    .body(e.getMessage());
        }
    }
}