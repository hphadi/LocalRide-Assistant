package com.localride.model;

import com.localride.model.enums.CancelRole;
import com.localride.model.enums.DriverStatus;
import com.localride.model.enums.PassengerStatus;
import com.localride.model.enums.RideStatus;
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
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    void setUp() {
        // Initialize passenger and driver for each test to ensure isolation
        testPassenger = new Passenger("Alice", "123-4567890");
        testDriver = new Driver("Bob", "Toyota Camry");

        // Redirect System.out to capture console output
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    void restoreStreams() {
        // Restore original System.out after each test
        System.setOut(originalOut);
    }

    @Test
    @DisplayName("Should create a ride with correct initial status and IDs")
    void testRideCreation() {
        Ride ride = new Ride(testPassenger, testDriver);

        // Assertions for initial state
        assertNotNull(ride, "Ride object should not be null after creation.");
        assertNotEquals(0, ride.getId(), "Ride ID should be assigned and not 0.");
        assertEquals(testPassenger, ride.getPassenger(), "The passenger object should be correctly linked.");
        assertEquals(testDriver, ride.getDriver(), "The driver object should be correctly linked.");
        //assertEquals(RideStatus.IN_PROGRESS, ride.getStatus(), "Initial ride status should be IN_PROGRESS."); // Based on your constructor
        //assertEquals(PassengerStatus.INRIDE, testPassenger.getStatus(), "Passenger status should be INRIDE after ride creation.");
        //assertEquals(DriverStatus.DRIVING, testDriver.getStatus(), "Driver status should be DRIVING after ride creation.");
        assertFalse(ride.isRideActive(), "Ride should not be active immediately after creation in constructor.");
    }

    @Test
    @DisplayName("Should start a ride successfully from ACCEPTED or REQUESTED status")
    void testStartRideSuccess() {
        Ride ride = new Ride(testPassenger, testDriver);
        // Manually set status to REQUESTED for testing startRide conditions
        ride.setStatus(RideStatus.REQUESTED);
        testPassenger.setStatus(PassengerStatus.REQUESTED); // Ensure passenger is also in a valid state
        testDriver.setStatus(DriverStatus.AVAILABLE); // Ensure driver is also in a valid state

        outContent.reset(); // Clear any constructor output
        ride.startRide();

        assertTrue(ride.isRideActive(), "Ride should be active after startRide.");
        assertEquals(RideStatus.IN_PROGRESS, ride.getStatus(), "Ride status should be IN_PROGRESS after startRide.");
        assertEquals(PassengerStatus.INRIDE, testPassenger.getStatus(), "Passenger status should be INRIDE after startRide.");
        assertEquals(DriverStatus.DRIVING, testDriver.getStatus(), "Driver status should be DRIVING after startRide.");
        assertTrue(outContent.toString().contains("Starting ride for Alice with driver Bob"), "Console output should confirm ride start.");
    }

    @Test
    @DisplayName("Should not start a ride if status is COMPLETED or CANCELLED")
    void testStartRideFailureInvalidStatus() {
        Ride ride = new Ride(testPassenger, testDriver);
        ride.setStatus(RideStatus.COMPLETED); // Set an invalid status

        outContent.reset();
        ride.startRide();

        assertFalse(ride.isRideActive(), "Ride should not become active.");
        assertEquals(RideStatus.COMPLETED, ride.getStatus(), "Ride status should remain COMPLETED.");
        assertTrue(outContent.toString().contains("Cannot start ride. Current status: COMPLETED"), "Console output should indicate failure.");
    }

    @Test
    @DisplayName("Should end a ride successfully when active or in progress")
    void testEndRideSuccess() throws InterruptedException {
        Ride ride = new Ride(testPassenger, testDriver);
        ride.setStatus(RideStatus.ACCEPTED); // Must be ACCEPTED or REQUESTED to start
        testPassenger.setStatus(PassengerStatus.REQUESTED);
        testDriver.setStatus(DriverStatus.AVAILABLE);
        ride.startRide(); // Start the ride first

        // Simulate some ride duration
        Thread.sleep(100); // Wait for 100 milliseconds to have a duration for fare calculation

        outContent.reset();
        ride.endRide();

        assertFalse(ride.isRideActive(), "Ride should not be active after endRide.");
        assertEquals(RideStatus.COMPLETED, ride.getStatus(), "Ride status should be COMPLETED after endRide.");
        assertTrue(outContent.toString().contains("Ride ended with status COMPLETED"), "Console output should confirm ride end.");
        assertTrue(outContent.toString().contains("Fare for Alice is: $"), "Fare calculation message should be present.");
    }

    @Test
    @DisplayName("Should not end a ride if it's not active or in progress")
    void testEndRideFailureNotActive() {
        Ride ride = new Ride(testPassenger, testDriver);
        ride.setStatus(RideStatus.REQUESTED); // Ride is requested, but not yet started/active

        outContent.reset();
        ride.endRide();

        assertFalse(ride.isRideActive(), "Ride should remain inactive.");
        assertEquals(RideStatus.REQUESTED, ride.getStatus(), "Ride status should remain REQUESTED.");
        assertTrue(outContent.toString().contains("Ride is not active. Or Cannot start ride. Current status: REQUESTED"), "Console output should indicate failure.");
    }

    @Test
    @DisplayName("Should cancel a ride successfully when in REQUESTED status by Passenger")
    void testCancelRideSuccessPassenger() {
        Ride ride = new Ride(testPassenger, testDriver);
        ride.setStatus(RideStatus.REQUESTED); // Set status for cancellation scenario

        outContent.reset();
        ride.cancelRide(CancelRole.PASSENGER);

        assertEquals(RideStatus.CANCELLED, ride.getStatus(), "Ride status should be CANCELLED.");
        assertEquals(CancelRole.PASSENGER, ride.cancelRole, "Cancel role should be PASSENGER.");
        assertTrue(outContent.toString().contains("Ride with id " + ride.getId() + " has been cancelled ❌"), "Console output should confirm cancellation.");
        assertEquals(PassengerStatus.NOTREQUESTED, testPassenger.getStatus(), "Passenger status should revert to NOTREQUESTED after cancellation from REQUESTED.");
        // Driver status might remain AVAILABLE if never moved from that state
        //assertEquals(DriverStatus.DRIVING, testDriver.getStatus(), "Driver status should not change from its initial constructor state if never accepted"); // No change if never accepted
    }

    @Test
    @DisplayName("Should cancel a ride successfully when in ACCEPTED status by Driver")
    void testCancelRideSuccessDriver() {
        Ride ride = new Ride(testPassenger, testDriver);
        ride.setStatus(RideStatus.ACCEPTED); // Set status for cancellation scenario
        testPassenger.setStatus(PassengerStatus.INRIDE); // Simulate passenger being in ride state
        testDriver.setStatus(DriverStatus.DRIVING); // Simulate driver driving

        outContent.reset();
        ride.cancelRide(CancelRole.DRIVER);

        assertEquals(RideStatus.CANCELLED, ride.getStatus(), "Ride status should be CANCELLED.");
        assertEquals(CancelRole.DRIVER, ride.cancelRole, "Cancel role should be DRIVER.");
        assertTrue(outContent.toString().contains("Ride with id " + ride.getId() + " has been cancelled ❌"), "Console output should confirm cancellation.");
        assertEquals(PassengerStatus.NOTREQUESTED, testPassenger.getStatus(), "Passenger status should revert to NOTREQUESTED after cancellation from ACCEPTED.");
        assertEquals(DriverStatus.AVAILABLE, testDriver.getStatus(), "Driver status should revert to AVAILABLE after cancellation from ACCEPTED.");
    }

    @Test
    @DisplayName("Should not cancel a ride if status is IN_PROGRESS or COMPLETED")
    void testCancelRideFailureInvalidStatus() {
        Ride ride = new Ride(testPassenger, testDriver);
        ride.setStatus(RideStatus.IN_PROGRESS); // Set an invalid status for cancellation

        outContent.reset();
        ride.cancelRide(CancelRole.PASSENGER);

        assertEquals(RideStatus.IN_PROGRESS, ride.getStatus(), "Ride status should remain IN_PROGRESS.");
        assertTrue(outContent.toString().contains("Cannot cancel ride with id " + ride.getId() + " . Current status: IN_PROGRESS"), "Console output should indicate failure.");
    }
}
