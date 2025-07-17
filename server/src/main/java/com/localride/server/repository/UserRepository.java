package com.localride.server.repository;

import com.localride.server.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.List;

/**
 * UserRepository interface for managing User entities.
 * It extends JpaRepository to provide CRUD operations and custom query methods.
 */
public interface UserRepository extends JpaRepository <User, Long> {
    // Method to find a user by username
   Optional<User> findByUsername(String username);

    List<User> findByRole(String role);
    // Method to check if a user exists by username
    boolean existsByUsername(String username);
    // Method to check if a user exists by email
    boolean existsByEmail(String email);
    // Method to find a user by email
    Optional<User> findByEmail(String email);
    @Query(value = "SELECT u FROM User u WHERE u.role = :targetRole AND FUNCTION('ST_DWithin', u.location, :currentLocationPoint, :radiusInMeters) = true")
    List<User> findNearbyUsers(@Param("currentLocationPoint") org.locationtech.jts.geom.Point currentLocationPoint,
                               @Param("targetRole") String targetRole,
                               @Param("radiusInMeters") double radiusInMeters);

}
