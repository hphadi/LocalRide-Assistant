# LocalRideAssistant

**LocalRideAssistant is a comprehensive, scalable platform envisioned to revolutionize urban mobility through a robust ride-hailing service.** Starting as a **console-based simulation**, this project is the foundational step towards developing a full-fledged, multi-platform application with **web and mobile (Android/iOS)** interfaces, aiming to deliver an intuitive and efficient ride-sharing experience.

---

## Current Features (Console Simulation)

The current version of LocalRideAssistant operates as a console application, simulating the core functionalities of a ride-hailing service. It serves as a robust backend proof-of-concept.

-   **Passenger Management:** Create and manage passengers, including their status (e.g., requested, in-ride, assigned to ride).
-   **Driver Management:** Create and manage drivers, including their status (e.g., available, driving, assigned to ride).
-   **Ride Lifecycle Management:** Simulate the complete ride process:
    -   Requesting a ride by a passenger.
    -   Drivers accepting ride requests.
    -   Starting the ride.
    -   Completing the ride.
    -   Cancelling rides by either party or the system.
-   **Intelligent Matching (Basic):** Randomly assigns available drivers to requested rides, considering basic filtering rules.
-   **Status Tracking:** Maintains distinct and updated statuses for Passengers, Drivers, and Rides throughout their lifecycle.
-   **Basic Rating System:** Placeholder for future comprehensive rating implementation.
-   **Command-Line Interface:** Interactive console application for user interaction.

---

## Prerequisites

To run this console-based project, you will need:

* **Java Development Kit (JDK) 24** or later.
* **Apache Maven** (usually bundled with IDEs like IntelliJ IDEA, or can be installed separately).

---

## Getting Started (Console Version)

Follow these steps to get the console-based project up and running on your local machine:

### 1. Clone the Repository

Open your terminal or Git Bash and run the following command:

```bash
git clone [https://github.com/hphadi/LocalRideAssistant.git](https://github.com/hphadi/LocalRideAssistant.git)
cd LocalRideAssistant