package com.sms.controller;

import com.sms.dto.ApiResponse;
import com.sms.entity.Attendance;
import com.sms.entity.Course;
import com.sms.entity.Student;
import com.sms.repository.AttendanceRepository;
import com.sms.repository.CourseRepository;
import com.sms.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/attendance")
@CrossOrigin(origins = "http://localhost:3000")
public class AttendanceController {

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CourseRepository courseRepository;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Attendance>>> getAllAttendance() {
        return ResponseEntity.ok(ApiResponse.success("Attendance fetched", attendanceRepository.findAll()));
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<ApiResponse<List<Attendance>>> getByStudent(@PathVariable Long studentId) {
        return ResponseEntity.ok(ApiResponse.success("Attendance by student",
                attendanceRepository.findByStudentId(studentId)));
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<ApiResponse<List<Attendance>>> getByCourse(@PathVariable Long courseId) {
        return ResponseEntity.ok(ApiResponse.success("Attendance by course",
                attendanceRepository.findByCourseId(courseId)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Attendance>> markAttendance(
            @RequestParam Long studentId,
            @RequestParam Long courseId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam String status) {
        try {
            Student student = studentRepository.findById(studentId)
                    .orElseThrow(() -> new RuntimeException("Student not found"));
            Course course = courseRepository.findById(courseId)
                    .orElseThrow(() -> new RuntimeException("Course not found"));

            Attendance attendance = new Attendance();
            attendance.setStudent(student);
            attendance.setCourse(course);
            attendance.setAttendanceDate(date);
            attendance.setStatus(status.toUpperCase());

            Attendance saved = attendanceRepository.save(attendance);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("Attendance marked", saved));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/percentage/{studentId}/{courseId}")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getAttendancePercentage(
            @PathVariable Long studentId, @PathVariable Long courseId) {
        long total = attendanceRepository.countTotalByStudentAndCourse(studentId, courseId);
        long present = attendanceRepository.countPresentByStudentAndCourse(studentId, courseId);
        double percentage = total > 0 ? (present * 100.0 / total) : 0.0;

        Map<String, Object> result = new HashMap<>();
        result.put("total", total);
        result.put("present", present);
        result.put("absent", total - present);
        result.put("percentage", Math.round(percentage * 100.0) / 100.0);

        return ResponseEntity.ok(ApiResponse.success("Attendance percentage", result));
    }
}
