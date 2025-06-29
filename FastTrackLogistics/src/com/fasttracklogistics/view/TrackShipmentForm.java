package com.fasttracklogistics.view;

import com.fasttracklogistics.db.DatabaseConnection;

import javax.swing.table.DefaultTableModel;
import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TrackShipmentForm extends JFrame {
    private JTextField shipmentIdField, locationField, statusField, estimatedDeliveryField;
    private JTable shipmentTable;
    private DefaultTableModel tableModel;

    public TrackShipmentForm() {
        setTitle("Track Shipment Progress");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Form Panel
        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        formPanel.add(new JLabel("Shipment ID:"));
        shipmentIdField = new JTextField();
        shipmentIdField.setEditable(false);
        formPanel.add(shipmentIdField);

        formPanel.add(new JLabel("Location:"));
        locationField = new JTextField();
        formPanel.add(locationField);

        formPanel.add(new JLabel("Status:"));
        statusField = new JTextField();
        formPanel.add(statusField);

        formPanel.add(new JLabel("Estimated Delivery (yyyy-MM-dd HH:mm):"));
        estimatedDeliveryField = new JTextField();
        formPanel.add(estimatedDeliveryField);

        JButton updateButton = new JButton("Update");
        formPanel.add(updateButton);

        // Table
        String[] columns = {"ID", "Sender", "Receiver", "Contents", "Status", "Location", "Estimated Delivery", "Driver ID"};
        tableModel = new DefaultTableModel(columns, 0);
        shipmentTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(shipmentTable);
        add(scrollPane, BorderLayout.CENTER);
        add(formPanel, BorderLayout.NORTH);

        // Load data
        loadShipments();

        // Button Actions
        updateButton.addActionListener(e -> updateShipment());

        shipmentTable.getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = shipmentTable.getSelectedRow();
            if (selectedRow >= 0) {
                shipmentIdField.setText(tableModel.getValueAt(selectedRow, 0).toString());
                locationField.setText(tableModel.getValueAt(selectedRow, 5).toString());
                statusField.setText(tableModel.getValueAt(selectedRow, 4).toString());
                estimatedDeliveryField.setText(tableModel.getValueAt(selectedRow, 6).toString());
            }
        });
    }

    private void loadShipments() {
        tableModel.setRowCount(0);
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM shipment")) {
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                        rs.getInt("shipment_id"),
                        rs.getString("sender_name"),
                        rs.getString("receiver_name"),
                        rs.getString("package_contents"),
                        rs.getString("status"),
                        rs.getString("location"),
                        rs.getTimestamp("estimated_delivery"),
                        rs.getInt("driver_id")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading shipments: " + e.getMessage());
        }
    }

    private void updateShipment() {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(
                     "UPDATE shipment SET location = ?, status = ?, estimated_delivery = ? WHERE shipment_id = ?")) {
            pstmt.setString(1, locationField.getText());
            pstmt.setString(2, statusField.getText());
            pstmt.setTimestamp(3, Timestamp.valueOf(LocalDateTime.parse(estimatedDeliveryField.getText(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))));
            pstmt.setInt(4, Integer.parseInt(shipmentIdField.getText()));
            pstmt.executeUpdate();
            loadShipments();
            clearFields();
            JOptionPane.showMessageDialog(this, "Shipment updated successfully!");
        } catch (SQLException | IllegalArgumentException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating shipment: " + e.getMessage());
        }
    }

    private void clearFields() {
        shipmentIdField.setText("");
        locationField.setText("");
        statusField.setText("");
        estimatedDeliveryField.setText("");
    }
}