public class Passenger {
    String name;
    String phoneNumber;

    public String getName() {
        return name;
    }

    public Passenger(String name, String phoneNumber) {
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    public Passenger(String name) {
        this.name = name;
        this.phoneNumber = "000-000000"; // default value
    }

    public void requestRide() {
        System.out.println(name + " ride requested.");
    }

}
