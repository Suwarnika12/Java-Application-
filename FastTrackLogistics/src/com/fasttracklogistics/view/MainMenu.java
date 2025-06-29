package com.fasttracklogistics.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MainMenu extends JFrame {
    public MainMenu() {
        setTitle("FastTrack Logistics");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(8, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JButton manageShipmentsBtn = new JButton("Manage Shipments");
        JButton managePersonnelBtn = new JButton("Manage Delivery Personnel");
        JButton scheduleDeliveriesBtn = new JButton("Schedule Deliveries");
        JButton trackShipmentBtn = new JButton("Track Shipment Progress");
        JButton assignDriversBtn = new JButton("Assign Drivers to Shipments");
        JButton monthlyReportsBtn = new JButton("Generate Monthly Reports");
        JButton customerNotificationsBtn = new JButton("Send Customer Notifications");
        JButton personnelNotificationsBtn = new JButton("Send Personnel Notifications");

        manageShipmentsBtn.addActionListener(e -> new ManageShipmentsForm().setVisible(true));
        managePersonnelBtn.addActionListener(e -> new ManageDeliveryPersonnelForm().setVisible(true));
        scheduleDeliveriesBtn.addActionListener(e -> new ScheduleDeliveriesForm().setVisible(true));
        trackShipmentBtn.addActionListener(e -> new TrackShipmentForm().setVisible(true));
        assignDriversBtn.addActionListener(e -> new AssignDriversForm().setVisible(true));
        monthlyReportsBtn.addActionListener(e -> new MonthlyReportsForm().setVisible(true));
        customerNotificationsBtn.addActionListener(e -> new CustomerNotificationForm().setVisible(true));
        personnelNotificationsBtn.addActionListener(e -> new PersonnelNotificationForm().setVisible(true));

        panel.add(manageShipmentsBtn);
        panel.add(managePersonnelBtn);
        panel.add(scheduleDeliveriesBtn);
        panel.add(trackShipmentBtn);
        panel.add(assignDriversBtn);
        panel.add(monthlyReportsBtn);
        panel.add(customerNotificationsBtn);
        panel.add(personnelNotificationsBtn);

        add(panel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainMenu().setVisible(true));
    }
}