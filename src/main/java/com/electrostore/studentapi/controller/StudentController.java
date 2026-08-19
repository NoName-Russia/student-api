package com.electrostore.studentapi.controller;

import com.electrostore.studentapi.entity.Student;
import com.electrostore.studentapi.service.StudentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
@CrossOrigin
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping
    public ResponseEntity<?> registerStudent(
            @RequestBody Student student
    ) {

        try {

            Student savedStudent =
                    studentService.register(student);

            return ResponseEntity.ok(savedStudent);

        } catch (RuntimeException e) {

            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<Student>> getAllStudents() {

        return ResponseEntity.ok(
                studentService.getAllStudents()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getStudent(
            @PathVariable Long id
    ) {

        try {

            return ResponseEntity.ok(
                    studentService.getStudent(id)
            );

        } catch (RuntimeException e) {

            return ResponseEntity
                    .notFound()
                    .build();
        }
    }
}