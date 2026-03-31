package com.sms.service;

import com.sms.entity.Course;
import com.sms.entity.Faculty;
import com.sms.repository.CourseRepository;
import com.sms.repository.FacultyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private FacultyRepository facultyRepository;

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public Optional<Course> getCourseById(Long id) {
        return courseRepository.findById(id);
    }

    public Course createCourse(Course course, Long facultyId) {
        if (courseRepository.existsByCourseCode(course.getCourseCode())) {
            throw new RuntimeException("Course code already exists: " + course.getCourseCode());
        }
        if (facultyId != null) {
            Faculty faculty = facultyRepository.findById(facultyId)
                    .orElseThrow(() -> new RuntimeException("Faculty not found with id: " + facultyId));
            course.setFaculty(faculty);
        }
        return courseRepository.save(course);
    }

    public Course updateCourse(Long id, Course courseDetails, Long facultyId) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + id));

        course.setCourseName(courseDetails.getCourseName());
        course.setDescription(courseDetails.getDescription());
        course.setCredits(courseDetails.getCredits());

        if (facultyId != null) {
            Faculty faculty = facultyRepository.findById(facultyId)
                    .orElseThrow(() -> new RuntimeException("Faculty not found with id: " + facultyId));
            course.setFaculty(faculty);
        } else {
            course.setFaculty(null);
        }

        return courseRepository.save(course);
    }

    public void deleteCourse(Long id) {
        if (!courseRepository.existsById(id)) {
            throw new RuntimeException("Course not found with id: " + id);
        }
        courseRepository.deleteById(id);
    }

    public long getTotalCourses() {
        return courseRepository.count();
    }
}
