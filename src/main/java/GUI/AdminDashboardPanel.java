package GUI;

import javax.swing.*;
import java.awt.*;

public class AdminDashboardPanel extends JPanel {

    public AdminDashboardPanel(MainFrame mainFrame) {

        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 245));

        // --- Title ---
        JLabel title = new JLabel("Admin Dashboard", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        // --- Button Panel ---
        JPanel buttonPanel = new JPanel(new GridLayout(4, 2, 15, 15)); // changed to 4 rows to add Customer button
        buttonPanel.setBackground(new Color(245, 245, 245));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 200, 30, 200));

        JButton btnManageEmployees = createButton("Manage Employees");
        btnManageEmployees.addActionListener(e -> mainFrame.switchPanel("Employee"));

        JButton btnManageUsers = createButton("Manage Users");

        JButton btnWarehouseStock = createButton("Warehouse Stock");
        btnWarehouseStock.addActionListener(e -> mainFrame.switchPanel("ClothingItem"));

        JButton btnPurchasesSuppliers = createButton("Purchases / Suppliers");
        btnPurchasesSuppliers.addActionListener(e -> mainFrame.switchPanel("Supplier"));

        JButton btnSalesBilling = createButton("Sales & Billing");
        btnSalesBilling.addActionListener(e -> mainFrame.switchPanel("Sales"));

        JButton btnReports = createButton("Reports");

        JButton btnManageCustomers = createButton("Manage Customers"); // NEW
        btnManageCustomers.addActionListener(e -> mainFrame.switchPanel("Customer"));

        // Add buttons to panel
        buttonPanel.add(btnManageEmployees);
        buttonPanel.add(btnManageUsers);
        buttonPanel.add(btnWarehouseStock);
        buttonPanel.add(btnPurchasesSuppliers);
        buttonPanel.add(btnSalesBilling);
        buttonPanel.add(btnReports);
        buttonPanel.add(btnManageCustomers); // NEW

        // --- Back Button ---
        JButton btnBack = createBackButton("Back to Login");
        btnBack.addActionListener(e -> mainFrame.showLoginPanel()); // go back to login
        JPanel backPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        backPanel.setBackground(new Color(245, 245, 245));
        backPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 0, 0));
        backPanel.add(btnBack);

        // --- Add components ---
        add(title, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
        add(backPanel, BorderLayout.SOUTH);
    }

    private JButton createButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btn.setFocusPainted(false);
        btn.setBackground(new Color(0, 150, 136));
        btn.setForeground(Color.WHITE);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(0, 137, 123));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(0, 150, 136));
            }
        });

        return btn;
    }

    private JButton createBackButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.setBackground(new Color(0, 180, 140)); // slightly different than other buttons
        btn.setForeground(Color.WHITE);
        btn.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(0, 160, 125));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(0, 180, 140));
            }
        });

        return btn;
    }
}
