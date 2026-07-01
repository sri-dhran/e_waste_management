package com.ewaste.controller;

import com.ewaste.model.CollectionCenter;
import com.ewaste.repository.CollectionCenterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/centers")
@CrossOrigin(origins = "*")
public class CenterController {

    @Autowired
    private CollectionCenterRepository centerRepository;

    @GetMapping
    public ResponseEntity<List<CollectionCenter>> getCenters() {
        return ResponseEntity.ok(centerRepository.findAll());
    }

    @GetMapping("/nearby")
    public ResponseEntity<?> getNearbyCenters(
            @RequestParam("latitude") double userLat,
            @RequestParam("longitude") double userLon,
            @RequestParam(value = "radius", defaultValue = "50.0") double radius) {

        List<CollectionCenter> allCenters = centerRepository.findAll();
        List<Map<String, Object>> nearbyCenters = new ArrayList<>();

        for (CollectionCenter center : allCenters) {
            if (center.getLatitude() != null && center.getLongitude() != null) {
                double distance = calculateDistanceInKilometers(userLat, userLon, center.getLatitude(), center.getLongitude());
                if (distance <= radius) {
                    Map<String, Object> centerMap = new HashMap<>();
                    centerMap.put("centerId", center.getCenterId());
                    centerMap.put("centerName", center.getCenterName());
                    centerMap.put("location", center.getLocation());
                    centerMap.put("contact", center.getContact());
                    centerMap.put("latitude", center.getLatitude());
                    centerMap.put("longitude", center.getLongitude());
                    centerMap.put("distance", Math.round(distance * 100.0) / 100.0); // round to 2 decimal places
                    nearbyCenters.add(centerMap);
                }
            }
        }

        // Sort by distance ascending
        nearbyCenters.sort((c1, c2) -> Double.compare((double) c1.get("distance"), (double) c2.get("distance")));

        return ResponseEntity.ok(nearbyCenters);
    }

    private double calculateDistanceInKilometers(double lat1, double lon1, double lat2, double lon2) {
        double lonDiff = Math.toRadians(lon2 - lon1);
        double latDiff = Math.toRadians(lat2 - lat1);
        double a = Math.sin(latDiff / 2) * Math.sin(latDiff / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDiff / 2) * Math.sin(lonDiff / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double earthRadius = 6371; // In kilometers
        return earthRadius * c;
    }
}
