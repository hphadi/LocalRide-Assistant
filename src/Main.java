//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    //Enum version
    public enum Version {
        V0("First run: Just a hello message"),
        V1("Added Passenger and Driver classes"),
        V2("Added constructor to Driver and Passenger"),
        V3("Ride class added and ride starts/ends");

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

    public static void main(String[] args) {

        Version selectedVersion = Version.V3;

        System.out.println("Running version: " + selectedVersion + " - " + selectedVersion.getDescription());

        switch (selectedVersion) {
            case V0 -> firstGenerationMethod();
            case V1 -> secondGenerationMethod();
            case V2 -> thirdGenerationMethod();
            case V3 -> forthGenerationMethod();
        }
    }
}