package com.ewaste.controller;

import com.ewaste.model.Reward;
import com.ewaste.model.WasteSubmission;
import com.ewaste.repository.CollectionCenterRepository;
import com.ewaste.repository.RewardRepository;
import com.ewaste.repository.UserRepository;
import com.ewaste.repository.WasteSubmissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.*;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
public class AdminController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WasteSubmissionRepository wasteRepository;

    @Autowired
    private CollectionCenterRepository centerRepository;

    @Autowired
    private RewardRepository rewardRepository;

    @GetMapping("/stats")
    public ResponseEntity<?> getStats() {
        long totalUsers = userRepository.findAll().stream()
                .filter(u -> u.getCenter() == null) // only standard customers
                .count();
        long totalWasteSubmitted = wasteRepository.count();
        long totalRecycled = wasteRepository.countByStatus("Recycled");
        long pendingRequests = wasteRepository.countByStatus("Pending");
        long collectionCenters = centerRepository.count();

        int totalRewardPointsIssued = rewardRepository.findAll().stream()
                .mapToInt(Reward::getPoints)
                .sum();

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalUsers", totalUsers);
        stats.put("totalWasteSubmitted", totalWasteSubmitted);
        stats.put("totalRecycled", totalRecycled);
        stats.put("pendingRequests", pendingRequests);
        stats.put("collectionCenters", collectionCenters);
        stats.put("rewardPointsIssued", totalRewardPointsIssued);

        return ResponseEntity.ok(stats);
    }

    @GetMapping("/charts")
    public ResponseEntity<?> getChartsData() {
        List<WasteSubmission> allWaste = wasteRepository.findAll();

        // 1. Device Type Counts (for Pie Chart)
        Map<String, Integer> deviceCounts = new HashMap<>();
        for (WasteSubmission ws : allWaste) {
            String device = ws.getDeviceName();
            deviceCounts.put(device, deviceCounts.getOrDefault(device, 0) + ws.getQuantity());
        }

        // 2. Monthly Recycling Counts (for Bar Chart)
        // Group by Month (e.g. "January", "February", etc.)
        Map<String, Integer> monthlyRecycled = new LinkedHashMap<>();
        
        // Let's pre-populate the last 6 months to make the chart look nice and complete
        DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("MMM yyyy");
        for (int i = 5; i >= 0; i--) {
            String monthLabel = java.time.LocalDate.now().minusMonths(i).format(monthFormatter);
            monthlyRecycled.put(monthLabel, 0);
        }

        for (WasteSubmission ws : allWaste) {
            if ("Recycled".equalsIgnoreCase(ws.getStatus())) {
                String monthLabel = ws.getDate().format(monthFormatter);
                if (monthlyRecycled.containsKey(monthLabel)) {
                    monthlyRecycled.put(monthLabel, monthlyRecycled.get(monthLabel) + ws.getQuantity());
                } else {
                    // Fallback if not within the pre-populated last 6 months
                    monthlyRecycled.put(monthLabel, ws.getQuantity());
                }
            }
        }

        Map<String, Object> chartsData = new HashMap<>();
        chartsData.put("deviceCounts", deviceCounts);
        chartsData.put("monthlyRecycled", monthlyRecycled);

        return ResponseEntity.ok(chartsData);
    }
}
