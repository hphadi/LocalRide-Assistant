

import com.localride.console.AppConsole;
import com.localride.model.Driver;
import com.localride.model.Passenger;
import com.localride.model.Ride;
import com.localride.model.enums.CancelRole;
import com.localride.service.DriverManager;
import com.localride.service.PassengerManager;
import com.localride.service.RideManager;
import java.util.concurrent.ThreadLocalRandom;

public class Main {
    //Enum version
    public enum Version {
        V0("First run: Just a hello message"),
        V1("Added Passenger and Driver classes"),
        V2("Added constructor to Driver and Passenger"),
        V3("Ride class added and ride starts/ends"),
        V4("Ride class added and ride cancel"),
        V5("Clarify services and manage all models"),
        V6("Make interactive console menu");

        private final String description;

        Version(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }
    /**
     * Generation 0 starting program
     */
    private static void firstGenerationMethod(){
        //0
        System.out.println("Hi, first app is began 🚗");

    }

    /**
     * Generation 01
     * add Passenger class
     * add driver class
     * acceptRide in driver usable
     */
    private static void secondGenerationMethod(){

        Passenger passenger = new Passenger("Ali", "09123456789");
        Driver driver = new Driver("Mahdi", "White Pride");

        passenger.requestRide();
        driver.acceptRide();

    }

    /**
     * Generation 03
     * add Passenger class
     * add driver class
     * acceptRide in driver usable
     * add constructor
     */
    private static void thirdGenerationMethod(){

        Passenger passenger = new Passenger("Ali");
        Driver driver = new Driver("Reza");

        passenger.requestRide();               // passenger requested
        driver.acceptRide(passenger);         // driver accepted
    }

    /**
     * Generation 04
     * add Passenger class
     * add driver class
     * acceptRide in driver usable
     * add multiple constructor
     */
    private static void forthGenerationMethod(){
        Passenger passenger = new Passenger("Ali");
        Driver driver= new Driver("Reza");

        passenger.requestRide();
        driver.acceptRide();

        Ride ride=new Ride(passenger, driver);
        ride.startRide();

        // after a second ride is finished
        ride.endRide();
    }

    /**
     * Generation 05
     * add Passenger class
     * add driver class
     * acceptRide in driver usable
     * add multiple constructor
     */
    private static void fifthGenerationMethod(){
        Passenger passenger = new Passenger("Ali");
        Driver driver= new Driver("Reza");

        passenger.requestRide();
        driver.acceptRide();

        Ride ride=new Ride(passenger, driver);
        ride.startRide();
        // TODO: Replace printStackTrace with proper logging
        try {
            Thread.sleep(3000); // 3 seconds
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ride.cancelRide(CancelRole.SYSTEM);
        ride.endRide();
        System.out.println("Final Ride Status: " + ride.getStatus());
    }

    /**
     * Generation 06
     * add RideManager class
     * add driver class
     * acceptRide in driver usable
     * add multiple constructor
     */
    private static void sixthGenerationMethod(){
        Passenger passenger = new Passenger("Ali");
        Driver driver= new Driver("Reza");
        DriverManager driverManager =  new DriverManager();
        driverManager.addDriver(driver);
        PassengerManager passengerManager =new PassengerManager();
        passengerManager.addPassenger(passenger);

        RideManager rideManager = new RideManager();
        Ride ride = rideManager.createRide(passenger, driver);

        rideManager.acceptRide(ride);
        rideManager.startRide(ride);
        // TODO: Replace printStackTrace with proper logging
        try {
            int seconds = ThreadLocalRandom.current().nextInt(1, 6); // عدد بین 1 تا 5 ثانیه
            Thread.sleep(seconds * 1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        rideManager.endRide(ride);
        System.out.println("Final Ride Status: " + ride.getStatus());
    }

    /**
     * Generation 07
     * add Interactive Console Menu
     */
    private static void seventhGenerationMethod(){
        new AppConsole().run();
    }

    public static void main(String[] args) {

        Version selectedVersion = Version.V6;

        System.out.println("Running version: " + selectedVersion + " - " + selectedVersion.getDescription());

        switch (selectedVersion) {
            case V0 :
                firstGenerationMethod();
                break;
            case V1 :
                secondGenerationMethod();
                break;
            case V2 :
                thirdGenerationMethod();
                break;
            case V3 :
                forthGenerationMethod();
                break;
            case V4 :
                fifthGenerationMethod();
                break;
            case V5 :
                sixthGenerationMethod();
                break;
            case V6:
                seventhGenerationMethod();
                break;
            default :
                throw new IllegalStateException("Unexpected value: " + selectedVersion);
        }
    }
}