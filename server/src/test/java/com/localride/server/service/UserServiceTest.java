package com.localride.server.service;

import com.localride.server.dto.UserRegistrationRequest;
import com.localride.server.model.Role;
import com.localride.server.model.User;
import com.localride.server.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private GeometryFactory geometryFactory;
    private WKTReader wktReader;

    @BeforeEach
    void setUp() {
        geometryFactory = new GeometryFactory();
        wktReader = new WKTReader(geometryFactory);

        // IMPORTANT: Removed passwordEncoder stubbings from here.
        // They are now moved to specific test methods where they are used (e.g., registerUser_Success, updateUser_UserExists_UpdatesUser).
        // This prevents UnnecessaryStubbingException by ensuring stubs are only active when actually invoked.
    }

    @Test
    @DisplayName("Should register a new user successfully")
    void registerUser_Success() {
        // Given
        UserRegistrationRequest request = new UserRegistrationRequest();
        request.setUsername("newuser");
        request.setPassword("password123");
        request.setRole(Role.PASSENGER); // Corrected: Using Role enum directly as per DTO
        request.setName("New User");
        request.setEmail("new@example.com");
        request.setPhone("1234567890");
        request.setLocation("POINT(1.0 2.0)");

        when(userRepository.findByUsername(request.getUsername())).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Stub passwordEncoder.encode() because registerUser calls it to hash the password.
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

        // When
        User registeredUser = userService.registerUser(request);

        // Then
        assertNotNull(registeredUser);
        assertEquals("newuser", registeredUser.getUsername());
        assertEquals("encodedPassword", registeredUser.getPasswordHash());
        assertEquals(Role.PASSENGER, registeredUser.getRole()); // Assert against Role enum
        assertNotNull(registeredUser.getLocation());
        assertEquals(1.0, registeredUser.getLocation().getX());
        assertEquals(2.0, registeredUser.getLocation().getY());
        verify(userRepository, times(1)).findByUsername(request.getUsername());
        verify(userRepository, times(1)).save(any(User.class));
        verify(passwordEncoder, times(1)).encode(request.getPassword()); // Verify encode was called
    }

    @Test
    @DisplayName("Should throw RuntimeException if username already exists during registration")
    void registerUser_UsernameAlreadyExists_ThrowsException() {
        // Given
        UserRegistrationRequest request = new UserRegistrationRequest();
        request.setUsername("existinguser");
        request.setPassword("password123");
        request.setRole(Role.PASSENGER); // Corrected: Using Role enum directly

        when(userRepository.findByUsername(request.getUsername())).thenReturn(Optional.of(new User()));

        // When / Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.registerUser(request));
        assertEquals("Username already exists", exception.getMessage());
        verify(userRepository, times(1)).findByUsername(request.getUsername());
        verify(userRepository, never()).save(any(User.class)); // Save should not be called
        verify(passwordEncoder, never()).encode(anyString()); // encode() should not be called if username exists early
    }

    @Test
    @DisplayName("Should retrieve a user by ID successfully")
    void getUserById_UserExists_ReturnsUser() {
        // Given
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        user.setUsername("testuser");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // When
        Optional<User> foundUser = userService.getUserById(userId);

        // Then
        assertTrue(foundUser.isPresent());
        assertEquals(userId, foundUser.get().getId());
        verify(userRepository, times(1)).findById(userId);
        // passwordEncoder.encode() is NOT called in getUserById, so no stubbing for it here.
    }

    @Test
    @DisplayName("Should return empty Optional if user not found by ID")
    void getUserById_UserDoesNotExist_ReturnsEmptyOptional() {
        // Given
        Long userId = 99L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // When
        Optional<User> foundUser = userService.getUserById(userId);

        // Then
        assertFalse(foundUser.isPresent());
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    @DisplayName("Should retrieve all users successfully")
    void getAllUsers_ReturnsListOfUsers() {
        // Given
        User user1 = new User(); user1.setId(1L); user1.setUsername("u1");
        User user2 = new User(); user2.setId(2L); user2.setUsername("u2");
        List<User> users = Arrays.asList(user1, user2);

        when(userRepository.findAll()).thenReturn(users);

        // When
        List<User> allUsers = userService.getAllUsers();

        // Then
        assertNotNull(allUsers);
        assertEquals(2, allUsers.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should update an existing user successfully")
    void updateUser_UserExists_UpdatesUser() {
        // Given
        Long userId = 1L;
        User existingUser = new User();
        existingUser.setId(userId);
        existingUser.setUsername("olduser");
        existingUser.setRole(Role.PASSENGER);
        existingUser.setPasswordHash("oldHash"); // Existing hash, should be replaced

        UserRegistrationRequest updateRequest = new UserRegistrationRequest();
        updateRequest.setUsername("updateduser");
        updateRequest.setPassword("newpassword"); // A new password is provided
        updateRequest.setRole(Role.DRIVER); // Corrected: Using Role enum directly
        updateRequest.setName("Updated Name");
        updateRequest.setEmail("updated@example.com"); // Added email update for completeness
        updateRequest.setPhone("0987654321"); // Added phone update
        updateRequest.setLocation("POINT(5.0 6.0)");

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Stub passwordEncoder.encode() because updateUser calls it if a new password is provided.
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

        // When
        Optional<User> updatedUserOptional = userService.updateUser(userId, updateRequest);

        // Then
        assertTrue(updatedUserOptional.isPresent());
        User updatedUser = updatedUserOptional.get();
        assertEquals(userId, updatedUser.getId());
        assertEquals("updateduser", updatedUser.getUsername());
        assertEquals("encodedPassword", updatedUser.getPasswordHash()); // Should be the new encoded password
        assertEquals(Role.DRIVER, updatedUser.getRole());
        assertEquals("Updated Name", updatedUser.getName());
        assertEquals("updated@example.com", updatedUser.getEmail());
        assertEquals("0987654321", updatedUser.getPhone());
        assertNotNull(updatedUser.getLocation());
        assertEquals(5.0, updatedUser.getLocation().getX());
        assertEquals(6.0, updatedUser.getLocation().getY());

        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).save(existingUser);
        verify(passwordEncoder, times(1)).encode(updateRequest.getPassword()); // Verify encode was called
    }

    @Test
    @DisplayName("Should update an existing user without changing password if not provided")
    void updateUser_UserExists_NoPasswordChange() {
        // Given
        Long userId = 1L;
        User existingUser = new User();
        existingUser.setId(userId);
        existingUser.setUsername("olduser");
        existingUser.setRole(Role.PASSENGER);
        existingUser.setPasswordHash("oldHash"); // Existing hash should remain

        UserRegistrationRequest updateRequest = new UserRegistrationRequest();
        updateRequest.setUsername("updateduser");
        updateRequest.setPassword(null); // No new password provided
        updateRequest.setRole(Role.DRIVER);
        updateRequest.setName("Updated Name");
        updateRequest.setLocation("POINT(5.0 6.0)");

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));
        // passwordEncoder.encode() is NOT called if password is null/empty, so no stubbing here.

        // When
        Optional<User> updatedUserOptional = userService.updateUser(userId, updateRequest);

        // Then
        assertTrue(updatedUserOptional.isPresent());
        User updatedUser = updatedUserOptional.get();
        assertEquals("oldHash", updatedUser.getPasswordHash()); // Password hash should remain unchanged
        verify(passwordEncoder, never()).encode(anyString()); // Verify encode was NOT called
    }


    @Test
    @DisplayName("Should return empty Optional when updating a non-existent user")
    void updateUser_UserDoesNotExist_ReturnsEmptyOptional() {
        // Given
        Long userId = 99L;
        UserRegistrationRequest updateRequest = new UserRegistrationRequest();
        updateRequest.setUsername("anyuser"); // Minimal request for a non-existent user
        updateRequest.setPassword("anypass");
        updateRequest.setRole(Role.PASSENGER); // Using enum for completeness

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // When
        Optional<User> updatedUser = userService.updateUser(userId, updateRequest);

        // Then
        assertFalse(updatedUser.isPresent());
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, never()).save(any(User.class)); // Save should not be called
        verify(passwordEncoder, never()).encode(anyString()); // encode() should not be called
    }

    @Test
    @DisplayName("Should delete a user successfully")
    void deleteUser_UserExists_ReturnsTrue() {
        // Given
        Long userId = 1L;
        when(userRepository.existsById(userId)).thenReturn(true);
        doNothing().when(userRepository).deleteById(userId);

        // When
        boolean isDeleted = userService.deleteUser(userId);

        // Then
        assertTrue(isDeleted);
        verify(userRepository, times(1)).existsById(userId);
        verify(userRepository, times(1)).deleteById(userId);
    }

    @Test
    @DisplayName("Should not delete a user if user does not exist")
    void deleteUser_UserDoesNotExist_ReturnsFalse() {
        // Given
        Long userId = 99L;
        when(userRepository.existsById(userId)).thenReturn(false);

        // When
        boolean isDeleted = userService.deleteUser(userId);

        // Then
        assertFalse(isDeleted);
        verify(userRepository, times(1)).existsById(userId);
        verify(userRepository, never()).deleteById(anyLong()); // deleteById should not be called
    }

    @Test
    @DisplayName("Should retrieve users by role successfully")
    void getUsersByRole_ReturnsFilteredUsers() {
        // Given
        String roleName = "DRIVER"; // This method expects a String role
        User driver1 = new User(); driver1.setId(1L); driver1.setRole(Role.DRIVER);
        User driver2 = new User(); driver2.setId(2L); driver2.setRole(Role.DRIVER);
        List<User> drivers = Arrays.asList(driver1, driver2);

        when(userRepository.findByRole(roleName)).thenReturn(drivers);

        // When
        List<User> foundDrivers = userService.getUsersByRole(roleName);

        // Then
        assertNotNull(foundDrivers);
        assertEquals(2, foundDrivers.size());
        assertEquals(Role.DRIVER, foundDrivers.get(0).getRole());
        verify(userRepository, times(1)).findByRole(roleName);
    }

    @Test
    @DisplayName("Should find nearby users correctly")
    void findNearbyUsers_ReturnsNearbyUsers() throws ParseException {
        // Given
        Point currentLocation = (Point) wktReader.read("POINT(0.0 0.0)");
        String targetRole = "PASSENGER";
        double radius = 1000.0;

        User nearbyUser1 = new User(); nearbyUser1.setId(1L); nearbyUser1.setRole(Role.PASSENGER);
        User nearbyUser2 = new User(); nearbyUser2.setId(2L); nearbyUser2.setRole(Role.PASSENGER);
        List<User> nearbyUsers = Arrays.asList(nearbyUser1, nearbyUser2);

        when(userRepository.findNearbyUsers(currentLocation, targetRole, radius)).thenReturn(nearbyUsers);

        // When
        List<User> foundUsers = userService.findNearbyUsers(currentLocation, targetRole, radius);

        // Then
        assertNotNull(foundUsers);
        assertEquals(2, foundUsers.size());
        assertEquals(nearbyUser1.getId(), foundUsers.get(0).getId());
        assertEquals(nearbyUser2.getId(), foundUsers.get(1).getId());
        verify(userRepository, times(1)).findNearbyUsers(currentLocation, targetRole, radius);
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException if current location is null for nearby search")
    void findNearbyUsers_NullCurrentLocation_ThrowsException() {
        // When / Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> userService.findNearbyUsers(null, "DRIVER", 1000.0));
        assertEquals("Current location cannot be null for nearby user search.", exception.getMessage());
        verify(userRepository, never()).findNearbyUsers(any(), anyString(), anyDouble()); // Ensure no repository call if location is null
    }

    @Test
    @DisplayName("Should retrieve user by username successfully")
    void getUserByUsername_UserExists_ReturnsUser() {
        // Given
        String username = "existingUser";
        User user = new User();
        user.setUsername(username);
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        // When
        User foundUser = userService.getUserByUsername(username);

        // Then
        assertNotNull(foundUser);
        assertEquals(username, foundUser.getUsername());
        verify(userRepository, times(1)).findByUsername(username);
    }

    @Test
    @DisplayName("Should throw RuntimeException if user not found by username")
    void getUserByUsername_UserDoesNotExist_ThrowsException() {
        // Given
        String username = "nonExistentUser";
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        // When / Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.getUserByUsername(username));
        assertEquals("User not found with username: " + username, exception.getMessage());
        verify(userRepository, times(1)).findByUsername(username);
    }
}