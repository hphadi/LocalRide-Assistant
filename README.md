# LocalRideAssistant 📜

[![Java](https://img.shields.io/badge/Java-21-brightgreen.svg)](https://www.java.com)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.3-green.svg)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-17-blue.svg)](https://www.postgresql.org)
[![Maven](https://img.shields.io/badge/Maven-3.11.0-red.svg)](https://maven.apache.org)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

**LocalRideAssistant** is a scalable ride-hailing platform built with a multi-module **Maven** structure. It leverages **JavaFX** ☕ for the desktop client, **Spring Boot** 🌐 for the backend, and **PostgreSQL** 🗄️ with **PostGIS** for geospatial data management. This project supports thousands of concurrent users (passengers, drivers, admins) with multi-language capabilities (English, extensible to German 🇩🇪) and serves as a foundation for a production-ready application.

---

## 📋 Project Overview

LocalRideAssistant aims to provide an efficient and modern ride-hailing solution. The current implementation includes a console-based simulation, a REST API, and a modular architecture, setting the stage for future web and mobile (Android/iOS) interfaces.

### Key Objectives
- **Scalability**: Supports large-scale user interactions across geographic regions.
- **Geospatial Features**: Utilizes PostGIS for location-based ride matching.
- **Modularity**: Organized into client, server, and database modules for maintainability.
- **Multi-Language Support**: Designed for English with plans for German and other languages.

---

## 🛠️ Current Features

### Console Simulation
The console version simulates core ride-hailing functionalities:
- 👤 **Passenger Management**: Create and manage passengers with statuses (e.g., requested, in-ride, assigned).
- 🚙 **Driver Management**: Manage drivers with statuses (e.g., available, driving, assigned).
- 🔄 **Ride Lifecycle Management**:
    - Requesting rides by passengers.
    - Drivers accepting ride requests.
    - Starting and completing rides.
    - Cancelling rides by passengers, drivers, or the system.
- 🔗 **Intelligent Matching (Basic)**: Randomly assigns available drivers to ride requests.
- 📊 **Status Tracking**: Tracks real-time statuses for passengers, drivers, and rides.
- ⭐ **Basic Rating System**: Placeholder for future comprehensive rating implementation.
- 💻 **Command-Line Interface**: Interactive console for user interaction.

### Multi-Module Architecture
- ☕ **Client**: JavaFX-based desktop frontend with a login interface and role-based dashboards.
- 🌐 **Server**: Spring Boot backend with REST APIs for authentication and ride management.
- 🗄️ **Database**: PostgreSQL with PostGIS for geospatial queries, managed via Flyway migrations.

---

## 📦 Prerequisites

To set up and run LocalRideAssistant, ensure you have:
- ☕ **Java Development Kit (JDK)**: Version 21 or later.
- 🛠️ **Apache Maven**: Included with IDEs like IntelliJ IDEA or installable separately.
- 🗄️ **PostgreSQL 17**: With PostGIS extension for geospatial features.
- 📚 **Git**: For cloning the repository.

---

## 🚀 Getting Started

Follow these steps to set up and run the project locally:

### 1. Clone the Repository
```bash
git clone https://github.com/hphadi/LocalRideAssistant.git
cd LocalRideAssistant