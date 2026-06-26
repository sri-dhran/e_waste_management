package com.ewaste.controller;

import com.ewaste.model.*;
import com.ewaste.repository.*;
import com.ewaste.util.QRCodeGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
@RequestMapping("/api/waste")
@CrossOrigin(origins = "*")
public class WasteController {

    @Autowired
    private WasteSubmissionRepository wasteRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CollectionCenterRepository centerRepository;

    @Autowired
    private TransportationLogRepository logRepository;

    @Autowired
    private RewardRepository rewardRepository;

    @PostMapping(value = "/submit", consumes = {"multipart/form-data"})
    public ResponseEntity<?> submitWaste(
            @RequestParam("userId") Integer userId,
            @RequestParam("deviceName") String deviceName,
            @RequestParam("quantity") Integer quantity,
            @RequestParam("condition") String condition,
            @RequestParam("centerId") Integer centerId,
            @RequestParam(value = "photo", required = false) MultipartFile photoFile) {

        try {
            // Validate User & Center
            Optional<User> userOpt = userRepository.findById(userId);
            if (userOpt.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("message", "Invalid User ID"));
            }
            User user = userOpt.get();

            Optional<CollectionCenter> centerOpt = centerRepository.findById(centerId);
            if (centerOpt.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("message", "Invalid Collection Center ID"));
            }
            CollectionCenter center = centerOpt.get();

            // Generate Unique Waste ID (e.g. EW123456)
            String wasteId = "EW" + (100000 + new Random().nextInt(900000));
            while (wasteRepository.existsById(wasteId)) {
                wasteId = "EW" + (100000 + new Random().nextInt(900000));
            }

            // Save Photo if present
            String photoPath = null;
            if (photoFile != null && !photoFile.isEmpty()) {
                String originalFilename = photoFile.getOriginalFilename();
                String ext = originalFilename != null && originalFilename.contains(".")
                        ? originalFilename.substring(originalFilename.lastIndexOf("."))
                        : ".jpg";
                String fileName = wasteId + "_device" + ext;
                
                String[] uploadPaths = {"../uploads/", "./uploads/"};
                for (String dirPath : uploadPaths) {
                    try {
                        File dir = new File(dirPath);
                        if (!dir.exists()) {
                            dir.mkdirs();
                        }
                        File file = new File(dir, fileName);
                        try (FileOutputStream fos = new FileOutputStream(file)) {
                            fos.write(photoFile.getBytes());
                        }
                        System.out.println("Uploaded photo saved at: " + file.getAbsolutePath());
                    } catch (Exception e) {
                        System.err.println("Error saving photo to " + dirPath + ": " + e.getMessage());
                    }
                }
                photoPath = "/uploads/" + fileName;
            }

            // Generate QR Code data
            String qrText = String.format(
                    "Waste ID : %s\nUser : %s\nDevice : %s\nCenter : %s\nStatus : Pending",
                    wasteId, user.getName(), deviceName, center.getCenterName()
            );
            String qrPath = QRCodeGenerator.generateQRCode(wasteId, qrText);

            // Create Submission
            WasteSubmission submission = new WasteSubmission();
            submission.setWasteId(wasteId);
            submission.setUser(user);
            submission.setDeviceName(deviceName);
            submission.setQuantity(quantity);
            submission.setCondition(condition);
            submission.setPhoto(photoPath);
            submission.setCenter(center);
            submission.setStatus("Pending");
            submission.setDate(LocalDateTime.now());
            submission.setQrPath(qrPath);

            WasteSubmission savedSubmission = wasteRepository.save(submission);
            return ResponseEntity.ok(savedSubmission);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(Map.of("message", "Error submitting e-waste: " + e.getMessage()));
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<WasteSubmission>> getUserSubmissions(@PathVariable Integer userId) {
        return ResponseEntity.ok(wasteRepository.findByUserUserIdOrderByDateDesc(userId));
    }

    @GetMapping("/all")
    public ResponseEntity<List<WasteSubmission>> getAllSubmissions() {
        return ResponseEntity.ok(wasteRepository.findAllByOrderByDateDesc());
    }

    @GetMapping("/{wasteId}")
    public ResponseEntity<?> getSubmissionDetails(@PathVariable String wasteId) {
        Optional<WasteSubmission> opt = wasteRepository.findById(wasteId);
        if (opt.isPresent()) {
            return ResponseEntity.ok(opt.get());
        }
        return ResponseEntity.notFound().build();
    }

    // Staff endpoint: Verify & Accept Waste
    @PostMapping("/{wasteId}/verify-accept")
    public ResponseEntity<?> verifyAndAcceptWaste(@PathVariable String wasteId, @RequestParam("staffUserId") Integer staffUserId) {
        Optional<WasteSubmission> opt = wasteRepository.findById(wasteId);
        if (opt.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Invalid Waste ID"));
        }
        
        WasteSubmission submission = opt.get();
        if (!"Pending".equalsIgnoreCase(submission.getStatus())) {
            return ResponseEntity.badRequest().body(Map.of("message", "Waste has already been accepted/processed. Current status: " + submission.getStatus()));
        }

        Optional<User> staffOpt = userRepository.findById(staffUserId);
        if (staffOpt.isEmpty() || staffOpt.get().getCenter() == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "Invalid Staff Credentials"));
        }
        User staff = staffOpt.get();

        // Update status to Received
        submission.setStatus("Received");
        wasteRepository.save(submission);

        // Add transit log
        TransportationLog log = new TransportationLog();
        log.setWasteSubmission(submission);
        log.setStatus("Received");
        log.setDate(LocalDate.now());
        log.setTime(LocalTime.now());
        log.setLocation(staff.getCenter().getCenterName() + " (" + staff.getCenter().getLocation() + ")");
        logRepository.save(log);

        return ResponseEntity.ok(submission);
    }

    // Admin endpoint: Update transit status
    @PostMapping("/{wasteId}/update-transit")
    public ResponseEntity<?> updateTransitStatus(
            @PathVariable String wasteId,
            @RequestParam("status") String status,
            @RequestParam("location") String location) {

        Optional<WasteSubmission> opt = wasteRepository.findById(wasteId);
        if (opt.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Invalid Waste ID"));
        }

        WasteSubmission submission = opt.get();
        String currentStatus = submission.getStatus();
        
        // Allowed transitions
        List<String> validStatuses = Arrays.asList("Received", "In Transit", "Recycling Center", "Recycled");
        if (!validStatuses.contains(status)) {
            return ResponseEntity.badRequest().body(Map.of("message", "Invalid status code"));
        }

        submission.setStatus(status);
        wasteRepository.save(submission);

        // Create log entry
        TransportationLog log = new TransportationLog();
        log.setWasteSubmission(submission);
        log.setStatus(status);
        log.setDate(LocalDate.now());
        log.setTime(LocalTime.now());
        log.setLocation(location);
        logRepository.save(log);

        // Earn rewards if Recycled
        if ("Recycled".equalsIgnoreCase(status) && !"Recycled".equalsIgnoreCase(currentStatus)) {
            User user = submission.getUser();
            user.setRewardPoints(user.getRewardPoints() + 100);
            userRepository.save(user);

            Reward reward = new Reward();
            reward.setUser(user);
            reward.setWasteSubmission(submission);
            reward.setPoints(100);
            reward.setDate(LocalDateTime.now());
            rewardRepository.save(reward);
        }

        return ResponseEntity.ok(submission);
    }

    // Get Tracking Logs
    @GetMapping("/{wasteId}/tracking")
    public ResponseEntity<List<TransportationLog>> getTrackingLogs(@PathVariable String wasteId) {
        return ResponseEntity.ok(logRepository.findByWasteSubmissionWasteIdOrderByDateAscTimeAsc(wasteId));
    }

    // Get user rewards
    @GetMapping("/user/{userId}/rewards")
    public ResponseEntity<List<Reward>> getUserRewards(@PathVariable Integer userId) {
        return ResponseEntity.ok(rewardRepository.findByUserUserIdOrderByDateDesc(userId));
    }
}
