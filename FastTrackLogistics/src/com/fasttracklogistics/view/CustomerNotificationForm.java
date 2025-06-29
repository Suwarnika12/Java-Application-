package com.fasttracklogistics.view;

import com.fasttracklogistics.db.DatabaseConnection;
import com.fasttracklogistics.util.EmailSender;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import javax.mail.MessagingException;

public class CustomerNotificationForm extends JFrame {
    private final JComboBox<Integer> shipmentIdCombo;
    private final JTextArea messageArea;

    public CustomerNotificationForm() {
        setTitle("Send Customer Notifications");
        setSize(700, 200);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Form Panel
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        formPanel.add(new JLabel("Shipment ID:"));
        shipmentIdCombo = new JComboBox<>();
        formPanel.add(shipmentIdCombo);

        messageArea = new JTextArea(5, 20);
        JScrollPane scrollPane = new JScrollPane(messageArea);
        formPanel.add(new JLabel("Message:"));
        formPanel.add(scrollPane);

        JButton sendButton = new JButton("Send Notification");
        formPanel.add(new JLabel());
        formPanel.add(sendButton);

        add(formPanel, BorderLayout.CENTER);

        // Load shipments
        loadShipments();

        // Button Action
        sendButton.addActionListener(e -> sendNotification());
    }

    private void loadShipments() {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT shipment_id FROM shipment")) {
            while (rs.next()) {
                shipmentIdCombo.addItem(rs.getInt("shipment_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading shipments: " + e.getMessage());
        }
    }

    private void sendNotification() {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(
                     "SELECT receiver_name, status, estimated_delivery FROM shipment WHERE shipment_id = ?")) {
            pstmt.setInt(1, (Integer) shipmentIdCombo.getSelectedItem());
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String receiverName = rs.getString("receiver_name");
                String status = rs.getString("status");
                String estimatedDelivery = rs.getTimestamp("estimated_delivery").toString();
                String subject = "Shipment Update: Shipment ID " + shipmentIdCombo.getSelectedItem();
                String body = "Dear " + receiverName + ",\n\n" +
                        "Your shipment (ID: " + shipmentIdCombo.getSelectedItem() + ") is currently " + status +
                        ".\nEstimated Delivery: " + estimatedDelivery + "\n\n" +
                        messageArea.getText() + "\n\nBest regards,\nFastTrack Logistics";
                String customerEmail = getCustomerEmail((Integer) shipmentIdCombo.getSelectedItem());
                EmailSender.sendEmail(customerEmail, subject, body);
                JOptionPane.showMessageDialog(this, "Notification sent successfully!");
            }
        } catch (SQLException | MessagingException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error sending notification: " + e.getMessage());
        }
    }

    private String getCustomerEmail(int shipmentId) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(
                     "SELECT customer_email FROM delivery_schedule WHERE shipment_id = ?")) {
            pstmt.setInt(1, shipmentId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getString("customer_email");
            }
            return "ctrlc.fasttrack25@gmail.com"; // Fallback email
        }
    }
}