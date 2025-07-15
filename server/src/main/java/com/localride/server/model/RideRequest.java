package com.localride.server.model;
import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * RideRequest entity representing a ride request in the Local Ride Assistant system.
 * It includes fields for passenger, start and end locations, status, and timestamps.
 */
@Entity
@Table(name = "ride_requests")
public class RideRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "passenger_id", nullable = false)
    private User passenger;

    @Column(name = "start_location", columnDefinition = "GEOMETRY")
    private String startLocation;

    @Column(name = "end_location", columnDefinition = "GEOMETRY")
    private String endLocation;

    private String status;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public User getPassenger() { return passenger; }
    public void setPassenger(User passenger) { this.passenger = passenger; }
    public String getStartLocation() { return startLocation; }
    public void setStartLocation(String startLocation) { this.startLocation = startLocation; }
    public String getEndLocation() { return endLocation; }
    public void setEndLocation(String endLocation) { this.endLocation = endLocation; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
