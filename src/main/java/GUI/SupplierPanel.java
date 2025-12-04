package GUI;

import DAO.SupplierDAO;
import Models.Supplier;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class SupplierPanel extends JPanel {

    private SupplierDAO supplierDAO = new SupplierDAO();

    private JTextField txtId, txtName, txtContact, txtSearch;
    private JButton btnAdd, btnUpdate, btnDelete, btnClear, btnRefresh, btnSearch;
    private JTable supplierTable;
    private DefaultTableModel tableModel;

    private final Color panelBackground = new Color(245, 245, 245);
    private final Color formBackground = Color.WHITE;
    private final Color buttonColor = new Color(0, 150, 136);
    private final Color backButtonColor = new Color(200, 150, 136);
    private final Color backButtonhover = new Color(200, 100, 136);
    private final Color buttonHover = new Color(0, 137, 123);
    private final Color tableSelection = new Color(200, 230, 201);
    private final Font labelFont = new Font("Segoe UI", Font.BOLD, 14);
    private final Font inputFont = new Font("Segoe UI", Font.PLAIN, 16);

    public SupplierPanel() {

        setLayout(new BorderLayout(10, 10));
        setBackground(panelBackground);
        setBorder(new EmptyBorder(10, 10, 10, 10));

        // -------- Left Panel --------
        JPanel leftPanel = new JPanel(new BorderLayout(10, 10));
        leftPanel.setBackground(panelBackground);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(formBackground);
        formPanel.setBorder(BorderFactory.createTitledBorder("Supplier Details"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.ipady = 10;

        JLabel lblId = new JLabel("Supplier ID:"); lblId.setFont(labelFont);
        JLabel lblName = new JLabel("Name:"); lblName.setFont(labelFont);
        JLabel lblContact = new JLabel("Contact:"); lblContact.setFont(labelFont);

        txtId = new JTextField(); txtId.setFont(inputFont);
        txtName = new JTextField(); txtName.setFont(inputFont);
        txtContact = new JTextField(); txtContact.setFont(inputFont);

        gbc.gridx = 0; gbc.gridy = 0; formPanel.add(lblId, gbc);
        gbc.gridx = 1; gbc.gridy = 0; formPanel.add(txtId, gbc);

        gbc.gridx = 0; gbc.gridy = 1; formPanel.add(lblName, gbc);
        gbc.gridx = 1; gbc.gridy = 1; formPanel.add(txtName, gbc);

        gbc.gridx = 0; gbc.gridy = 2; formPanel.add(lblContact, gbc);
        gbc.gridx = 1; gbc.gridy = 2; formPanel.add(txtContact, gbc);

        // -------- Button Panel --------
        JPanel buttonPanel = new JPanel(new GridLayout(3, 2, 15, 15));
        buttonPanel.setBackground(formBackground);

        // Back button
        JButton btnBack = new JButton("Back");
        btnBack.setBackground(backButtonColor);
        btnBack.setForeground(Color.WHITE);
        btnBack.setFont(labelFont);
        btnBack.setFocusPainted(false);
        btnBack.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btnBack.setBackground(backButtonhover); }
            public void mouseExited(MouseEvent e) { btnBack.setBackground(backButtonColor); }
        });

        btnBack.addActionListener(e -> {
            Container topFrame = SwingUtilities.getWindowAncestor(this);
            if (topFrame instanceof MainFrame) {
                ((MainFrame) topFrame).switchPanel("Dashboard");
            }
        });

        buttonPanel.add(btnBack);

        btnAdd = createButton("Add");
        btnUpdate = createButton("Update");
        btnDelete = createButton("Delete");
        btnClear = createButton("Clear");
        btnRefresh = createButton("Refresh");

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnClear);
        buttonPanel.add(btnRefresh);

        leftPanel.add(formPanel, BorderLayout.CENTER);
        leftPanel.add(buttonPanel, BorderLayout.SOUTH);

        // -------- Right Panel: Table + Search --------
        JPanel rightPanel = new JPanel(new BorderLayout(10, 10));
        rightPanel.setBackground(panelBackground);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchPanel.setBackground(panelBackground);

        txtSearch = new JTextField(25);
        txtSearch.setFont(inputFont);
        btnSearch = createButton("Search");

        searchPanel.add(new JLabel("Search:"));
        searchPanel.add(txtSearch);
        searchPanel.add(btnSearch);

        tableModel = new DefaultTableModel(new String[]{"Supplier ID", "Name", "Contact"}, 0);
        supplierTable = new JTable(tableModel);
        supplierTable.setFont(inputFont);
        supplierTable.setRowHeight(35);
        supplierTable.setSelectionBackground(tableSelection);

        JScrollPane tableScroll = new JScrollPane(supplierTable);
        rightPanel.add(searchPanel, BorderLayout.NORTH);
        rightPanel.add(tableScroll, BorderLayout.CENTER);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
        splitPane.setDividerLocation(450);
        splitPane.setOneTouchExpandable(true);

        add(splitPane, BorderLayout.CENTER);

        // Load data initially
        loadSuppliers();

        // -------- Table Row Click --------
        supplierTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = supplierTable.getSelectedRow();
                if (row >= 0) {
                    txtId.setText(tableModel.getValueAt(row, 0).toString());
                    txtName.setText(tableModel.getValueAt(row, 1).toString());
                    txtContact.setText(tableModel.getValueAt(row, 2).toString());
                    txtId.setEditable(false);
                }
            }
        });

        // -------- Button Actions --------
        btnAdd.addActionListener(e -> addSupplier());
        btnUpdate.addActionListener(e -> updateSupplier());
        btnDelete.addActionListener(e -> deleteSupplier());
        btnClear.addActionListener(e -> clearForm());
        btnRefresh.addActionListener(e -> loadSuppliers());
        btnSearch.addActionListener(e -> searchSupplier());
    }

    private JButton createButton(String text) {
        JButton btn = new JButton(text);
        btn.setBackground(buttonColor);
        btn.setForeground(Color.WHITE);
        btn.setFont(labelFont);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(buttonHover); }
            public void mouseExited(MouseEvent e) { btn.setBackground(buttonColor); }
        });
        return btn;
    }

    // -------- CRUD Methods --------

    private void loadSuppliers() {
        tableModel.setRowCount(0);
        List<Supplier> suppliers = supplierDAO.getAll();
        for (Supplier s : suppliers) {
            tableModel.addRow(new Object[]{s.getSupplierId(), s.getName(), s.getContact()});
        }
    }

    private void addSupplier() {
        String id = txtId.getText().trim();
        String name = txtName.getText().trim();
        String contact = txtContact.getText().trim();

        if (id.isEmpty() || name.isEmpty() || contact.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required!");
            return;
        }

        Supplier sup = new Supplier(id, name, contact);

        if (supplierDAO.insert(sup)) {
            JOptionPane.showMessageDialog(this, "Supplier added successfully!");
            loadSuppliers();
            clearForm();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to add supplier! ID must be unique.");
        }
    }

    private void updateSupplier() {
        String id = txtId.getText().trim();
        if (id.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Select a supplier to update.");
            return;
        }

        Supplier sup = new Supplier(
                id,
                txtName.getText().trim(),
                txtContact.getText().trim()
        );

        if (supplierDAO.update(sup)) {
            JOptionPane.showMessageDialog(this, "Supplier updated successfully!");
            loadSuppliers();
            clearForm();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to update supplier.");
        }
    }

    private void deleteSupplier() {
        String id = txtId.getText().trim();
        if (id.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Select a supplier to delete.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Delete this supplier?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        if (supplierDAO.delete(id)) {
            JOptionPane.showMessageDialog(this, "Supplier deleted successfully!");
            loadSuppliers();
            clearForm();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to delete supplier.");
        }
    }

    private void clearForm() {
        txtId.setText("");
        txtName.setText("");
        txtContact.setText("");
        txtId.setEditable(true);
    }

    private void searchSupplier() {
        String keyword = txtSearch.getText().trim();
        tableModel.setRowCount(0);

        if (keyword.isEmpty()) {
            loadSuppliers();
            return;
        }

        List<Supplier> suppliers = supplierDAO.search(keyword);
        for (Supplier s : suppliers) {
            tableModel.addRow(new Object[]{s.getSupplierId(), s.getName(), s.getContact()});
        }
    }
}
