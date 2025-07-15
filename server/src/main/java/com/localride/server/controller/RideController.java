package com.localride.server.controller;


import com.localride.server.dto.RideRequestDTO;
import com.localride.server.model.RideRequest;
import com.localride.server.model.User;
import com.localride.server.repository.RideRequestRepository;
import com.localride.server.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rides")
public class RideController {
    @Autowired
    private RideRequestRepository rideRequestRepository;
    @Autowired
    private UserRepository userRepository;

    @PostMapping("/request")
    public ResponseEntity<String> requestRide(@RequestBody RideRequestDTO rideRequestDTO) {
        return userRepository.findById(rideRequestDTO.getPassengerId())
                .map(passenger -> {
                    RideRequest rideRequest = new RideRequest();
                    rideRequest.setPassenger(passenger);
                    rideRequest.setStartLocation(rideRequestDTO.getStartLocation());
                    rideRequest.setEndLocation(rideRequestDTO.getEndLocation());
                    rideRequest.setStatus("REQUESTED");
                    rideRequestRepository.save(rideRequest);
                    return ResponseEntity.ok("Ride request created with ID: " + rideRequest.getId());
                })
                .orElseGet(() -> ResponseEntity.status(404).body("Passenger not found"));
    }
}
