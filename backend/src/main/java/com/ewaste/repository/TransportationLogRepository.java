package com.ewaste.repository;

import com.ewaste.model.TransportationLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransportationLogRepository extends JpaRepository<TransportationLog, Integer> {
    List<TransportationLog> findByWasteSubmissionWasteIdOrderByDateDescTimeDesc(String wasteId);
    List<TransportationLog> findByWasteSubmissionWasteIdOrderByDateAscTimeAsc(String wasteId);
}
