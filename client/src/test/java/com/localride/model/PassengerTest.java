package com.localride.model;


import com.localride.model.enums.PassengerStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Passenger Class Tests")
public class PassengerTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    void setUpStreams() {
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    void restoreStreams() {
        System.setOut(originalOut);
    }

    @Test
    @DisplayName("Should create passenger with name and phone number")
    void testPassengerCreationWithPhone() {
        Passenger passenger = new Passenger("Bob", "987-6543210");
        assertNotNull(passenger);
        assertEquals("Bob", passenger.getName());
        assertEquals("987-6543210", passenger.getPhoneNumber());
        assertEquals(PassengerStatus.NOTREQUESTED, passenger.getStatus());
        assertEquals(0, passenger.getRating());
        assertNotEquals(0, passenger.getId());
    }

    @Test
    @DisplayName("Should create passenger with only name and default phone number")
    void testPassengerCreationOnlyName() {
        Passenger passenger = new Passenger("Charlie");
        assertNotNull(passenger);
        assertEquals("Charlie", passenger.getName());
        assertEquals("000-000000", passenger.getPhoneNumber()); // Default phone number
        assertEquals(PassengerStatus.NOTREQUESTED, passenger.getStatus());
        assertEquals(0, passenger.getRating());
        assertNotEquals(0, passenger.getId());
    }

    @Test
    @DisplayName("Should update passenger status to REQUESTED on ride request")
    void testRequestRide() {
        Passenger passenger = new Passenger("David");
        outContent.reset(); // Clear any initial output

        passenger.requestRide();
        assertEquals(PassengerStatus.REQUESTED, passenger.getStatus());
        assertTrue(outContent.toString().contains("David ride requested."), "Console output should confirm ride request.");
    }

    @Test
    @DisplayName("Should update passenger status using setStatus")
    void testSetStatus() {
        Passenger passenger = new Passenger("Eve");
        passenger.setStatus(PassengerStatus.INRIDE);
        assertEquals(PassengerStatus.INRIDE, passenger.getStatus());
        passenger.setStatus(PassengerStatus.NOTREQUESTED);
        assertEquals(PassengerStatus.NOTREQUESTED, passenger.getStatus());
    }

    @Test
    @DisplayName("Should print cancellation message when cancelRide is called")
    void testCancelRide() {
        Passenger passenger = new Passenger("Frank");
        outContent.reset();
        passenger.cancelRide();
        assertTrue(outContent.toString().contains("Frank ride Cancel."), "Console output should confirm passenger cancellation.");
        // Note: This method currently only prints, it does not change internal status.
        // For a more comprehensive test, one might want to mock/verify interaction if status changes were expected.
    }
}