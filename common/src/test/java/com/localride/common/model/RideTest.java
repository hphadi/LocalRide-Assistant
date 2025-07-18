package com.localride.common.model;

import com.localride.common.model.enums.CancelRole;
import com.localride.common.model.enums.DriverStatus;
import com.localride.common.model.enums.PassengerStatus;
import com.localride.common.model.enums.RideStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Ride Class Tests")
public class RideTest {

    private Passenger testPassenger;
    private Driver testDriver;
    // We still keep outContent for debugging/logging, but assertions will rely on method return values
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    void setUp() {
        testPassenger = new Passenger("Alice", "123-4567890");
        testDriver = new Driver("Bob", "Toyota Camry");
        System.setOut(new PrintStream(outContent)); // Still capture output if needed for debugging
    }

    @AfterEach
    void restoreStreams() {
        System.setOut(originalOut);
    }

    @Test
    @DisplayName("Should create a ride with correct initial status and IDs")
    void testRideCreation() {
        Ride ride = new Ride(testPassenger, testDriver);
        assertNotNull(ride, "Ride object should not be null after creation.");
        assertNotEquals(0, ride.getId(), "Ride ID should be assigned and not 0.");
        assertEquals(testPassenger, ride.getPassenger(), "The passenger object should be correctly linked.");
        assertEquals(testDriver, ride.getDriver(), "The driver object should be correctly linked.");
        assertEquals(RideStatus.PENDING, ride.getStatus(), "Initial ride status should be PENDING.");
        assertFalse(ride.isRideActive(), "Ride should not be active immediately after creation in constructor.");
    }

    @Test
    @DisplayName("Should start a ride successfully from ACCEPTED or REQUESTED status")
    void testStartRideSuccess() {
        Ride ride = new Ride(testPassenger, testDriver);
        ride.setStatus(RideStatus.REQUESTED); // Set status to REQUESTED for testing startRide conditions
        testPassenger.setStatus(PassengerStatus.REQUESTED);
        testDriver.setStatus(DriverStatus.AVAILABLE);

        outContent.reset(); // Clear any constructor output
        boolean started = ride.startRide(); // Capture return value

        assertTrue(started, "startRide should return true when successful."); // Assert on return value
        assertTrue(ride.isRideActive(), "Ride should be active after startRide.");
        assertEquals(RideStatus.IN_PROGRESS, ride.getStatus(), "Ride status should be IN_PROGRESS after startRide.");
        assertEquals(PassengerStatus.INRIDE, testPassenger.getStatus(), "Passenger status should be INRIDE after startRide.");
        assertEquals(DriverStatus.DRIVING, testDriver.getStatus(), "Driver status should be DRIVING after startRide.");
        // OPTIONAL: You can keep console output checks for debugging, but they shouldn't be the primary assertion.
        // assertTrue(outContent.toString().contains("Starting ride for Alice with driver Bob"), "Console output should confirm ride start.");
    }

    @Test
    @DisplayName("Should not start a ride if status is COMPLETED or CANCELLED")
    void testStartRideFailureInvalidStatus() {
        Ride ride = new Ride(testPassenger, testDriver);
        ride.setStatus(RideStatus.COMPLETED); // Set an invalid status

        outContent.reset();
        boolean started = ride.startRide(); // Capture return value

        assertFalse(started, "startRide should return false for invalid status."); // Assert on return value
        assertFalse(ride.isRideActive(), "Ride should not become active.");
        assertEquals(RideStatus.COMPLETED, ride.getStatus(), "Ride status should remain COMPLETED.");
        // OPTIONAL:
        // assertTrue(outContent.toString().contains("Cannot start ride. Current status: COMPLETED"), "Console output should indicate failure.");
    }

    @Test
    @DisplayName("Should end a ride successfully when in progress")
    void testEndRideSuccess() throws InterruptedException {
        Ride ride = new Ride(testPassenger, testDriver);
        // First, set up the ride to be IN_PROGRESS
        ride.setStatus(RideStatus.ACCEPTED); // Must be ACCEPTED or REQUESTED to start
        testPassenger.setStatus(PassengerStatus.REQUESTED);
        testDriver.setStatus(DriverStatus.AVAILABLE);
        ride.startRide(); // Start the ride first

        // Simulate some ride duration
        Thread.sleep(100);

        outContent.reset();
        boolean ended = ride.endRide(); // Capture return value

        assertTrue(ended, "endRide should return true when successful."); // Assert on return value
        assertFalse(ride.isRideActive(), "Ride should not be active after endRide.");
        assertEquals(RideStatus.COMPLETED, ride.getStatus(), "Ride status should be COMPLETED after endRide.");
        // OPTIONAL:
        //assertTrue(outContent.toString().contains("Fare for Alice is: $"), "Fare calculation message should be present.");
    }

    @Test
    @DisplayName("Should not end a ride if it's not in progress")
    void testEndRideFailureNotActive() {
        Ride ride = new Ride(testPassenger, testDriver);
        ride.setStatus(RideStatus.REQUESTED); // Ride is requested, but not yet started/active

        outContent.reset();
        boolean ended = ride.endRide(); // Capture return value

        assertFalse(ended, "endRide should return false if not in progress."); // Assert on return value
        assertFalse(ride.isRideActive(), "Ride should remain inactive.");
        assertEquals(RideStatus.REQUESTED, ride.getStatus(), "Ride status should remain REQUESTED.");
        // OPTIONAL:
        // assertTrue(outContent.toString().contains("Cannot end ride. Current status: REQUESTED"), "Console output should indicate failure.");
    }

    @Test
    @DisplayName("Should cancel a ride successfully when in REQUESTED status by Passenger")
    void testCancelRideSuccessPassenger() {
        Ride ride = new Ride(testPassenger, testDriver);
        ride.setStatus(RideStatus.REQUESTED);

        outContent.reset();
        boolean cancelled = ride.cancelRide(CancelRole.PASSENGER);

        assertTrue(cancelled, "cancelRide should return true on successful cancellation.");
        assertEquals(RideStatus.CANCELLED, ride.getStatus(), "Ride status should be CANCELLED.");
        assertEquals(CancelRole.PASSENGER, ride.getCancelRole(), "Cancel role should be PASSENGER.");
        // Check passenger/driver status only if ride was ACCEPTED before cancellation.
        // For REQUESTED, passenger's status might not have changed from initial.
        // Need to ensure the default status of passenger/driver for a new ride is not INRIDE/DRIVING.
        assertEquals(PassengerStatus.NOTREQUESTED, testPassenger.getStatus(), "Passenger status should revert to NOTREQUESTED after cancellation from REQUESTED.");
        assertEquals(DriverStatus.AVAILABLE, testDriver.getStatus(), "Driver status should remain AVAILABLE.");
    }

    @Test
    @DisplayName("Should cancel a ride successfully when in ACCEPTED status by Driver")
    void testCancelRideSuccessDriver() {
        Ride ride = new Ride(testPassenger, testDriver);
        ride.setStatus(RideStatus.ACCEPTED);
        testPassenger.setStatus(PassengerStatus.INRIDE); // Simulate passenger being in ride state
        testDriver.setStatus(DriverStatus.DRIVING); // Simulate driver driving

        outContent.reset();
        boolean cancelled = ride.cancelRide(CancelRole.DRIVER);

        assertTrue(cancelled, "cancelRide should return true on successful cancellation.");
        assertEquals(RideStatus.CANCELLED, ride.getStatus(), "Ride status should be CANCELLED.");
        assertEquals(CancelRole.DRIVER, ride.getCancelRole(), "Cancel role should be DRIVER.");
        assertEquals(PassengerStatus.NOTREQUESTED, testPassenger.getStatus(), "Passenger status should revert to NOTREQUESTED after cancellation from ACCEPTED.");
        assertEquals(DriverStatus.AVAILABLE, testDriver.getStatus(), "Driver status should revert to AVAILABLE after cancellation from ACCEPTED.");
    }

    @Test
    @DisplayName("Should not cancel a ride if status is IN_PROGRESS or COMPLETED")
    void testCancelRideFailureInvalidStatus() {
        Ride ride = new Ride(testPassenger, testDriver);
        ride.setStatus(RideStatus.IN_PROGRESS); // Set an invalid status for cancellation

        outContent.reset();
        boolean cancelled = ride.cancelRide(CancelRole.PASSENGER);

        assertFalse(cancelled, "cancelRide should return false for invalid status.");
        assertEquals(RideStatus.IN_PROGRESS, ride.getStatus(), "Ride status should remain IN_PROGRESS.");
    }
}