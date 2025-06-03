public class Ride {
    Passenger passenger;
    Driver driver;
    boolean isRideActive;


    public Ride(Passenger passenger, Driver driver) {
        this.passenger = passenger;
        this.driver = driver ;
        this.isRideActive = false;
    }

    public void startRide(){
        isRideActive = true;
        //System.out.println("Starting ride for "+passenger.name + " with driver "+ driver.name);
        System.out.println("🚗 Starting ride for " + passenger.name + " with driver " + driver.name + " in " + driver.carModel);
    }

    public void endRide() {
        if (isRideActive) {
            isRideActive = false;
            double fare = calculateFare();
            System.out.println("🏁 Ride ended. Fare for " + passenger.name + " is: $" + fare);
        } else {
            System.out.println("❗ Ride is not active.");
        }
    }

    private double calculateFare() {
        // const fair now
        return 15.0;
    }

}
