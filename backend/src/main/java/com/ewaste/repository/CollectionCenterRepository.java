package com.ewaste.repository;

import com.ewaste.model.CollectionCenter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CollectionCenterRepository extends JpaRepository<CollectionCenter, Integer> {
}
