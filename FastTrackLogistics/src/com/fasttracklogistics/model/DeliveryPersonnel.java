package com.fasttracklogistics.model;

public class DeliveryPersonnel {
    private int driverId;
    private String driverName;
    private String email;
    private String route;
    private String availability;
    private String deliveryHistory;

    // Constructor
    public DeliveryPersonnel(int driverId, String driverName, String email, String route,
                             String availability, String deliveryHistory) {
        this.driverId = driverId;
        this.driverName = driverName;
        this.email = email;
        this.route = route;
        this.availability = availability;
        this.deliveryHistory = deliveryHistory;
    }

    // Getters and Setters
    public int getDriverId() { return driverId; }
    public void setDriverId(int driverId) { this.driverId = driverId; }
    public String getDriverName() { return driverName; }
    public void setDriverName(String driverName) { this.driverName = driverName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getRoute() { return route; }
    public void setRoute(String route) { this.route = route; }
    public String getAvailability() { return availability; }
    public void setAvailability(String availability) { this.availability = availability; }
    public String getDeliveryHistory() { return deliveryHistory; }
    public void setDeliveryHistory(String deliveryHistory) { this.deliveryHistory = deliveryHistory; }
}