package com.sms.service;

import com.sms.entity.Faculty;
import com.sms.repository.FacultyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class FacultyService {

    @Autowired
    private FacultyRepository facultyRepository;

    public List<Faculty> getAllFaculty() {
        return facultyRepository.findAll();
    }

    public Optional<Faculty> getFacultyById(Long id) {
        return facultyRepository.findById(id);
    }

    public Faculty createFaculty(Faculty faculty) {
        if (facultyRepository.existsByEmail(faculty.getEmail())) {
            throw new RuntimeException("Faculty with email already exists: " + faculty.getEmail());
        }
        return facultyRepository.save(faculty);
    }

    public Faculty updateFaculty(Long id, Faculty facultyDetails) {
        Faculty faculty = facultyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Faculty not found with id: " + id));

        faculty.setName(facultyDetails.getName());
        faculty.setPhone(facultyDetails.getPhone());
        faculty.setDepartment(facultyDetails.getDepartment());
        faculty.setDesignation(facultyDetails.getDesignation());

        if (!faculty.getEmail().equals(facultyDetails.getEmail())) {
            if (facultyRepository.existsByEmail(facultyDetails.getEmail())) {
                throw new RuntimeException("Email already in use: " + facultyDetails.getEmail());
            }
            faculty.setEmail(facultyDetails.getEmail());
        }

        return facultyRepository.save(faculty);
    }

    public void deleteFaculty(Long id) {
        if (!facultyRepository.existsById(id)) {
            throw new RuntimeException("Faculty not found with id: " + id);
        }
        facultyRepository.deleteById(id);
    }

    public long getTotalFaculty() {
        return facultyRepository.count();
    }
}
