package com.fasttracklogistics.view;

import com.fasttracklogistics.db.DatabaseConnection;

import javax.swing.table.DefaultTableModel;
import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class MonthlyReportsForm extends JFrame {
    private JTextField monthField;
    private JTable reportTable;
    private DefaultTableModel tableModel;

    public MonthlyReportsForm() {
        setTitle("Generate Monthly Reports");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Form Panel
        JPanel formPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        formPanel.add(new JLabel("Month (yyyy-MM):"));
        monthField = new JTextField();
        formPanel.add(monthField);

        JButton generateButton = new JButton("Generate Report");
        formPanel.add(generateButton);

        // Table
        String[] columns = {"Shipment ID", "Sender", "Receiver", "Status", "Delivery Date"};
        tableModel = new DefaultTableModel(columns, 0);
        reportTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(reportTable);
        add(scrollPane, BorderLayout.CENTER);
        add(formPanel, BorderLayout.NORTH);

        // Button Action
        generateButton.addActionListener(e -> generateReport());
    }

    private void generateReport() {
        tableModel.setRowCount(0);
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(
                     "SELECT * FROM shipment WHERE DATE_FORMAT(estimated_delivery, '%Y-%m') = ?")) {
            pstmt.setString(1, monthField.getText());
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                        rs.getInt("shipment_id"),
                        rs.getString("sender_name"),
                        rs.getString("receiver_name"),
                        rs.getString("status"),
                        rs.getTimestamp("estimated_delivery")
                });
            }
            JOptionPane.showMessageDialog(this, "Report generated successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error generating report: " + e.getMessage());
        }
    }
}