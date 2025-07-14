package com.localride.model;

import com.localride.model.enums.DriverStatus;
import com.localride.model.enums.RideStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Driver Class Tests")
public class DriverTest {

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
    @DisplayName("Should create driver with name and car model")
    void testDriverCreationWithCarModel() {
        Driver driver = new Driver("Grace", "Ford Focus");
        assertNotNull(driver);
        assertEquals("Grace", driver.getName());
        assertEquals("Ford Focus", driver.getCarModel());
        assertEquals(DriverStatus.AVAILABLE, driver.getStatus());
        assertEquals(0, driver.getRating());
        assertNotEquals(0, driver.getId());
    }

    @Test
    @DisplayName("Should create driver with only name and default car model")
    void testDriverCreationOnlyName() {
        Driver driver = new Driver("Harry");
        assertNotNull(driver);
        assertEquals("Harry", driver.getName());
        assertEquals("Unknown", driver.getCarModel()); // Default car model
        assertEquals(DriverStatus.AVAILABLE, driver.getStatus());
        assertEquals(0, driver.getRating());
        assertNotEquals(0, driver.getId());
    }

    @Test
    @DisplayName("Should accept a ride (general) when available")
    void testAcceptRideGeneralWhenAvailable() {
        Driver driver = new Driver("Ivy");
        driver.setStatus(DriverStatus.AVAILABLE);
        outContent.reset();

        driver.acceptRide();
        assertTrue(outContent.toString().contains("Ivy with car Unknown and id: " + driver.getId() + " accepted the Ride"), "Console output should confirm acceptance.");
    }

    @Test
    @DisplayName("Should not accept a ride (general) when not available")
    void testAcceptRideGeneralWhenNotAvailable() {
        Driver driver = new Driver("Jack");
        driver.setStatus(DriverStatus.DRIVING); // Not available
        outContent.reset();

        driver.acceptRide();
        assertTrue(outContent.toString().contains("Driver Jack with id : " + driver.getId() + " is not available now;"), "Console output should indicate unavailability.");
    }

    @Test
    @DisplayName("Should accept a ride for a specific passenger when available")
    void testAcceptRideSpecificPassengerWhenAvailable() {
        Driver driver = new Driver("Karen");
        driver.setStatus(DriverStatus.AVAILABLE);
        Passenger passenger = new Passenger("Liam");
        outContent.reset();

        driver.acceptRide(passenger);
        assertTrue(outContent.toString().contains("Karen accepted the ride for Liam"), "Console output should confirm acceptance for passenger.");
    }

    @Test
    @DisplayName("Should accept a Ride object and update its status to ACCEPTED")
    void testAcceptRideObject() {
        Driver driver = new Driver("Mike", "Tesla");
        Passenger passenger = new Passenger("Nora");
        Ride ride = new Ride(passenger, driver);
        ride.setStatus(RideStatus.REQUESTED); // Simulate a requested ride

        driver.setStatus(DriverStatus.AVAILABLE); // Driver must be available to accept

        outContent.reset();
        driver.acceptRide(ride);

        assertEquals(RideStatus.ACCEPTED, ride.getStatus(), "Ride status should be ACCEPTED after driver accepts.");
        assertTrue(outContent.toString().contains("Mike accepted the ride for Nora"), "Console output should confirm driver acceptance.");
    }

    @Test
    @DisplayName("Should not accept a Ride object if ride status is not REQUESTED")
    void testAcceptRideObjectInvalidRideStatus() {
        Driver driver = new Driver("Olivia");
        Passenger passenger = new Passenger("Peter");
        Ride ride = new Ride(passenger, driver);
        ride.setStatus(RideStatus.IN_PROGRESS); // Ride already in progress

        outContent.reset();
        driver.acceptRide(ride);

        assertEquals(RideStatus.IN_PROGRESS, ride.getStatus(), "Ride status should remain IN_PROGRESS.");
        assertTrue(outContent.toString().contains("Cannot accept ride. Current status: IN_PROGRESS"), "Console output should indicate invalid ride status.");
    }

    @Test
    @DisplayName("Should update driver status using setStatus")
    void testSetStatus() {
        Driver driver = new Driver("Quinn");
        driver.setStatus(DriverStatus.DRIVING);
        assertEquals(DriverStatus.DRIVING, driver.getStatus());
        driver.setStatus(DriverStatus.AVAILABLE);
        assertEquals(DriverStatus.AVAILABLE, driver.getStatus());
    }

    @Test
    @DisplayName("Should print cancellation message when cancelRide is called")
    void testCancelRide() {
        Driver driver = new Driver("Ryan");
        outContent.reset();
        driver.cancelRide();
        assertTrue(outContent.toString().contains("Ryan ride Cancel."), "Console output should confirm driver cancellation.");
        // Note: Similar to Passenger, this method only prints and doesn't change internal status.
    }
}