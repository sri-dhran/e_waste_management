package com.ewaste.repository;

import com.ewaste.model.WasteSubmission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WasteSubmissionRepository extends JpaRepository<WasteSubmission, String> {
    List<WasteSubmission> findByUserUserIdOrderByDateDesc(Integer userId);
    List<WasteSubmission> findAllByOrderByDateDesc();
    long countByStatus(String status);
}
