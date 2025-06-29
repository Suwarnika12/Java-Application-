package com.fasttracklogistics.model;

import java.time.LocalDateTime;

public class DeliverySchedule {
    private int scheduleId;
    private int shipmentId;
    private String customerEmail;
    private LocalDateTime deliverySlot;
    private String status;

    // Constructor
    public DeliverySchedule(int scheduleId, int shipmentId, String customerEmail,
                            LocalDateTime deliverySlot, String status) {
        this.scheduleId = scheduleId;
        this.shipmentId = shipmentId;
        this.customerEmail = customerEmail;
        this.deliverySlot = deliverySlot;
        this.status = status;
    }

    // Getters and Setters
    public int getScheduleId() { return scheduleId; }
    public void setScheduleId(int scheduleId) { this.scheduleId = scheduleId; }
    public int getShipmentId() { return shipmentId; }
    public void setShipmentId(int shipmentId) { this.shipmentId = shipmentId; }
    public String getCustomerEmail() { return customerEmail; }
    public void setCustomerEmail(String customerEmail) { this.customerEmail = customerEmail; }
    public LocalDateTime getDeliverySlot() { return deliverySlot; }
    public void setDeliverySlot(LocalDateTime deliverySlot) { this.deliverySlot = deliverySlot; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
