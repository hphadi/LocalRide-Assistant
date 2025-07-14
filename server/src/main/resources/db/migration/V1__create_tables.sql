  CREATE EXTENSION IF NOT EXISTS postgis;

  CREATE TABLE users (
      id SERIAL PRIMARY KEY,
      username VARCHAR(50) UNIQUE NOT NULL,
      password_hash VARCHAR(255) NOT NULL,
      role VARCHAR(20) NOT NULL,
      name VARCHAR(100) NOT NULL,
      email VARCHAR(100),
      phone VARCHAR(20),
      location GEOMETRY(POINT),
      created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
  );

  CREATE TABLE rides (
      id SERIAL PRIMARY KEY,
      passenger_id INT REFERENCES users(id),
      driver_id INT REFERENCES users(id),
      start_location GEOMETRY(POINT),
      end_location GEOMETRY(POINT),
      status VARCHAR(20),
      created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
  );

  CREATE TABLE ride_requests (
      id SERIAL PRIMARY KEY,
      passenger_id INT REFERENCES users(id),
      start_location GEOMETRY(POINT),
      end_location GEOMETRY(POINT),
      status VARCHAR(20),
      created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
  );