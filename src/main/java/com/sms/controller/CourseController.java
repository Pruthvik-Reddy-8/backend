package com.sms.controller;

import com.sms.dto.ApiResponse;
import com.sms.entity.Course;
import com.sms.service.CourseService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/courses")
@CrossOrigin(origins = "http://localhost:3000")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Course>>> getAllCourses() {
        return ResponseEntity.ok(ApiResponse.success("Courses fetched", courseService.getAllCourses()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Course>> getCourseById(@PathVariable Long id) {
        return courseService.getCourseById(id)
                .map(c -> ResponseEntity.ok(ApiResponse.success("Course found", c)))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("Course not found with id: " + id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Course>> createCourse(@Valid @RequestBody Course course,
                                                             @RequestParam(required = false) Long facultyId) {
        try {
            Course created = courseService.createCourse(course, facultyId);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("Course created successfully", created));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Course>> updateCourse(@PathVariable Long id,
                                                             @Valid @RequestBody Course course,
                                                             @RequestParam(required = false) Long facultyId) {
        try {
            Course updated = courseService.updateCourse(id, course, facultyId);
            return ResponseEntity.ok(ApiResponse.success("Course updated successfully", updated));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCourse(@PathVariable Long id) {
        try {
            courseService.deleteCourse(id);
            return ResponseEntity.ok(ApiResponse.success("Course deleted successfully", null));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/count")
    public ResponseEntity<ApiResponse<Long>> getTotalCourses() {
        return ResponseEntity.ok(ApiResponse.success("Total courses", courseService.getTotalCourses()));
    }
}
