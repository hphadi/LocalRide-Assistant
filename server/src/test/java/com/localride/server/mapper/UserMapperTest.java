package com.localride.server.mapper;

import com.localride.server.dto.UserResponseDTO;
import com.localride.server.model.Role;
import com.localride.server.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {

    private UserMapper userMapper;
    private GeometryFactory geometryFactory;
    private WKTReader wktReader;

    @BeforeEach
    void setUp() {
        userMapper = new UserMapper();
        geometryFactory = new GeometryFactory();
        wktReader = new WKTReader(geometryFactory);
    }

    @Test
    @DisplayName("Should convert User entity to UserResponseDTO with location")
    void toDto_UserWithLocation_ReturnsCorrectDto() throws ParseException {
        // Given
        User user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setRole(Role.PASSENGER);
        user.setName("Test User");
        user.setEmail("test@example.com");
        user.setPhone("1234567890");
        Point location = (Point) wktReader.read("POINT(10.0 20.0)");
        user.setLocation(location);
        user.setCreatedAt(LocalDateTime.now());

        // When
        UserResponseDTO dto = userMapper.toDto(user);

        // Then
        assertNotNull(dto);
        assertEquals(user.getId(), dto.getId());
        assertEquals(user.getUsername(), dto.getUsername());
        assertEquals(user.getRole().name(), dto.getRole()); // Check if role enum name is correctly converted to String
        assertEquals(user.getName(), dto.getName());
        assertEquals(user.getEmail(), dto.getEmail());
        assertEquals(user.getPhone(), dto.getPhone());
        assertEquals(user.getLocation().getY(), dto.getLatitude()); // Y is latitude
        assertEquals(user.getLocation().getX(), dto.getLongitude()); // X is longitude
        assertEquals(user.getCreatedAt(), dto.getCreatedAt());
    }

    @Test
    @DisplayName("Should convert User entity to UserResponseDTO without location")
    void toDto_UserWithoutLocation_ReturnsCorrectDto() {
        // Given
        User user = new User();
        user.setId(2L);
        user.setUsername("anotheruser");
        user.setRole(Role.DRIVER);
        user.setName("Another User");
        user.setEmail("another@example.com");
        user.setPhone("0987654321");
        user.setLocation(null); // No location
        user.setCreatedAt(LocalDateTime.now());

        // When
        UserResponseDTO dto = userMapper.toDto(user);

        // Then
        assertNotNull(dto);
        assertEquals(user.getId(), dto.getId());
        assertEquals(user.getUsername(), dto.getUsername());
        assertEquals(user.getRole().name(), dto.getRole());
        assertNull(dto.getLatitude()); // Assert latitude is null
        assertNull(dto.getLongitude()); // Assert longitude is null
    }

    @Test
    @DisplayName("Should return null if input User entity is null")
    void toDto_NullUser_ReturnsNull() {
        // Given
        User user = null;

        // When
        UserResponseDTO dto = userMapper.toDto(user);

        // Then
        assertNull(dto);
    }

    @Test
    @DisplayName("Should convert a list of User entities to a list of UserResponseDTOs")
    void toDtoList_ListOfUsers_ReturnsListOfDtos() throws ParseException {
        // Given
        User user1 = new User();
        user1.setId(1L);
        user1.setUsername("user1");
        user1.setRole(Role.PASSENGER);
        user1.setLocation((Point) wktReader.read("POINT(10 20)"));

        User user2 = new User();
        user2.setId(2L);
        user2.setUsername("user2");
        user2.setRole(Role.DRIVER);
        user2.setLocation((Point) wktReader.read("POINT(30 40)"));

        List<User> users = Arrays.asList(user1, user2);

        // When
        List<UserResponseDTO> dtoList = userMapper.toDtoList(users);

        // Then
        assertNotNull(dtoList);
        assertEquals(2, dtoList.size());
        assertEquals("user1", dtoList.get(0).getUsername());
        assertEquals("user2", dtoList.get(1).getUsername());
        assertEquals(10.0, dtoList.get(0).getLongitude());
        assertEquals(40.0, dtoList.get(1).getLatitude());
    }

    @Test
    @DisplayName("Should return empty list if input list is empty")
    void toDtoList_EmptyList_ReturnsEmptyList() {
        // Given
        List<User> users = Collections.emptyList();

        // When
        List<UserResponseDTO> dtoList = userMapper.toDtoList(users);

        // Then
        assertNotNull(dtoList);
        assertTrue(dtoList.isEmpty());
    }

    @Test
    @DisplayName("Should return null if input list is null")
    void toDtoList_NullList_ReturnsNull() {
        // Given
        List<User> users = null;

        // When
        List<UserResponseDTO> dtoList = userMapper.toDtoList(users);

        // Then
        assertNull(dtoList);
    }
}