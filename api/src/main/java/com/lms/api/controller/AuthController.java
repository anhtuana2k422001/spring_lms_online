package com.lms.api.controller;

import com.lms.api.dto.LoginDto;
import com.lms.api.dto.UserRegistrationDto;
import com.lms.api.util.JwtUtil;
import com.lms.common.response.ApiResponse;
import com.lms.core.domain.User;
import com.lms.core.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    
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
                .body(ApiResponse.success("User registered successfully"));
                
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Registration failed: " + e.getMessage()));
        }
    }
    
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<Map<String, Object>>> login(@Valid @RequestBody LoginDto loginDto) {
        try {
            Optional<User> userOpt = userService.findByEmail(loginDto.getEmail());
            
            if (userOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("Invalid email or password"));
            }
            
            User user = userOpt.get();
            
            // Check  password
            if (!passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("Invalid email or password"));
            }
            
            // get role default
            String roleName = "USER";
            if (user.getRoles() != null && !user.getRoles().isEmpty()) {
                roleName = user.getRoles().iterator().next().getName();
            }
            
            // Create JWT token
            String token = jwtUtil.generateToken(
                user.getEmail(), 
                user.getId().toString(), 
                roleName
            );
            
            // Tạo response data
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("token", token);
            responseData.put("type", "Bearer");
            responseData.put("user", Map.of(
                "id", user.getId(),
                "email", user.getEmail(),
                "firstName", user.getFirstName(),
                "lastName", user.getLastName(),
                "role", roleName
            ));
            
            return ResponseEntity.ok(ApiResponse.success(responseData));
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Login failed: " + e.getMessage()));
        }
    }
}