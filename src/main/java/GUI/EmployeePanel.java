package GUI;

import DAO.EmployeeDAO;
import Models.Employee;
import Models.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class EmployeePanel extends JPanel {

    private EmployeeDAO employeeDAO = new EmployeeDAO();
    private User currentUser;

    private JTextField txtId, txtName, txtPhone, txtSalary, txtPosition, txtSearch;
    private JButton btnAdd, btnUpdate, btnDelete, btnClear, btnRefresh, btnSearch;
    private JTable employeeTable;
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

    public EmployeePanel(User user) {
        this.currentUser = user;

        setLayout(new BorderLayout(10, 10));
        setBackground(panelBackground);
        setBorder(new EmptyBorder(10, 10, 10, 10));

        // --- Left Panel: Form + Buttons ---
        JPanel leftPanel = new JPanel(new BorderLayout(10, 10));
        leftPanel.setBackground(panelBackground);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(formBackground);
        formPanel.setBorder(BorderFactory.createTitledBorder("Employee Details"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.ipady = 10;

        JLabel lblId = new JLabel("Employee ID:"); lblId.setFont(labelFont);
        txtId = new JTextField(); txtId.setFont(inputFont);

        JLabel lblName = new JLabel("Name:"); lblName.setFont(labelFont);
        txtName = new JTextField(); txtName.setFont(inputFont);

        JLabel lblPhone = new JLabel("Phone:"); lblPhone.setFont(labelFont);
        txtPhone = new JTextField(); txtPhone.setFont(inputFont);

        JLabel lblSalary = new JLabel("Salary:"); lblSalary.setFont(labelFont);
        txtSalary = new JTextField(); txtSalary.setFont(inputFont);

        JLabel lblPosition = new JLabel("Position:"); lblPosition.setFont(labelFont);
        txtPosition = new JTextField(); txtPosition.setFont(inputFont);

        gbc.gridx = 0; gbc.gridy = 0; formPanel.add(lblId, gbc);
        gbc.gridx = 1; gbc.gridy = 0; formPanel.add(txtId, gbc);
        gbc.gridx = 0; gbc.gridy = 1; formPanel.add(lblName, gbc);
        gbc.gridx = 1; gbc.gridy = 1; formPanel.add(txtName, gbc);
        gbc.gridx = 0; gbc.gridy = 2; formPanel.add(lblPhone, gbc);
        gbc.gridx = 1; gbc.gridy = 2; formPanel.add(txtPhone, gbc);
        gbc.gridx = 0; gbc.gridy = 3; formPanel.add(lblSalary, gbc);
        gbc.gridx = 1; gbc.gridy = 3; formPanel.add(txtSalary, gbc);
        gbc.gridx = 0; gbc.gridy = 4; formPanel.add(lblPosition, gbc);
        gbc.gridx = 1; gbc.gridy = 4; formPanel.add(txtPosition, gbc);

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
            // Go back to Dashboard
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

        tableModel = new DefaultTableModel(new String[]{"Employee ID", "Name", "Phone", "Salary", "Position"}, 0);
        employeeTable = new JTable(tableModel);
        employeeTable.setFont(inputFont);
        employeeTable.setRowHeight(35);
        employeeTable.setSelectionBackground(tableSelection);
        JScrollPane tableScroll = new JScrollPane(employeeTable);

        rightPanel.add(searchPanel, BorderLayout.NORTH);
        rightPanel.add(tableScroll, BorderLayout.CENTER);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
        splitPane.setDividerLocation(450);
        splitPane.setOneTouchExpandable(true);
        add(splitPane, BorderLayout.CENTER);

        loadEmployees();

        // Row selection
        employeeTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = employeeTable.getSelectedRow();
                if (row >= 0) {
                    txtId.setText(tableModel.getValueAt(row, 0).toString());
                    txtName.setText(tableModel.getValueAt(row, 1).toString());
                    txtPhone.setText(tableModel.getValueAt(row, 2).toString());
                    txtSalary.setText(tableModel.getValueAt(row, 3).toString());
                    txtPosition.setText(tableModel.getValueAt(row, 4).toString());
                    txtId.setEditable(false);
                }
            }
        });

        // Button Actions
        btnAdd.addActionListener(e -> addEmployee());
        btnUpdate.addActionListener(e -> updateEmployee());
        btnDelete.addActionListener(e -> deleteEmployee());
        btnClear.addActionListener(e -> clearForm());
        btnRefresh.addActionListener(e -> loadEmployees());
        btnSearch.addActionListener(e -> searchEmployee());

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
                btnDelete.setEnabled(false);
                break;
            case "staff":
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

    private void loadEmployees() {
        tableModel.setRowCount(0);
        List<Employee> employees = employeeDAO.getAll();
        for (Employee emp : employees) {
            tableModel.addRow(new Object[]{
                    emp.getEmployeeId(),
                    emp.getEmpName(),
                    emp.getTellNo(),
                    emp.getSalary(),
                    emp.getEmpPosition()
            });
        }
    }

    private void addEmployee() {
        if (!btnAdd.isEnabled()) return;
        try {
            String id = txtId.getText().trim();
            String name = txtName.getText().trim();
            String phone = txtPhone.getText().trim();
            String salaryStr = txtSalary.getText().trim();
            String position = txtPosition.getText().trim();

            if (id.isEmpty() || name.isEmpty() || phone.isEmpty() || salaryStr.isEmpty() || position.isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields are required!");
                return;
            }

            double salary = Double.parseDouble(salaryStr);
            Employee emp = new Employee(id, name, phone, salary, position);

            if ("Manager".equalsIgnoreCase(position) && "Manager".equalsIgnoreCase(currentUser.getRole())) {
                JOptionPane.showMessageDialog(this, "Managers cannot add another Manager.");
                return;
            }

            if (employeeDAO.insert(emp, currentUser)) {
                JOptionPane.showMessageDialog(this, "Employee added successfully!");
                loadEmployees();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add employee. Check ID uniqueness or permission.");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Salary must be numeric.");
        }
    }

    private void updateEmployee() {
        if (!btnUpdate.isEnabled()) return;
        try {
            String id = txtId.getText().trim();
            if (id.isEmpty()) { JOptionPane.showMessageDialog(this, "Select an employee to update."); return; }
            String name = txtName.getText().trim();
            String phone = txtPhone.getText().trim();
            double salary = Double.parseDouble(txtSalary.getText().trim());
            String position = txtPosition.getText().trim();

            Employee emp = new Employee(id, name, phone, salary, position);
            if (employeeDAO.update(emp, currentUser)) {
                JOptionPane.showMessageDialog(this, "Employee updated successfully!");
                loadEmployees();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update employee. Check permissions.");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Salary must be numeric.");
        }
    }

    private void deleteEmployee() {
        if (!btnDelete.isEnabled()) return;
        String id = txtId.getText().trim();
        if (id.isEmpty()) { JOptionPane.showMessageDialog(this, "Select an employee to delete."); return; }
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure to delete this employee?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        if (employeeDAO.delete(id, currentUser)) {
            JOptionPane.showMessageDialog(this, "Employee deleted successfully!");
            loadEmployees();
            clearForm();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to delete employee. Check permissions.");
        }
    }

    private void clearForm() {
        txtId.setText(""); txtName.setText(""); txtPhone.setText(""); txtSalary.setText(""); txtPosition.setText("");
        txtId.setEditable(true);
    }

    private void searchEmployee() {
        String keyword = txtSearch.getText().trim();
        tableModel.setRowCount(0);
        if (keyword.isEmpty()) { loadEmployees(); return; }
        List<Employee> employees = employeeDAO.searchByIdOrName(keyword);
        for (Employee emp : employees) {
            tableModel.addRow(new Object[]{
                    emp.getEmployeeId(), emp.getEmpName(), emp.getTellNo(), emp.getSalary(), emp.getEmpPosition()
            });
        }
    }
}
