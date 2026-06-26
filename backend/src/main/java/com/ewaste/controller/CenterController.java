package com.ewaste.controller;

import com.ewaste.model.CollectionCenter;
import com.ewaste.repository.CollectionCenterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
}
