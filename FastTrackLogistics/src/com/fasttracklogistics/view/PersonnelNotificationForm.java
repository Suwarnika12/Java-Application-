package com.fasttracklogistics.view;

import com.fasttracklogistics.db.DatabaseConnection;
import com.fasttracklogistics.util.EmailSender;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import javax.mail.MessagingException;

public class PersonnelNotificationForm extends JFrame {
    private final JComboBox<Integer> driverIdCombo;
    private final JTextArea messageArea;

    public PersonnelNotificationForm() {
        setTitle("Send Personnel Notifications");
        setSize(700, 200);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Form Panel
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        formPanel.add(new JLabel("Driver ID:"));
        driverIdCombo = new JComboBox<>();
        formPanel.add(driverIdCombo);

        messageArea = new JTextArea(5, 20);
        JScrollPane scrollPane = new JScrollPane(messageArea);
        formPanel.add(new JLabel("Message:"));
        formPanel.add(scrollPane);

        JButton sendButton = new JButton("Send Notification");
        formPanel.add(new JLabel());
        formPanel.add(sendButton);

        add(formPanel, BorderLayout.CENTER);

        // Load drivers
        loadDrivers();

        // Button Action
        sendButton.addActionListener(e -> sendNotification());
    }

    private void loadDrivers() {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT driver_id FROM delivery_personnel")) {
            while (rs.next()) {
                driverIdCombo.addItem(rs.getInt("driver_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading drivers: " + e.getMessage());
        }
    }

    private void sendNotification() {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(
                     "SELECT driver_name, email FROM delivery_personnel WHERE driver_id = ?")) {
            pstmt.setInt(1, (Integer) driverIdCombo.getSelectedItem());
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String driverName = rs.getString("driver_name");
                String email = rs.getString("email");
                String subject = "New Assignment Notification: Driver ID " + driverIdCombo.getSelectedItem();
                String body = "Dear " + driverName + ",\n\n" +
                        "You have a new assignment.\n\n" +
                        messageArea.getText() + "\n\nBest regards,\nFastTrack Logistics";
                EmailSender.sendEmail(email, subject, body);
                JOptionPane.showMessageDialog(this, "Notification sent successfully!");
            }
        } catch (SQLException | MessagingException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error sending notification: " + e.getMessage());
        }
    }
}