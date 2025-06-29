package com.fasttracklogistics.view;

import com.fasttracklogistics.db.DatabaseConnection;
import com.fasttracklogistics.model.Shipment;
import javax.swing.table.DefaultTableModel;
import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ManageShipmentsForm extends JFrame {
    private final JTextField shipmentIdField, senderNameField, receiverNameField, contentsField, statusField, locationField, estimatedDeliveryField;
    private final JTable shipmentTable;
    private final DefaultTableModel tableModel;

    public ManageShipmentsForm() {
        setTitle("Manage Shipments");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Form Panel
        JPanel formPanel = new JPanel(new GridLayout(10, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        formPanel.add(new JLabel("Shipment ID:"));
        shipmentIdField = new JTextField();
        shipmentIdField.setEditable(false);
        formPanel.add(shipmentIdField);

        formPanel.add(new JLabel("Sender Name:"));
        senderNameField = new JTextField();
        formPanel.add(senderNameField);

        formPanel.add(new JLabel("Receiver Name:"));
        receiverNameField = new JTextField();
        formPanel.add(receiverNameField);

        formPanel.add(new JLabel("Package Contents:"));
        contentsField = new JTextField();
        formPanel.add(contentsField);

        formPanel.add(new JLabel("Status:"));
        statusField = new JTextField();
        formPanel.add(statusField);

        formPanel.add(new JLabel("Location:"));
        locationField = new JTextField();
        formPanel.add(locationField);

        formPanel.add(new JLabel("Estimated Delivery (yyyy-MM-dd HH:mm):"));
        estimatedDeliveryField = new JTextField();
        formPanel.add(estimatedDeliveryField);

        JButton addButton = new JButton("Add");
        JButton updateButton = new JButton("Update");
        JButton removeButton = new JButton("Remove");
        formPanel.add(addButton);
        formPanel.add(updateButton);
        formPanel.add(removeButton);

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
        addButton.addActionListener(e -> addShipment());
        updateButton.addActionListener(e -> updateShipment());
        removeButton.addActionListener(e -> removeShipment());

        shipmentTable.getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = shipmentTable.getSelectedRow();
            if (selectedRow >= 0) {
                shipmentIdField.setText(tableModel.getValueAt(selectedRow, 0).toString());
                senderNameField.setText(tableModel.getValueAt(selectedRow, 1).toString());
                receiverNameField.setText(tableModel.getValueAt(selectedRow, 2).toString());
                contentsField.setText(tableModel.getValueAt(selectedRow, 3).toString());
                statusField.setText(tableModel.getValueAt(selectedRow, 4).toString());
                locationField.setText(tableModel.getValueAt(selectedRow, 5).toString());
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

    private void addShipment() {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(
                     "INSERT INTO shipment (sender_name, receiver_name, package_contents, status, location, estimated_delivery) VALUES (?, ?, ?, ?, ?, ?)")) {
            pstmt.setString(1, senderNameField.getText());
            pstmt.setString(2, receiverNameField.getText());
            pstmt.setString(3, contentsField.getText());
            pstmt.setString(4, statusField.getText());
            pstmt.setString(5, locationField.getText());
            pstmt.setTimestamp(6, Timestamp.valueOf(LocalDateTime.parse(estimatedDeliveryField.getText(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))));
            pstmt.executeUpdate();
            loadShipments();
            clearFields();
            JOptionPane.showMessageDialog(this, "Shipment added successfully!");
        } catch (SQLException | IllegalArgumentException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error adding shipment: " + e.getMessage());
        }
    }

    private void updateShipment() {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(
                     "UPDATE shipment SET sender_name = ?, receiver_name = ?, package_contents = ?, status = ?, location = ?, estimated_delivery = ? WHERE shipment_id = ?")) {
            pstmt.setString(1, senderNameField.getText());
            pstmt.setString(2, receiverNameField.getText());
            pstmt.setString(3, contentsField.getText());
            pstmt.setString(4, statusField.getText());
            pstmt.setString(5, locationField.getText());
            pstmt.setTimestamp(6, Timestamp.valueOf(LocalDateTime.parse(estimatedDeliveryField.getText(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))));
            pstmt.setInt(7, Integer.parseInt(shipmentIdField.getText()));
            pstmt.executeUpdate();
            loadShipments();
            clearFields();
            JOptionPane.showMessageDialog(this, "Shipment updated successfully!");
        } catch (SQLException | IllegalArgumentException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating shipment: " + e.getMessage());
        }
    }

    private void removeShipment() {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("DELETE FROM shipment WHERE shipment_id = ?")) {
            pstmt.setInt(1, Integer.parseInt(shipmentIdField.getText()));
            pstmt.executeUpdate();
            loadShipments();
            clearFields();
            JOptionPane.showMessageDialog(this, "Shipment removed successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error removing shipment: " + e.getMessage());
        }
    }

    private void clearFields() {
        shipmentIdField.setText("");
        senderNameField.setText("");
        receiverNameField.setText("");
        contentsField.setText("");
        statusField.setText("");
        locationField.setText("");
        estimatedDeliveryField.setText("");
    }
}