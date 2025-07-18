package com.localride.server.controller;

import com.localride.common.model.UserRegistrationRequest;
import com.localride.common.model.UserResponseDTO;
import com.localride.server.mapper.UserMapper;
import com.localride.server.model.User; // این را اضافه کنید
import com.localride.server.service.UserService;
import org.locationtech.jts.geom.Point; // این را اضافه کنید
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;
    private final UserMapper userMapper; // <<-- Declare the mapper

    @Autowired // <<-- Inject the mapper here
    public UserController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper; // <<-- Initialize the mapper
    }

    // متد اصلی برای دریافت همه کاربران (فقط ادمین و ساپورت)
    @GetMapping // All Users
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPPORT')")
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        logger.info("Received request to get all users");
        List<User> users = userService.getAllUsers();
        // Use the mapper here
        List<UserResponseDTO> userDTOs = userMapper.toDtoList(users);
        return ResponseEntity.ok(userDTOs);
    }

    @PutMapping("/{id}") // Update User
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPPORT')")
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable Long id, @RequestBody UserRegistrationRequest request) {
        logger.info("Received request to update user with id: {}", id);
        return userService.updateUser(id, request)
                // Use the mapper here
                .map(user -> new ResponseEntity<>(userMapper.toDto(user), HttpStatus.OK))
                .orElseGet(() -> {
                    logger.warn("User not found with id: {}", id);
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                });
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPPORT')") // مثال: دسترسی برای ادمین/ساپورت
    // اگر کاربر بتواند پروفایل خودش را حذف کند: @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPPORT') or @securityService.isOwner(#id)")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        logger.info("Received request to delete user with id: {}", id);
        if (userService.deleteUser(id)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            logger.warn("User not found with id: {}", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // متد برای دریافت کاربران هم‌نوع (مثل راننده‌ها فقط راننده‌ها) - نام متد اصلاح شد
    @GetMapping("/my-role")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<UserResponseDTO>> getUsersByMyRole() { // نام متد از getAllUsers به getUsersByMyRole تغییر یافت
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        Optional<String> currentUserRole = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .filter(a -> !a.startsWith("ROLE_")) // اگر Authorityها با ROLE_ شروع می شوند
                .findFirst();

        if (currentUserRole.isPresent()) {
            String role = currentUserRole.get();
            List<User> users;
            if ("ADMIN".equalsIgnoreCase(role)) {
                users = userService.getAllUsers();
            } else {
                users = userService.getUsersByRole(role);
            }
            // Use the mapper here
            List<UserResponseDTO> userDTOs = userMapper.toDtoList(users);
            return ResponseEntity.ok(userDTOs);
        } else {
            return ResponseEntity.badRequest().body(Collections.emptyList());
        }
    }

    @GetMapping("/{id}") // Get User by ID (assuming you added this as per previous conversation)
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPPORT') or @securityService.isOwner(#id)")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id) {
        logger.info("Received request to get user with id: {}", id);
        return userService.getUserById(id)
                // Use the mapper here
                .map(userMapper::toDto) // shorthand for user -> userMapper.toDto(user)
                .map(userDTO -> new ResponseEntity<>(userDTO, HttpStatus.OK))
                .orElseGet(() -> {
                    logger.warn("User not found with id: {}", id);
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                });
    }

    // متد برای دریافت راننده/مسافر‌های نزدیک
    @GetMapping("/nearby")
    @PreAuthorize("hasAnyAuthority('DRIVER', 'PASSENGER')")
    public ResponseEntity<List<UserResponseDTO>> getNearbyUsers(@RequestParam(defaultValue = "5000") double radiusInMeters) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User currentUser = userService.getUserByUsername(username); // این متد User Entity برمی‌گرداند
        Point currentLocation = currentUser.getLocation();

        if (currentLocation == null) {
            logger.warn("Current user {} has no location set, cannot find nearby users.", username);
            return ResponseEntity.badRequest().body(Collections.emptyList());
        }

        Optional<String> currentUserRole = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .filter(a -> !a.startsWith("ROLE_"))
                .findFirst();

        if (currentUserRole.isPresent()) {
            String targetRole = null;
            if ("DRIVER".equalsIgnoreCase(currentUserRole.get())) {
                targetRole = "PASSENGER";
            } else if ("PASSENGER".equalsIgnoreCase(currentUserRole.get())) {
                targetRole = "DRIVER";
            }

            if (targetRole != null) {
                List<User> nearbyUsers = userService.findNearbyUsers(currentLocation, targetRole, radiusInMeters);
                // Use the mapper here
                List<UserResponseDTO> userDTOs = userMapper.toDtoList(nearbyUsers);
                return ResponseEntity.ok(userDTOs);
            }
        }
        logger.warn("Invalid role for nearby search for user: {}", username);
        return ResponseEntity.badRequest().body(Collections.emptyList());
    }

    // متد کمکی برای تبدیل User Entity به UserResponseDTO
    private UserResponseDTO convertToDto(User user) {
        if (user == null) {
            return null;
        }
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setRole(user.getRole() != null ? user.getRole().name() : null); // تبدیل Role enum به String
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getPhone());

        // تبدیل Point به latitude و longitude
        if (user.getLocation() != null) {
            dto.setLatitude(user.getLocation().getY()); // Latitude (عرض جغرافیایی)
            dto.setLongitude(user.getLocation().getX()); // Longitude (طول جغرافیایی)
        } else {
            dto.setLatitude(null); // اکنون Double می تواند null باشد
            dto.setLongitude(null); // اکنون Double می تواند null باشد
        }

        dto.setCreatedAt(user.getCreatedAt()); // اختصاص مستقیم LocalDateTime

        return dto;
    }

}