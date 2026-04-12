package com.firs.controller;

import com.firs.model.User;
import com.firs.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:8080", allowCredentials = "true")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(HttpSession session) {
        User sessionUser = (User) session.getAttribute("user");

        if (sessionUser == null) {
            return ResponseEntity.status(401).body(Map.of(
                    "status", "error",
                    "message", "Not logged in"
            ));
        }

        Optional<User> userOpt = userRepository.findById(sessionUser.getId());

        if (userOpt.isEmpty()) {
            return ResponseEntity.status(404).body(Map.of(
                    "status", "error",
                    "message", "User not found"
            ));
        }

        return ResponseEntity.ok(Map.of(
                "status", "success",
                "user", userOpt.get()
        ));
    }

    @PostMapping("/profile-image")
    public ResponseEntity<?> uploadProfileImage(
            @RequestParam("file") MultipartFile file,
            HttpSession session
    ) {
        try {
            User sessionUser = (User) session.getAttribute("user");

            if (sessionUser == null) {
                return ResponseEntity.status(401).body(Map.of(
                        "status", "error",
                        "message", "Not logged in"
                ));
            }

            if (file == null || file.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of(
                        "status", "error",
                        "message", "No file selected"
                ));
            }

            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                return ResponseEntity.badRequest().body(Map.of(
                        "status", "error",
                        "message", "Only image files are allowed"
                ));
            }

            Optional<User> userOpt = userRepository.findById(sessionUser.getId());
            if (userOpt.isEmpty()) {
                return ResponseEntity.status(404).body(Map.of(
                        "status", "error",
                        "message", "User not found"
                ));
            }

            User user = userOpt.get();

            String uploadDir = System.getProperty("user.dir") + File.separator + "uploads" + File.separator + "profiles";
            File dir = new File(uploadDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            String originalName = StringUtils.cleanPath(file.getOriginalFilename());
            String extension = "";

            int dotIndex = originalName.lastIndexOf('.');
            if (dotIndex >= 0) {
                extension = originalName.substring(dotIndex);
            }

            String fileName = "profile_" + user.getId() + "_" + UUID.randomUUID() + extension;
            Path targetPath = Paths.get(uploadDir).resolve(fileName);

            Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

            user.setProfileImage("/uploads/profiles/" + fileName);
            User updatedUser = userRepository.save(user);

            session.setAttribute("user", updatedUser);

            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Profile image uploaded successfully",
                    "user", updatedUser
            ));

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                    "status", "error",
                    "message", "Upload failed: " + e.getMessage()
            ));
        }
    }

    @DeleteMapping("/profile-image")
    public ResponseEntity<?> removeProfileImage(HttpSession session) {
        try {
            User sessionUser = (User) session.getAttribute("user");

            if (sessionUser == null) {
                return ResponseEntity.status(401).body(Map.of(
                        "status", "error",
                        "message", "Not logged in"
                ));
            }

            Optional<User> userOpt = userRepository.findById(sessionUser.getId());
            if (userOpt.isEmpty()) {
                return ResponseEntity.status(404).body(Map.of(
                        "status", "error",
                        "message", "User not found"
                ));
            }

            User user = userOpt.get();
            user.setProfileImage(null);

            User updatedUser = userRepository.save(user);
            session.setAttribute("user", updatedUser);

            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Profile image removed successfully",
                    "user", updatedUser
            ));

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                    "status", "error",
                    "message", "Failed to remove image: " + e.getMessage()
            ));
        }
    }
}