package com.sms.controller;

import com.sms.dto.ApiResponse;
import com.sms.entity.Faculty;
import com.sms.service.FacultyService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/faculty")
@CrossOrigin(origins = "http://localhost:3000")
public class FacultyController {

    @Autowired
    private FacultyService facultyService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Faculty>>> getAllFaculty() {
        return ResponseEntity.ok(ApiResponse.success("Faculty fetched", facultyService.getAllFaculty()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Faculty>> getFacultyById(@PathVariable Long id) {
        return facultyService.getFacultyById(id)
                .map(f -> ResponseEntity.ok(ApiResponse.success("Faculty found", f)))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("Faculty not found with id: " + id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Faculty>> createFaculty(@Valid @RequestBody Faculty faculty) {
        try {
            Faculty created = facultyService.createFaculty(faculty);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("Faculty created successfully", created));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Faculty>> updateFaculty(@PathVariable Long id,
                                                               @Valid @RequestBody Faculty faculty) {
        try {
            Faculty updated = facultyService.updateFaculty(id, faculty);
            return ResponseEntity.ok(ApiResponse.success("Faculty updated successfully", updated));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteFaculty(@PathVariable Long id) {
        try {
            facultyService.deleteFaculty(id);
            return ResponseEntity.ok(ApiResponse.success("Faculty deleted successfully", null));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/count")
    public ResponseEntity<ApiResponse<Long>> getTotalFaculty() {
        return ResponseEntity.ok(ApiResponse.success("Total faculty", facultyService.getTotalFaculty()));
    }
}
