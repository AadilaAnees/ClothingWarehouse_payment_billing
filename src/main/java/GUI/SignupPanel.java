package GUI;

import DAO.UserDAO;
import DAO.EmployeeDAO;
import Models.User;
import Models.Employee;

import javax.swing.*;
import java.awt.*;

public class SignupPanel extends JPanel {

    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JPasswordField txtConfirmPassword;
    private JTextField txtEmployeeId;
    private JComboBox<String> cmbRole;
    private JButton btnSignup, btnBack;

    public SignupPanel(MainFrame mainFrame) {

        setLayout(new BorderLayout());

        // --- LEFT PANEL (Form) ---
        JPanel leftPanel = new JPanel(null);
        leftPanel.setBackground(new Color(30, 30, 30));
        leftPanel.setPreferredSize(new Dimension(450, 650)); // taller for confirm password

        JLabel title = new JLabel("Create Account");
        title.setFont(new Font("Segoe UI", Font.BOLD, 32));
        title.setForeground(Color.WHITE);
        title.setBounds(50, 30, 300, 40);
        leftPanel.add(title);

        // Username
        JLabel lblUsername = new JLabel("Username:");
        lblUsername.setForeground(Color.WHITE);
        lblUsername.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblUsername.setBounds(50, 100, 120, 30);
        leftPanel.add(lblUsername);

        txtUsername = new JTextField();
        txtUsername.setBounds(50, 135, 300, 35);
        txtUsername.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        txtUsername.setBackground(new Color(50, 50, 50));
        txtUsername.setForeground(Color.WHITE);
        txtUsername.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        txtUsername.setCaretColor(Color.WHITE);
        txtUsername.setFocusable(true);
        txtUsername.requestFocusInWindow(); // initial focus only
        leftPanel.add(txtUsername);

        // Password
        JLabel lblPassword = new JLabel("Password:");
        lblPassword.setForeground(Color.WHITE);
        lblPassword.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblPassword.setBounds(50, 190, 120, 30);
        leftPanel.add(lblPassword);

        txtPassword = new JPasswordField();
        txtPassword.setBounds(50, 225, 300, 35);
        txtPassword.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        txtPassword.setBackground(new Color(50, 50, 50));
        txtPassword.setForeground(Color.WHITE);
        txtPassword.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        txtPassword.setCaretColor(Color.WHITE);
        txtPassword.setFocusable(true);
        leftPanel.add(txtPassword);

        // Confirm Password
        JLabel lblConfirmPassword = new JLabel("Confirm Password:");
        lblConfirmPassword.setForeground(Color.WHITE);
        lblConfirmPassword.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblConfirmPassword.setBounds(50, 270, 150, 30);
        leftPanel.add(lblConfirmPassword);

        txtConfirmPassword = new JPasswordField();
        txtConfirmPassword.setBounds(50, 305, 300, 35);
        txtConfirmPassword.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        txtConfirmPassword.setBackground(new Color(50, 50, 50));
        txtConfirmPassword.setForeground(Color.WHITE);
        txtConfirmPassword.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        txtConfirmPassword.setCaretColor(Color.WHITE);
        txtConfirmPassword.setFocusable(true);
        leftPanel.add(txtConfirmPassword);

        // Role selection
        JLabel lblRole = new JLabel("Select Role:");
        lblRole.setForeground(Color.WHITE);
        lblRole.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblRole.setBounds(50, 360, 120, 30);
        leftPanel.add(lblRole);

        cmbRole = new JComboBox<>(new String[]{"Admin", "Manager", "Staff"});
        cmbRole.setBounds(50, 395, 300, 35);
        cmbRole.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        leftPanel.add(cmbRole);

        // Employee ID
        JLabel lblEmpId = new JLabel("Employee ID:");
        lblEmpId.setForeground(Color.WHITE);
        lblEmpId.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblEmpId.setBounds(50, 450, 120, 30);
        leftPanel.add(lblEmpId);

        txtEmployeeId = new JTextField();
        txtEmployeeId.setBounds(50, 485, 300, 35);
        txtEmployeeId.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        txtEmployeeId.setBackground(new Color(50, 50, 50));
        txtEmployeeId.setForeground(Color.WHITE);
        txtEmployeeId.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        txtEmployeeId.setCaretColor(Color.WHITE);
        txtEmployeeId.setFocusable(true);
        leftPanel.add(txtEmployeeId);

        // Signup Button
        btnSignup = new JButton("Create Account");
        btnSignup.setBounds(50, 540, 300, 40);
        btnSignup.setFont(new Font("Segoe UI", Font.BOLD, 18));
        btnSignup.setBackground(new Color(0, 120, 215));
        btnSignup.setForeground(Color.WHITE);
        btnSignup.setBorder(null);
        btnSignup.addActionListener(e -> handleSignup());
        leftPanel.add(btnSignup);

        // Back Button
        btnBack = new JButton("Already have an account? Login");
        btnBack.setBounds(50, 590, 300, 35);
        btnBack.setForeground(Color.CYAN);
        btnBack.setBackground(new Color(40, 40, 40));
        btnBack.setBorder(null);
        btnBack.setFocusPainted(false);
        btnBack.addActionListener(e -> mainFrame.showLoginPanel());
        leftPanel.add(btnBack);

        // Wrap leftPanel in JScrollPane (vertical only)
        JScrollPane scrollPane = new JScrollPane(leftPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(null);

        // --- RIGHT PANEL (Image) ---
        ImageIcon icon = new ImageIcon("src/main/resources/loginscreenimage.jpg");
        Image img = icon.getImage();

        JPanel rightPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
            }
        };

        // --- SPLIT PANE ---
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scrollPane, rightPanel);
        splitPane.setDividerLocation(450);
        splitPane.setEnabled(false);
        splitPane.setDividerSize(0);

        add(splitPane, BorderLayout.CENTER);

        // Request focus on first text field
        txtUsername.requestFocusInWindow();
    }

    private void handleSignup() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();
        String confirmPassword = new String(txtConfirmPassword.getPassword()).trim();
        String role = cmbRole.getSelectedItem().toString();
        String employeeId = txtEmployeeId.getText().trim();

        // Check password match
        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match!");
            return;
        }

        if ("Admin".equalsIgnoreCase(role)) {
            employeeId = null;
        } else if (employeeId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Employee ID is required for Manager and Staff.");
            return;
        }

        UserDAO dao = new UserDAO();
        EmployeeDAO empDao = new EmployeeDAO();
        Employee emp = null;

        if (employeeId != null) {
            emp = empDao.getById(employeeId);
            if (emp == null) {
                JOptionPane.showMessageDialog(this, "No employee found with ID: " + employeeId);
                return;
            }
        }

        boolean added = dao.signup(new User(username, password, role, employeeId));
        if (added) {
            JOptionPane.showMessageDialog(this, "Account created successfully!");
        } else {
            JOptionPane.showMessageDialog(this, "Error creating account!");
        }
    }
}
