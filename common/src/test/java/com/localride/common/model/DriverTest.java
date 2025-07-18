package com.localride.common.model;
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

@DisplayName("Driver Class Tests")
public class DriverTest {

    private Driver testDriver;
    private Passenger testPassenger;
    private Ride testRide; // Added for acceptRide(Ride) test
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    void setUp() {
        testDriver = new Driver("Bob", "Toyota Camry");
        testPassenger = new Passenger("Alice", "123-456");
        testRide = new Ride(testPassenger, testDriver); // Initialize a ride
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    void restoreStreams() {
        System.setOut(originalOut);
    }

    @Test
    @DisplayName("Should create a driver with a given name and car model")
    void testDriverCreationWithNameAndCar() {
        assertNotNull(testDriver, "Driver object should not be null.");
        assertEquals("Bob", testDriver.getName(), "Driver name should be 'Bob'.");
        assertEquals("Toyota Camry", testDriver.getCarModel(), "Car model should be 'Toyota Camry'.");
        assertNotEquals(0, testDriver.getId(), "Driver ID should be assigned.");
        assertEquals(DriverStatus.AVAILABLE, testDriver.getStatus(), "Initial driver status should be AVAILABLE.");
        assertEquals(0, testDriver.getRating(), "Initial driver rating should be 0.");
    }

    @Test
    @DisplayName("Should create a driver with only name, defaulting car model to Unknown")
    void testDriverCreationOnlyName() {
        Driver driverWithoutCar = new Driver("Charlie");
        assertNotNull(driverWithoutCar, "Driver object should not be null.");
        assertEquals("Charlie", driverWithoutCar.getName(), "Driver name should be 'Charlie'.");
        assertEquals("Unknown", driverWithoutCar.getCarModel(), "Car model should default to 'Unknown'."); // <-- Expected 'Unknown'
        assertNotEquals(0, driverWithoutCar.getId(), "Driver ID should be assigned.");
        assertEquals(DriverStatus.AVAILABLE, driverWithoutCar.getStatus(), "Initial driver status should be AVAILABLE.");
        assertEquals(0, driverWithoutCar.getRating(), "Initial driver rating should be 0.");
    }

    @Test
    @DisplayName("Should accept a ride successfully when driver is available (general acceptRide)")
    void testAcceptRideGeneralWhenAvailable() {
        // Ensure driver is available
        testDriver.setStatus(DriverStatus.AVAILABLE);

        outContent.reset();
        // Assuming you kept the public boolean acceptRide() method.
        // If you only keep acceptRide(Ride ride), this test needs adjustment.
        // Let's adjust based on the boolean acceptRide(Ride ride) from my suggestion above.
        // First, set ride to REQUESTED state for driver to accept.
        testRide.setStatus(RideStatus.REQUESTED);
        testPassenger.setStatus(PassengerStatus.NOTREQUESTED); // Ensure passenger is not in ride state yet

        boolean accepted = testDriver.acceptRide(testRide); // Call the boolean acceptRide(Ride ride)

        assertTrue(accepted, "Driver should successfully accept the ride.");
        assertEquals(DriverStatus.DRIVING, testDriver.getStatus(), "Driver status should be DRIVING after accepting ride.");
        assertEquals(RideStatus.ACCEPTED, testRide.getStatus(), "Ride status should be ACCEPTED after driver accepts.");
        assertEquals(PassengerStatus.INRIDE, testPassenger.getStatus(), "Passenger status should be INRIDE after driver accepts.");
        // Optional:
        // assertTrue(outContent.toString().contains("accepted the Ride"), "Console output should confirm acceptance.");
    }

    @Test
    @DisplayName("Should not accept a ride if driver is not available")
    void testAcceptRideGeneralWhenNotAvailable() {
        testDriver.setStatus(DriverStatus.DRIVING); // Set driver to an unavailable status
        testRide.setStatus(RideStatus.REQUESTED); // Ride is requested

        outContent.reset();
        boolean accepted = testDriver.acceptRide(testRide); // Call the boolean acceptRide(Ride ride)

        assertFalse(accepted, "Driver should not accept the ride if not available.");
        assertEquals(DriverStatus.DRIVING, testDriver.getStatus(), "Driver status should remain DRIVING.");
        assertEquals(RideStatus.REQUESTED, testRide.getStatus(), "Ride status should remain REQUESTED.");
        // Optional:
        // assertTrue(outContent.toString().contains("is not available now"), "Console output should indicate driver is not available.");
    }

    @Test
    @DisplayName("Should update driver status to AWAY")
    void testSetStatusAway() {
        testDriver.setStatus(DriverStatus.AWAY);
        assertEquals(DriverStatus.AWAY, testDriver.getStatus(), "Driver status should be AWAY.");
    }

    @Test
    @DisplayName("Should update driver rating")
    void testSetRating() {
        testDriver.setRate(5);
        assertEquals(5, testDriver.getRating(), "Driver rating should be 5.");
    }

    @Test
    @DisplayName("Should return correct driver name")
    void testGetName() {
        assertEquals("Bob", testDriver.getName(), "getName should return 'Bob'.");
    }

    @Test
    @DisplayName("Should return correct car model")
    void testGetCarModel() {
        assertEquals("Toyota Camry", testDriver.getCarModel(), "getCarModel should return 'Toyota Camry'.");
    }

    @Test
    @DisplayName("Should change driver name")
    void testSetName() {
        testDriver.setName("Robert");
        assertEquals("Robert", testDriver.getName(), "Driver name should be updated to 'Robert'.");
    }

    @Test
    @DisplayName("Should change driver car model")
    void testSetCarModel() {
        testDriver.setCarModel("Honda Civic");
        assertEquals("Honda Civic", testDriver.getCarModel(), "Car model should be updated to 'Honda Civic'.");
    }
}