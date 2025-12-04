package GUI;

import DAO.ClothingItemDAO;
import Models.ClothingItem;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class StaffViewStockPanel extends JPanel {

    private ClothingItemDAO clothingDAO = new ClothingItemDAO();
    private JTable table;
    private DefaultTableModel tableModel;

    public StaffViewStockPanel(MainFrame mainFrame) {

        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(245, 245, 245));

        // Title
        JLabel title = new JLabel("Available Stock", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(title, BorderLayout.NORTH);

        // Table
        tableModel = new DefaultTableModel(
                new Object[]{"ID", "Color", "Material", "Size", "Category", "Price", "Supplier"},
                0
        );

        table = new JTable(tableModel);
        table.setRowHeight(28);
        table.setEnabled(false); // READ ONLY

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Load stock from DB
        loadStock();

        // Back Button
        JButton btnBack = new JButton("Back");
        btnBack.setBackground(new Color(0, 150, 136));
        btnBack.setForeground(Color.WHITE);
        btnBack.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnBack.setFocusPainted(false);
        btnBack.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnBack.addActionListener(e -> mainFrame.switchPanel("Dashboard"));

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottom.setBackground(new Color(245, 245, 245));
        bottom.add(btnBack);

        add(bottom, BorderLayout.SOUTH);
    }

    public void loadStock() {
        tableModel.setRowCount(0);
        List<ClothingItem> items = clothingDAO.getAll();

        for (ClothingItem item : items) {
            tableModel.addRow(new Object[]{
                    item.getClothId(),
                    item.getColor(),
                    item.getMaterial(),
                    item.getSize(),
                    item.getCategory(),
                    item.getPrice(),
                    item.getSupplierId()
            });
        }
    }
}
