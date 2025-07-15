package com.localride.server.repository;

import com.localride.server.model.RideRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RideRequestRepository extends JpaRepository<RideRequest, Long> {
    // Custom query methods can be defined here if needed
    // For example, to find ride requests by user ID or status
    // Optional<List<RideRequest>> findByUserId(Long userId);
    // List<RideRequest> findByStatus(String status);
}