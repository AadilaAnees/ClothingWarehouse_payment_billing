package GUI;

import DAO.ClothingItemDAO;
import DAO.SupplierDAO;
import Models.ClothingItem;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class ClothingItemPanel extends JPanel {

    private ClothingItemDAO clothingDAO = new ClothingItemDAO();

    private JTextField txtId, txtColor, txtMaterial, txtSize, txtCategory, txtPrice, txtSupplier;
    private JButton btnAdd, btnUpdate, btnDelete, btnClear;
    private JTable table;
    private DefaultTableModel tableModel;
    private JComboBox<String> comboSupplier;


    // Colors & fonts
    private final Color panelBackground = new Color(245, 245, 245);
    private final Color formBackground = Color.WHITE;
    private final Color buttonColor = new Color(0, 150, 136);
    private final Color buttonHover = new Color(0, 137, 123);
    private final Font labelFont = new Font("Segoe UI", Font.BOLD, 14);
    private final Font inputFont = new Font("Segoe UI", Font.PLAIN, 16);

    public ClothingItemPanel() {
        setLayout(new BorderLayout(10, 10));
        setBackground(panelBackground);
        setBorder(new EmptyBorder(10, 10, 10, 10));

        // Top panel: Back button
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBackground(panelBackground);
        JButton btnBack = new JButton("Back");
        btnBack.setBackground(new Color(200, 150, 136));
        btnBack.setForeground(Color.WHITE);
        btnBack.setFont(labelFont);
        btnBack.setFocusPainted(false);
        btnBack.addActionListener(e -> {
            Container topFrame = SwingUtilities.getWindowAncestor(this);
            if (topFrame instanceof MainFrame) {
                ((MainFrame) topFrame).switchPanel("Dashboard");
            }
        });
        topPanel.add(btnBack);
        add(topPanel, BorderLayout.NORTH);

        // Left: Form
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(formBackground);
        formPanel.setBorder(BorderFactory.createTitledBorder("Clothing Item Details"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        txtId = addLabelAndField(formPanel, gbc, "Cloth ID:", 0);
        txtColor = addLabelAndField(formPanel, gbc, "Color:", 1);
        txtMaterial = addLabelAndField(formPanel, gbc, "Material:", 2);
        txtSize = addLabelAndField(formPanel, gbc, "Size:", 3);
        txtCategory = addLabelAndField(formPanel, gbc, "Category:", 4);
        txtPrice = addLabelAndField(formPanel, gbc, "Price:", 5);
        JLabel lblSupplier = new JLabel("Supplier ID:");
        lblSupplier.setFont(labelFont);
        comboSupplier = new JComboBox<>();
        loadSuppliers(); // method to populate combo
        gbc.gridx = 0; gbc.gridy = 6; formPanel.add(lblSupplier, gbc);
        gbc.gridx = 1; gbc.gridy = 6; formPanel.add(comboSupplier, gbc);


        // Buttons
        JPanel buttonPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        buttonPanel.setBackground(formBackground);
        btnAdd = createButton("Add");
        btnUpdate = createButton("Update");
        btnDelete = createButton("Delete");
        btnClear = createButton("Clear");

        buttonPanel.add(btnAdd); buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete); buttonPanel.add(btnClear);
        gbc.gridx = 0; gbc.gridy = 7; gbc.gridwidth = 2; formPanel.add(buttonPanel, gbc);

        // Right: Table
        tableModel = new DefaultTableModel(new String[]{"ID", "Color", "Material", "Size", "Category", "Price", "Supplier"}, 0);
        table = new JTable(tableModel);
        table.setRowHeight(30);
        JScrollPane tableScroll = new JScrollPane(table);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, formPanel, tableScroll);
        splitPane.setDividerLocation(400);
        add(splitPane, BorderLayout.CENTER);

        loadItems();

        // Row click
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                if (row >= 0) {
                    txtId.setText(tableModel.getValueAt(row, 0).toString());
                    txtColor.setText(tableModel.getValueAt(row, 1).toString());
                    txtMaterial.setText(tableModel.getValueAt(row, 2).toString());
                    txtSize.setText(tableModel.getValueAt(row, 3).toString());
                    txtCategory.setText(tableModel.getValueAt(row, 4).toString());
                    txtPrice.setText(tableModel.getValueAt(row, 5).toString());
                    comboSupplier.setSelectedItem(tableModel.getValueAt(row, 6).toString());
                    txtId.setEditable(false);
                }
            }
        });

        // Button actions
        btnAdd.addActionListener(e -> addItem());
        btnUpdate.addActionListener(e -> updateItem());
        btnDelete.addActionListener(e -> deleteItem());
        btnClear.addActionListener(e -> clearForm());
    }

    private JTextField addLabelAndField(JPanel panel, GridBagConstraints gbc, String label, int y) {
        JLabel lbl = new JLabel(label); lbl.setFont(labelFont);
        JTextField field = new JTextField(); field.setFont(inputFont);
        gbc.gridx = 0; gbc.gridy = y; panel.add(lbl, gbc);
        gbc.gridx = 1; gbc.gridy = y; panel.add(field, gbc);
        return field;
    }

    private JButton createButton(String text) {
        JButton btn = new JButton(text);
        btn.setBackground(buttonColor); btn.setForeground(Color.WHITE);
        btn.setFont(labelFont); btn.setFocusPainted(false);
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(buttonHover); }
            public void mouseExited(MouseEvent e) { btn.setBackground(buttonColor); }
        });
        return btn;
    }

    private void loadItems() {
        tableModel.setRowCount(0);
        List<ClothingItem> items = clothingDAO.getAll();
        for (ClothingItem item : items) {
            tableModel.addRow(new Object[]{
                    item.getClothId(), item.getColor(), item.getMaterial(), item.getSize(),
                    item.getCategory(), item.getPrice(), item.getSupplierId()
            });
        }
    }

    private void addItem() {
        try {
            ClothingItem item = new ClothingItem(
                    txtId.getText().trim(),
                    txtColor.getText().trim(),
                    txtMaterial.getText().trim(),
                    txtSize.getText().trim(),
                    txtCategory.getText().trim(),
                    Double.parseDouble(txtPrice.getText().trim()),
                    comboSupplier.getSelectedItem().toString()
            );
            if (clothingDAO.insert(item)) {
                JOptionPane.showMessageDialog(this, "Item added successfully!");
                loadItems(); clearForm();
                MainFrame mainFrame = (MainFrame) SwingUtilities.getWindowAncestor(this);
                if (mainFrame.getSalesPanel() != null) {
                    mainFrame.getSalesPanel().refreshClothingItems(); // updates combo immediately
                }
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add item.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Check inputs!");
        }
    }

    private void updateItem() {
        try {
            ClothingItem item = new ClothingItem(
                    txtId.getText().trim(),
                    txtColor.getText().trim(),
                    txtMaterial.getText().trim(),
                    txtSize.getText().trim(),
                    txtCategory.getText().trim(),
                    Double.parseDouble(txtPrice.getText().trim()),
                    comboSupplier.getSelectedItem().toString()
            );
            if (clothingDAO.update(item)) {
                JOptionPane.showMessageDialog(this, "Item updated!");
                loadItems(); clearForm();
            } else JOptionPane.showMessageDialog(this, "Failed to update.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Check inputs!");
        }
    }

    private void deleteItem() {
        String id = txtId.getText().trim();
        if (id.isEmpty()) return;
        if (clothingDAO.delete(id)) {
            JOptionPane.showMessageDialog(this, "Item deleted!");
            loadItems(); clearForm();
        } else JOptionPane.showMessageDialog(this, "Failed to delete.");
    }

    private void clearForm() {
        txtId.setText(""); txtColor.setText(""); txtMaterial.setText("");
        txtSize.setText(""); txtCategory.setText(""); txtPrice.setText("");
        comboSupplier.setSelectedIndex(-1); // reset combo
        txtId.setEditable(true);
    }

    private void loadSuppliers() {
        comboSupplier.removeAllItems();
        List<Models.Supplier> suppliers = new SupplierDAO().getAll(); // returns Supplier objects
        for (Models.Supplier s : suppliers) {
            comboSupplier.addItem(s.getSupplierId()); // extract ID
        }
    }

}
