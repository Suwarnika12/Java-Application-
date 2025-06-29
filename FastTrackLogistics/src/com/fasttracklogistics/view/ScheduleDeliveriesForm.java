package com.fasttracklogistics.view;

import com.fasttracklogistics.db.DatabaseConnection;

import javax.swing.table.DefaultTableModel;
import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ScheduleDeliveriesForm extends JFrame {
    private JTextField scheduleIdField, shipmentIdField, customerEmailField, deliverySlotField, statusField;
    private JTable scheduleTable;
    private DefaultTableModel tableModel;

    public ScheduleDeliveriesForm() {
        setTitle("Schedule Deliveries");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Form Panel
        JPanel formPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        formPanel.add(new JLabel("Schedule ID:"));
        scheduleIdField = new JTextField();
        scheduleIdField.setEditable(false);
        formPanel.add(scheduleIdField);

        formPanel.add(new JLabel("Shipment ID:"));
        shipmentIdField = new JTextField();
        formPanel.add(shipmentIdField);

        formPanel.add(new JLabel("Customer Email:"));
        customerEmailField = new JTextField();
        formPanel.add(customerEmailField);

        formPanel.add(new JLabel("Delivery Slot (yyyy-MM-dd HH:mm):"));
        deliverySlotField = new JTextField();
        formPanel.add(deliverySlotField);

        formPanel.add(new JLabel("Status:"));
        statusField = new JTextField();
        formPanel.add(statusField);

        JButton addButton = new JButton("Add");
        JButton updateButton = new JButton("Update");
        formPanel.add(addButton);
        formPanel.add(updateButton);

        // Table
        String[] columns = {"Schedule ID", "Shipment ID", "Customer Email", "Delivery Slot", "Status"};
        tableModel = new DefaultTableModel(columns, 0);
        scheduleTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(scheduleTable);
        add(scrollPane, BorderLayout.CENTER);
        add(formPanel, BorderLayout.NORTH);

        // Load data
        loadSchedules();

        // Button Actions
        addButton.addActionListener(e -> addSchedule());
        updateButton.addActionListener(e -> updateSchedule());

        scheduleTable.getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = scheduleTable.getSelectedRow();
            if (selectedRow >= 0) {
                scheduleIdField.setText(tableModel.getValueAt(selectedRow, 0).toString());
                shipmentIdField.setText(tableModel.getValueAt(selectedRow, 1).toString());
                customerEmailField.setText(tableModel.getValueAt(selectedRow, 2).toString());
                deliverySlotField.setText(tableModel.getValueAt(selectedRow, 3).toString());
                statusField.setText(tableModel.getValueAt(selectedRow, 4).toString());
            }
        });
    }

    private void loadSchedules() {
        tableModel.setRowCount(0);
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM delivery_schedule")) {
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                        rs.getInt("schedule_id"),
                        rs.getInt("shipment_id"),
                        rs.getString("customer_email"),
                        rs.getTimestamp("delivery_slot"),
                        rs.getString("status")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading schedules: " + e.getMessage());
        }
    }

    private void addSchedule() {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(
                     "INSERT INTO delivery_schedule (shipment_id, customer_email, delivery_slot, status) VALUES (?, ?, ?, ?)")) {
            pstmt.setInt(1, Integer.parseInt(shipmentIdField.getText()));
            pstmt.setString(2, customerEmailField.getText());
            pstmt.setTimestamp(3, Timestamp.valueOf(LocalDateTime.parse(deliverySlotField.getText(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))));
            pstmt.setString(4, statusField.getText());
            pstmt.executeUpdate();
            loadSchedules();
            clearFields();
            JOptionPane.showMessageDialog(this, "Schedule added successfully!");
        } catch (SQLException | IllegalArgumentException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error adding schedule: " + e.getMessage());
        }
    }

    private void updateSchedule() {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(
                     "UPDATE delivery_schedule SET shipment_id = ?, customer_email = ?, delivery_slot = ?, status = ? WHERE schedule_id = ?")) {
            pstmt.setInt(1, Integer.parseInt(shipmentIdField.getText()));
            pstmt.setString(2, customerEmailField.getText());
            pstmt.setTimestamp(3, Timestamp.valueOf(LocalDateTime.parse(deliverySlotField.getText(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))));
            pstmt.setString(4, statusField.getText());
            pstmt.setInt(5, Integer.parseInt(scheduleIdField.getText()));
            pstmt.executeUpdate();
            loadSchedules();
            clearFields();
            JOptionPane.showMessageDialog(this, "Schedule updated successfully!");
        } catch (SQLException | IllegalArgumentException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating schedule: " + e.getMessage());
        }
    }

    private void clearFields() {
        scheduleIdField.setText("");
        shipmentIdField.setText("");
        customerEmailField.setText("");
        deliverySlotField.setText("");
        statusField.setText("");
    }
}