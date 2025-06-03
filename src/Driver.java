public class Driver {
    String name;
    String carModel;

    public Driver(String name, String carModel) {
        this.name = name;
        this.carModel = carModel;
    }

    public Driver(String name) {
        this.name = name;
        this.carModel = "Unknown"; // default value
    }

    public void acceptRide() {
        System.out.println(name + " with car " + carModel + " accepted the Ride");
    }

    public void acceptRide(Passenger passenger) {
        System.out.println(name + " accepted the ride for " + passenger.getName());
    }
}
