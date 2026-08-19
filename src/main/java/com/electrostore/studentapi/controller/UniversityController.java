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

    public UniversityController(UniversityService universityService) {
        this.universityService = universityService;
    }

    /**
     * Универсальный endpoint поиска.
     *
     * Примеры:
     * GET /api/universities
     * GET /api/universities?query=московский
     * GET /api/universities/search?query=московский
     */
    @GetMapping
    public ResponseEntity<?> searchUniversitiesWithoutSearchPath(
            @RequestParam(required = false, defaultValue = "") String query
    ) {
        return search(query);
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchUniversities(
            @RequestParam String query
    ) {
        return search(query);
    }

    private ResponseEntity<?> search(String query) {
        try {
            if (query == null || query.trim().length() < 2) {
                return ResponseEntity.ok(List.of());
            }

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
