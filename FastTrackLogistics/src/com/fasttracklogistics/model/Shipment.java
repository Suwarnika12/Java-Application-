package com.fasttracklogistics.model;

import java.time.LocalDateTime;

public class Shipment {
    private int shipmentId;
    private String senderName;
    private String receiverName;
    private String packageContents;
    private String status;
    private String location;
    private LocalDateTime estimatedDelivery;
    private int driverId;

    // Constructor
    public Shipment(int shipmentId, String senderName, String receiverName, String packageContents,
                    String status, String location, LocalDateTime estimatedDelivery, int driverId) {
        this.shipmentId = shipmentId;
        this.senderName = senderName;
        this.receiverName = receiverName;
        this.packageContents = packageContents;
        this.status = status;
        this.location = location;
        this.estimatedDelivery = estimatedDelivery;
        this.driverId = driverId;
    }

    // Getters and Setters
    public int getShipmentId() { return shipmentId; }
    public void setShipmentId(int shipmentId) { this.shipmentId = shipmentId; }
    public String getSenderName() { return senderName; }
    public void setSenderName(String senderName) { this.senderName = senderName; }
    public String getReceiverName() { return receiverName; }
    public void setReceiverName(String receiverName) { this.receiverName = receiverName; }
    public String getPackageContents() { return packageContents; }
    public void setPackageContents(String packageContents) { this.packageContents = packageContents; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public LocalDateTime getEstimatedDelivery() { return estimatedDelivery; }
    public void setEstimatedDelivery(LocalDateTime estimatedDelivery) { this.estimatedDelivery = estimatedDelivery; }
    public int getDriverId() { return driverId; }
    public void setDriverId(int driverId) { this.driverId = driverId; }
}