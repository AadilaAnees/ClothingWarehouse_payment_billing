package GUI;

import DAO.CustomerDAO;
import Models.Customer;
import Models.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class CustomerPanel extends JPanel {

    private CustomerDAO customerDAO = new CustomerDAO();
    private User currentUser;

    private JTextField txtId, txtName, txtAddress, txtPhone, txtSearch;
    private JButton btnAdd, btnUpdate, btnDelete, btnClear, btnRefresh, btnSearch;
    private JTable customerTable;
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

    public CustomerPanel(User user) {
        this.currentUser = user;

        setLayout(new BorderLayout(10, 10));
        setBackground(panelBackground);
        setBorder(new EmptyBorder(10, 10, 10, 10));

        // --- Left Panel: Form + Buttons ---
        JPanel leftPanel = new JPanel(new BorderLayout(10, 10));
        leftPanel.setBackground(panelBackground);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(formBackground);
        formPanel.setBorder(BorderFactory.createTitledBorder("Customer Details"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.ipady = 10;

        JLabel lblId = new JLabel("Customer ID:"); lblId.setFont(labelFont);
        txtId = new JTextField(); txtId.setFont(inputFont);

        JLabel lblName = new JLabel("Name:"); lblName.setFont(labelFont);
        txtName = new JTextField(); txtName.setFont(inputFont);

        JLabel lblAddress = new JLabel("Address:"); lblAddress.setFont(labelFont);
        txtAddress = new JTextField(); txtAddress.setFont(inputFont);

        JLabel lblPhone = new JLabel("Phone:"); lblPhone.setFont(labelFont);
        txtPhone = new JTextField(); txtPhone.setFont(inputFont);

        gbc.gridx = 0; gbc.gridy = 0; formPanel.add(lblId, gbc);
        gbc.gridx = 1; gbc.gridy = 0; formPanel.add(txtId, gbc);
        gbc.gridx = 0; gbc.gridy = 1; formPanel.add(lblName, gbc);
        gbc.gridx = 1; gbc.gridy = 1; formPanel.add(txtName, gbc);
        gbc.gridx = 0; gbc.gridy = 2; formPanel.add(lblAddress, gbc);
        gbc.gridx = 1; gbc.gridy = 2; formPanel.add(txtAddress, gbc);
        gbc.gridx = 0; gbc.gridy = 3; formPanel.add(lblPhone, gbc);
        gbc.gridx = 1; gbc.gridy = 3; formPanel.add(txtPhone, gbc);

        JPanel buttonPanel = new JPanel(new GridLayout(3, 2, 15, 15));
        buttonPanel.setBackground(formBackground);

        // --- Back Button ---
        JButton btnBack = new JButton("Back");
        btnBack.setBackground(backButtonColor);
        btnBack.setForeground(Color.WHITE);
        btnBack.setFont(labelFont);
        btnBack.setFocusPainted(false);
        btnBack.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnBack.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) { btnBack.setBackground(backButtonhover); }
            @Override
            public void mouseExited(MouseEvent e) { btnBack.setBackground(backButtonColor); }
        });
        btnBack.addActionListener(e -> {
            Container topFrame = SwingUtilities.getWindowAncestor(this);
            if(topFrame instanceof MainFrame) {
                ((MainFrame) topFrame).switchPanel("Dashboard");
            }
        });
        buttonPanel.add(btnBack);

        btnAdd = createButton("Add");
        btnUpdate = createButton("Update");
        btnDelete = createButton("Delete");
        btnClear = createButton("Clear");
        btnRefresh = createButton("Refresh");

        buttonPanel.add(btnAdd); buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete); buttonPanel.add(btnClear);
        buttonPanel.add(btnRefresh);

        leftPanel.add(formPanel, BorderLayout.CENTER);
        leftPanel.add(buttonPanel, BorderLayout.SOUTH);

        // --- Right Panel: Search + Table ---
        JPanel rightPanel = new JPanel(new BorderLayout(10, 10));
        rightPanel.setBackground(panelBackground);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchPanel.setBackground(panelBackground);
        txtSearch = new JTextField(25); txtSearch.setFont(inputFont);
        btnSearch = createButton("Search");
        searchPanel.add(new JLabel("Search:")); searchPanel.add(txtSearch); searchPanel.add(btnSearch);

        tableModel = new DefaultTableModel(new String[]{"Customer ID", "Name", "Address", "Phone"}, 0);
        customerTable = new JTable(tableModel);
        customerTable.setFont(inputFont);
        customerTable.setRowHeight(35);
        customerTable.setSelectionBackground(tableSelection);
        JScrollPane tableScroll = new JScrollPane(customerTable);

        rightPanel.add(searchPanel, BorderLayout.NORTH);
        rightPanel.add(tableScroll, BorderLayout.CENTER);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
        splitPane.setDividerLocation(450);
        splitPane.setOneTouchExpandable(true);
        add(splitPane, BorderLayout.CENTER);

        loadCustomers();

        // Row selection
        customerTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = customerTable.getSelectedRow();
                if (row >= 0) {
                    txtId.setText(tableModel.getValueAt(row, 0).toString());
                    txtName.setText(tableModel.getValueAt(row, 1).toString());
                    txtAddress.setText(tableModel.getValueAt(row, 2).toString());
                    txtPhone.setText(tableModel.getValueAt(row, 3).toString());
                    txtId.setEditable(false);
                }
            }
        });

        // Button Actions
        btnAdd.addActionListener(e -> addCustomer());
        btnUpdate.addActionListener(e -> updateCustomer());
        btnDelete.addActionListener(e -> deleteCustomer());
        btnClear.addActionListener(e -> clearForm());
        btnRefresh.addActionListener(e -> loadCustomers());
        btnSearch.addActionListener(e -> searchCustomer());

        // Apply role permissions
        applyRolePermissions();
    }

    private void applyRolePermissions() {
        if (currentUser == null) return;

        String role = currentUser.getRole().toLowerCase();
        switch (role) {
            case "admin":
                btnAdd.setEnabled(true);
                btnUpdate.setEnabled(true);
                btnDelete.setEnabled(true);
                break;
            case "manager":
                btnAdd.setEnabled(true);
                btnUpdate.setEnabled(true);
                btnDelete.setEnabled(true);
                break;
            case "staff":
                btnAdd.setEnabled(true);    // Staff can add
                btnUpdate.setEnabled(true); // Staff can update
                btnDelete.setEnabled(false); // Staff cannot delete
                break;
            default:
                btnAdd.setEnabled(false);
                btnUpdate.setEnabled(false);
                btnDelete.setEnabled(false);
        }
    }


    private JButton createButton(String text) {
        JButton btn = new JButton(text);
        btn.setBackground(buttonColor); btn.setForeground(Color.WHITE);
        btn.setFont(labelFont); btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(buttonHover); }
            public void mouseExited(MouseEvent e) { btn.setBackground(buttonColor); }
        });
        return btn;
    }

    private void loadCustomers() {
        tableModel.setRowCount(0);
        List<Customer> customers = customerDAO.getAll();
        for (Customer c : customers) {
            tableModel.addRow(c.toValuesArray());
        }
    }

    private void addCustomer() {
        if (!btnAdd.isEnabled()) return;
        String id = txtId.getText().trim();
        String name = txtName.getText().trim();
        String address = txtAddress.getText().trim();
        String phone = txtPhone.getText().trim();

        if (id.isEmpty() || name.isEmpty() || address.isEmpty() || phone.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required!");
            return;
        }

        Customer c = new Customer(id, name, address, phone);
        if (customerDAO.insert(c, currentUser)) {
            JOptionPane.showMessageDialog(this, "Customer added successfully!");
            loadCustomers();
            clearForm();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to add customer. Check ID uniqueness or permission.");
        }
    }

    private void updateCustomer() {
        if (!btnUpdate.isEnabled()) return;
        String id = txtId.getText().trim();
        if (id.isEmpty()) { JOptionPane.showMessageDialog(this, "Select a customer to update."); return; }

        String name = txtName.getText().trim();
        String address = txtAddress.getText().trim();
        String phone = txtPhone.getText().trim();

        Customer c = new Customer(id, name, address, phone);
        if (customerDAO.update(c, currentUser)) {
            JOptionPane.showMessageDialog(this, "Customer updated successfully!");
            loadCustomers();
            clearForm();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to update customer. Check permissions.");
        }
    }

    private void deleteCustomer() {
        if (!btnDelete.isEnabled()) return;
        String id = txtId.getText().trim();
        if (id.isEmpty()) { JOptionPane.showMessageDialog(this, "Select a customer to delete."); return; }

        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure to delete this customer?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        if (customerDAO.delete(id, currentUser)) {
            JOptionPane.showMessageDialog(this, "Customer deleted successfully!");
            loadCustomers();
            clearForm();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to delete customer. Check permissions.");
        }
    }

    private void clearForm() {
        txtId.setText(""); txtName.setText(""); txtAddress.setText(""); txtPhone.setText("");
        txtId.setEditable(true);
    }

    private void searchCustomer() {
        String keyword = txtSearch.getText().trim();
        tableModel.setRowCount(0);
        if (keyword.isEmpty()) { loadCustomers(); return; }
        List<Customer> customers = customerDAO.searchByIdOrName(keyword);
        for (Customer c : customers) {
            tableModel.addRow(c.toValuesArray());
        }
    }
}
