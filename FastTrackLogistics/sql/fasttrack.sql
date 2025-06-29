CREATE DATABASE fasttrack;
USE fasttrack;

CREATE TABLE shipment (
    shipment_id INT PRIMARY KEY AUTO_INCREMENT,
    sender_name VARCHAR(100) NOT NULL,
    receiver_name VARCHAR(100) NOT NULL,
    package_contents TEXT NOT NULL,
    status VARCHAR(50) NOT NULL,
    location VARCHAR(100),
    estimated_delivery DATETIME,
    driver_id INT,
    FOREIGN KEY (driver_id) REFERENCES delivery_personnel(driver_id)
);

CREATE TABLE delivery_personnel (
    driver_id INT PRIMARY KEY AUTO_INCREMENT,
    driver_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    route VARCHAR(100),
    availability VARCHAR(50) NOT NULL,
    delivery_history TEXT
);

CREATE TABLE delivery_schedule (
    schedule_id INT PRIMARY KEY AUTO_INCREMENT,
    shipment_id INT,
    customer_email VARCHAR(100) NOT NULL,
    delivery_slot DATETIME NOT NULL,
    status VARCHAR(50) NOT NULL,
    FOREIGN KEY (shipment_id) REFERENCES shipment(shipment_id)
);