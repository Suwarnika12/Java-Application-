package com.fasttracklogistics.view;

import com.fasttracklogistics.db.DatabaseConnection;

import javax.swing.table.DefaultTableModel;
import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class ManageDeliveryPersonnelForm extends JFrame {
    private final JTextField driverIdField, driverNameField, emailField, routeField, availabilityField, historyField;
    private final JTable personnelTable;
    private final DefaultTableModel tableModel;

    public ManageDeliveryPersonnelForm() {
        setTitle("Manage Delivery Personnel");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Form Panel
        JPanel formPanel = new JPanel(new GridLayout(10, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        formPanel.add(new JLabel("Driver ID:"));
        driverIdField = new JTextField();
        driverIdField.setEditable(false);
        formPanel.add(driverIdField);

        formPanel.add(new JLabel("Driver Name:"));
        driverNameField = new JTextField();
        formPanel.add(driverNameField);

        formPanel.add(new JLabel("Email:"));
        emailField = new JTextField();
        formPanel.add(emailField);

        formPanel.add(new JLabel("Route:"));
        routeField = new JTextField();
        formPanel.add(routeField);

        formPanel.add(new JLabel("Availability:"));
        availabilityField = new JTextField();
        formPanel.add(availabilityField);

        formPanel.add(new JLabel("Delivery History:"));
        historyField = new JTextField();
        formPanel.add(historyField);

        JButton addButton = new JButton("Add");
        JButton updateButton = new JButton("Update");
        JButton removeButton = new JButton("Remove");
        formPanel.add(addButton);
        formPanel.add(updateButton);
        formPanel.add(removeButton);

        // Table
        String[] columns = {"ID", "Name", "Email", "Route", "Availability", "History"};
        tableModel = new DefaultTableModel(columns, 0);
        personnelTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(personnelTable);
        add(scrollPane, BorderLayout.CENTER);
        add(formPanel, BorderLayout.NORTH);

        // Load data
        loadPersonnel();

        // Button Actions
        addButton.addActionListener(e -> addPersonnel());
        updateButton.addActionListener(e -> updatePersonnel());
        removeButton.addActionListener(e -> removePersonnel());

        personnelTable.getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = personnelTable.getSelectedRow();
            if (selectedRow >= 0) {
                driverIdField.setText(tableModel.getValueAt(selectedRow, 0).toString());
                driverNameField.setText(tableModel.getValueAt(selectedRow, 1).toString());
                emailField.setText(tableModel.getValueAt(selectedRow, 2).toString());
                routeField.setText(tableModel.getValueAt(selectedRow, 3).toString());
                availabilityField.setText(tableModel.getValueAt(selectedRow, 4).toString());
                historyField.setText(tableModel.getValueAt(selectedRow, 5).toString());
            }
        });
    }

    private void loadPersonnel() {
        tableModel.setRowCount(0);
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM delivery_personnel")) {
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                        rs.getInt("driver_id"),
                        rs.getString("driver_name"),
                        rs.getString("email"),
                        rs.getString("route"),
                        rs.getString("availability"),
                        rs.getString("delivery_history")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading personnel: " + e.getMessage());
        }
    }

    private void addPersonnel() {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(
                     "INSERT INTO delivery_personnel (driver_name, email, route, availability, delivery_history) VALUES (?, ?, ?, ?, ?)")) {
            pstmt.setString(1, driverNameField.getText());
            pstmt.setString(2, emailField.getText());
            pstmt.setString(3, routeField.getText());
            pstmt.setString(4, availabilityField.getText());
            pstmt.setString(5, historyField.getText());
            pstmt.executeUpdate();
            loadPersonnel();
            clearFields();
            JOptionPane.showMessageDialog(this, "Personnel added successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error adding personnel: " + e.getMessage());
        }
    }

    private void updatePersonnel() {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(
                     "UPDATE delivery_personnel SET driver_name = ?, email = ?, route = ?, availability = ?, delivery_history = ? WHERE driver_id = ?")) {
            pstmt.setString(1, driverNameField.getText());
            pstmt.setString(2, emailField.getText());
            pstmt.setString(3, routeField.getText());
            pstmt.setString(4, availabilityField.getText());
            pstmt.setString(5, historyField.getText());
            pstmt.setInt(6, Integer.parseInt(driverIdField.getText()));
            pstmt.executeUpdate();
            loadPersonnel();
            clearFields();
            JOptionPane.showMessageDialog(this, "Personnel updated successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating personnel: " + e.getMessage());
        }
    }

    private void removePersonnel() {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("DELETE FROM delivery_personnel WHERE driver_id = ?")) {
            pstmt.setInt(1, Integer.parseInt(driverIdField.getText()));
            pstmt.executeUpdate();
            loadPersonnel();
            clearFields();
            JOptionPane.showMessageDialog(this, "Personnel removed successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error removing personnel: " + e.getMessage());
        }
    }

    private void clearFields() {
        driverIdField.setText("");
        driverNameField.setText("");
        emailField.setText("");
        routeField.setText("");
        availabilityField.setText("");
        historyField.setText("");
    }
}