package com.localride.common.model;


import com.localride.common.model.enums.PassengerStatus;
import org.junit.jupiter.api.*;

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
        Assertions.assertNotNull(passenger);
        Assertions.assertEquals("Bob", passenger.getName());
        Assertions.assertEquals("987-6543210", passenger.getPhoneNumber());
        Assertions.assertEquals(PassengerStatus.NOTREQUESTED, passenger.getStatus());
        Assertions.assertEquals(0, passenger.getRating());
        Assertions.assertNotEquals(0, passenger.getId());
    }

    @Test
    @DisplayName("Should create passenger with only name and default phone number")
    void testPassengerCreationOnlyName() {
        Passenger passenger = new Passenger("Charlie");
        Assertions.assertNotNull(passenger);
        Assertions.assertEquals("Charlie", passenger.getName());
        Assertions.assertEquals("000-000000", passenger.getPhoneNumber()); // Default phone number
        Assertions.assertEquals(PassengerStatus.NOTREQUESTED, passenger.getStatus());
        Assertions.assertEquals(0, passenger.getRating());
        Assertions.assertNotEquals(0, passenger.getId());
    }

    @Test
    @DisplayName("Should update passenger status to REQUESTED on ride request")
    void testRequestRide() {
        Passenger passenger = new Passenger("David");
        outContent.reset(); // Clear any initial output

        passenger.requestRide();
        Assertions.assertEquals(PassengerStatus.REQUESTED, passenger.getStatus());
        Assertions.assertTrue(outContent.toString().contains("David ride requested."), "Console output should confirm ride request.");
    }

    @Test
    @DisplayName("Should update passenger status using setStatus")
    void testSetStatus() {
        Passenger passenger = new Passenger("Eve");
        passenger.setStatus(PassengerStatus.INRIDE);
        Assertions.assertEquals(PassengerStatus.INRIDE, passenger.getStatus());
        passenger.setStatus(PassengerStatus.NOTREQUESTED);
        Assertions.assertEquals(PassengerStatus.NOTREQUESTED, passenger.getStatus());
    }

    @Test
    @DisplayName("Should print cancellation message when cancelRide is called")
    void testCancelRide() {
        Passenger passenger = new Passenger("Frank");
        outContent.reset();
        passenger.cancelRide();
        Assertions.assertTrue(outContent.toString().contains("Frank ride Cancel."), "Console output should confirm passenger cancellation.");
        // Note: This method currently only prints, it does not change internal status.
        // For a more comprehensive test, one might want to mock/verify interaction if status changes were expected.
    }
}