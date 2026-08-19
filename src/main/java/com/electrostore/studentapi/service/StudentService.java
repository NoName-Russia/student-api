package com.electrostore.studentapi.service;

import com.electrostore.studentapi.entity.Student;
import com.electrostore.studentapi.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student register(Student student) {

        if (studentRepository.existsByEmail(student.getEmail())) {
            throw new RuntimeException(
                    "Пользователь с таким email уже зарегистрирован"
            );
        }

        return studentRepository.save(student);
    }

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Student getStudent(Long id) {

        return studentRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Студент не найден")
                );
    }
}