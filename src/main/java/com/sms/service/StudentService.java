package com.sms.service;

import com.sms.entity.Student;
import com.sms.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Optional<Student> getStudentById(Long id) {
        return studentRepository.findById(id);
    }

    public Student createStudent(Student student) {
        if (studentRepository.existsByEmail(student.getEmail())) {
            throw new RuntimeException("Student with email already exists: " + student.getEmail());
        }
        if (studentRepository.existsByEnrollmentNumber(student.getEnrollmentNumber())) {
            throw new RuntimeException("Enrollment number already exists: " + student.getEnrollmentNumber());
        }
        return studentRepository.save(student);
    }

    public Student updateStudent(Long id, Student studentDetails) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + id));

        student.setName(studentDetails.getName());
        student.setPhone(studentDetails.getPhone());
        student.setDateOfBirth(studentDetails.getDateOfBirth());
        student.setDepartment(studentDetails.getDepartment());
        student.setSemester(studentDetails.getSemester());

        if (!student.getEmail().equals(studentDetails.getEmail())) {
            if (studentRepository.existsByEmail(studentDetails.getEmail())) {
                throw new RuntimeException("Email already in use: " + studentDetails.getEmail());
            }
            student.setEmail(studentDetails.getEmail());
        }

        if (!student.getEnrollmentNumber().equals(studentDetails.getEnrollmentNumber())) {
            if (studentRepository.existsByEnrollmentNumber(studentDetails.getEnrollmentNumber())) {
                throw new RuntimeException("Enrollment number already in use: " + studentDetails.getEnrollmentNumber());
            }
            student.setEnrollmentNumber(studentDetails.getEnrollmentNumber());
        }

        return studentRepository.save(student);
    }

    public void deleteStudent(Long id) {
        if (!studentRepository.existsById(id)) {
            throw new RuntimeException("Student not found with id: " + id);
        }
        studentRepository.deleteById(id);
    }

    public List<Student> searchByName(String name) {
        return studentRepository.findByNameContainingIgnoreCase(name);
    }

    public List<Student> getStudentsByDepartment(String department) {
        return studentRepository.findByDepartment(department);
    }

    public long getTotalStudents() {
        return studentRepository.count();
    }
}
