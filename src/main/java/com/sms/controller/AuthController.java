package com.sms.controller;

import com.sms.dto.ApiResponse;
import com.sms.entity.User;
import com.sms.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<Map<String, String>>> login(
            @RequestBody Map<String, String> credentials) {

        String username = credentials.get("username");
        String password = credentials.get("password");

        if (username == null || password == null) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Username and password are required"));
        }

        Optional<User> userOpt = userRepository.findByUsername(username);

        if (userOpt.isEmpty()) {
            return ResponseEntity.status(401)
                    .body(ApiResponse.error("Invalid username or password"));
        }

        User user = userOpt.get();

        if (!user.getPassword().equals(password)) {
            return ResponseEntity.status(401)
                    .body(ApiResponse.error("Invalid username or password"));
        }

        Map<String, String> response = Map.of(
                "username", user.getUsername(),
                "role", user.getRole(),
                "message", "Login successful"
        );

        return ResponseEntity.ok(ApiResponse.success("Login successful", response));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<String>> logout() {
        return ResponseEntity.ok(ApiResponse.success("Logout successful", "Logged out"));
    }

    @GetMapping("/check")
    public ResponseEntity<ApiResponse<String>> check() {
        return ResponseEntity.ok(ApiResponse.success("Auth check", "OK"));
    }
}
