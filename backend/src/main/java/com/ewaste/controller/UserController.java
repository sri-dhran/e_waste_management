package com.ewaste.controller;

import com.ewaste.model.Admin;
import com.ewaste.model.User;
import com.ewaste.repository.AdminRepository;
import com.ewaste.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AdminRepository adminRepository;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Email is already registered"));
        }
        user.setRewardPoints(0);
        user.setCenter(null); // Normal user, not staff
        User savedUser = userRepository.save(user);
        return ResponseEntity.ok(savedUser);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        String emailOrUsername = credentials.get("email");
        if (emailOrUsername == null) {
            emailOrUsername = credentials.get("username");
        }
        String password = credentials.get("password");

        if (emailOrUsername == null || password == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "Username/Email and Password are required"));
        }

        // 1. Check if Admin
        Optional<Admin> adminOpt = adminRepository.findByUsername(emailOrUsername);
        if (adminOpt.isPresent()) {
            Admin admin = adminOpt.get();
            if (admin.getPassword().equals(password)) {
                Map<String, Object> response = new HashMap<>();
                response.put("role", "ADMIN");
                response.put("username", admin.getUsername());
                response.put("adminId", admin.getAdminId());
                return ResponseEntity.ok(response);
            }
        }

        // 2. Check if User or Staff
        Optional<User> userOpt = userRepository.findByEmail(emailOrUsername);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (user.getPassword().equals(password)) {
                Map<String, Object> response = new HashMap<>();
                response.put("userId", user.getUserId());
                response.put("name", user.getName());
                response.put("email", user.getEmail());
                response.put("phone", user.getPhone());
                response.put("rewardPoints", user.getRewardPoints());

                if (user.getCenter() != null) {
                    response.put("role", "STAFF");
                    response.put("centerId", user.getCenter().getCenterId());
                    response.put("centerName", user.getCenter().getCenterName());
                } else {
                    response.put("role", "USER");
                }
                return ResponseEntity.ok(response);
            }
        }

        return ResponseEntity.status(401).body(Map.of("message", "Invalid credentials"));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserDetails(@PathVariable Integer userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
            return ResponseEntity.ok(userOpt.get());
        }
        return ResponseEntity.notFound().build();
    }
}
