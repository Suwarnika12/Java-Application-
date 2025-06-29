package com.fasttracklogistics.view;

import com.fasttracklogistics.db.DatabaseConnection;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class AssignDriversForm extends JFrame {
    private final JComboBox<Integer> shipmentIdCombo;
    private final JComboBox<Integer> driverIdCombo;

    public AssignDriversForm() {
        setTitle("Assign Drivers to Shipments");
        setSize(500, 150);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(3, 2, 10, 10));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        add(new JLabel("Shipment ID:"));
        shipmentIdCombo = new JComboBox<>();
        add(shipmentIdCombo);

        add(new JLabel("Driver ID:"));
        driverIdCombo = new JComboBox<>();
        add(driverIdCombo);

        JButton assignButton = new JButton("Assign Driver");
        add(new JLabel());
        add(assignButton);

        loadComboBoxes();

        assignButton.addActionListener(e -> assignDriver());
    }

    private void loadComboBoxes() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            // Load shipments
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT shipment_id FROM shipment WHERE driver_id IS NULL");
            while (rs.next()) {
                shipmentIdCombo.addItem(rs.getInt("shipment_id"));
            }

            // Load available drivers
            rs = stmt.executeQuery("SELECT driver_id FROM delivery_personnel WHERE availability = 'Available'");
            while (rs.next()) {
                driverIdCombo.addItem(rs.getInt("driver_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading data: " + e.getMessage());
        }
    }

    private void assignDriver() {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("UPDATE shipment SET driver_id = ? WHERE shipment_id = ?")) {
            pstmt.setInt(1, (Integer) driverIdCombo.getSelectedItem());
            pstmt.setInt(2, (Integer) shipmentIdCombo.getSelectedItem());
            pstmt.executeUpdate();

            // Update driver availability
            try (PreparedStatement pstmt2 = conn.prepareStatement("UPDATE delivery_personnel SET availability = 'Assigned' WHERE driver_id = ?")) {
                pstmt2.setInt(1, (Integer) driverIdCombo.getSelectedItem());
                pstmt2.executeUpdate();
            }

            JOptionPane.showMessageDialog(this, "Driver assigned successfully!");
            loadComboBoxes();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error assigning driver: " + e.getMessage());
        }
    }
}
