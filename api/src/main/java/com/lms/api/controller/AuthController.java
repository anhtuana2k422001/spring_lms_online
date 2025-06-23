package com.lms.api.controller;

import com.lms.api.dto.LoginDto;
import com.lms.api.dto.UserRegistrationDto;
import com.lms.common.response.ApiResponse;
import com.lms.core.domain.User;
import com.lms.core.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<String>> register(@Valid @RequestBody UserRegistrationDto registrationDto) {
        try {
            // Kiểm tra email đã tồn tại
            if (userService.existsByEmail(registrationDto.getEmail())) {
                return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Email already exists"));
            }
            
            // Tạo user mới
            User user = new User();
            user.setEmail(registrationDto.getEmail());
            user.setPassword(registrationDto.getPassword()); // Sẽ được encode trong service
            user.setFirstName(registrationDto.getFirstName());
            user.setLastName(registrationDto.getLastName());
            user.setPhoneNumber(registrationDto.getPhoneNumber());
            
            User savedUser = userService.createUser(user);
            
            return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("User registered successfully with ID: " + savedUser.getId()));
                
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Registration failed: " + e.getMessage()));
        }
    }
    
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<String>> login(@Valid @RequestBody LoginDto loginDto) {
        try {
            Optional<User> userOpt = userService.findByEmail(loginDto.getEmail());
            
            if (userOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("Invalid email or password"));
            }
            
            User user = userOpt.get();
            
            // Kiểm tra password
            if (!passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("Invalid email or password"));
            }
            
            return ResponseEntity.ok(
                ApiResponse.success("Login successful for user: " + user.getFirstName() + " " + user.getLastName())
            );
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Login failed: " + e.getMessage()));
        }
    }
    
    @GetMapping("/profile/{email}")
    public ResponseEntity<ApiResponse<User>> getProfile(@PathVariable String email) {
        try {
            Optional<User> userOpt = userService.findByEmail(email);
            
            if (userOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("User not found"));
            }
            
            User user = userOpt.get();
            // Xóa password khỏi response
            user.setPassword(null);
            
            return ResponseEntity.ok(ApiResponse.success(user));
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Failed to get profile: " + e.getMessage()));
        }
    }
}