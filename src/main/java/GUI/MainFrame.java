package GUI;

import Models.User;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class MainFrame extends JFrame {

    private JPanel mainPanel;
    private CardLayout cardLayout;
    private JLabel statusLabel;
    private User loggedInUser;
    private Map<String, JPanel> panels;// track all panels

    private final Color backgroundColor = new Color(245, 245, 245);

    private SalesBillingPanel salesPanel;
    private StaffViewStockPanel staffViewStockPanel;



    public SalesBillingPanel getSalesPanel() {
        return salesPanel;
    }

    public MainFrame() {
        setTitle("Clothing Warehouse Management System");
        setSize(1100, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(backgroundColor);

        panels = new HashMap<>();

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        mainPanel.setBackground(backgroundColor);
        add(mainPanel, BorderLayout.CENTER);

        statusLabel = new JLabel("Welcome! Please login.");
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        statusLabel.setOpaque(true);
        statusLabel.setBackground(new Color(230, 230, 230));
        statusLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        add(statusLabel, BorderLayout.SOUTH);

        showLoginPanel();
        setVisible(true);
    }

    public void showLoginPanel() {
        LoginPanel loginPanel = new LoginPanel(this);
        addPanel("Login", loginPanel);
        switchPanel("Login");
        statusLabel.setText("Please login to continue.");
    }

    public void showSignupPanel() {
        SignupPanel signupPanel = new SignupPanel(this);
        addPanel("Signup", signupPanel);
        switchPanel("Signup");
        statusLabel.setText("Create a new account.");
    }

    public void loadDashboardByRole(String role, User user) {
        loggedInUser = user;

        switch (role.toLowerCase()) {
            case "admin":
                AdminDashboardPanel adminDashboard = new AdminDashboardPanel(this);
                this.salesPanel = new SalesBillingPanel(this);  // FIXED
                ClothingItemPanel clothingPanel = new ClothingItemPanel();
                EmployeePanel adminEmployeePanel = new EmployeePanel(loggedInUser);
                CustomerPanel adminCustomerPanel = new CustomerPanel(loggedInUser);
                SupplierPanel supplierPanel = new SupplierPanel();

                addPanel("Dashboard", adminDashboard);
                addPanel("Sales", this.salesPanel);
                addPanel("ClothingItem", clothingPanel);
                addPanel("Employee", adminEmployeePanel);
                addPanel("Customer", adminCustomerPanel);
                addPanel("Supplier", supplierPanel);

                switchPanel("Dashboard");
                statusLabel.setText("Welcome Admin!");
                break;


            case "manager":
                ManagerDashboardPanel managerDashboard = new ManagerDashboardPanel(this);
                EmployeePanel managerEmployeePanel = new EmployeePanel(loggedInUser);
                CustomerPanel managerCustomerPanel = new CustomerPanel(loggedInUser);
                ClothingItemPanel managerclothingPanel = new ClothingItemPanel();
                this.salesPanel = new SalesBillingPanel(this);

                addPanel("Dashboard", managerDashboard);
                addPanel("Employee", managerEmployeePanel);
                addPanel("Customer", managerCustomerPanel);
                addPanel("Sales", this.salesPanel);
                addPanel("Warehouse", managerclothingPanel);


                switchPanel("Dashboard");
                statusLabel.setText("Welcome Manager!");
                break;

            case "staff":
                StaffDashboardPanel staffDashboard = new StaffDashboardPanel(this);
                CustomerPanel staffCustomerPanel = new CustomerPanel(loggedInUser);
                staffViewStockPanel = new StaffViewStockPanel(this);
                this.salesPanel = new SalesBillingPanel(this);


                addPanel("Sales", this.salesPanel);
                addPanel("Dashboard", staffDashboard);
                addPanel("Customer", staffCustomerPanel);
                addPanel("StaffViewStock", staffViewStockPanel);


                switchPanel("Dashboard");
                statusLabel.setText("Welcome Staff!");
                break;

            default:
                JOptionPane.showMessageDialog(this, "Unknown role: " + role);
                showLoginPanel();
        }
        cardLayout.show(mainPanel, "Dashboard");
        revalidate();
        repaint();


    }

    // Dynamic panel handling
    public void addPanel(String name, JPanel panel) {
        panels.put(name, panel);
        mainPanel.add(panel, name);
    }

    public void switchPanel(String name) {
        if (panels.containsKey(name)) {
            cardLayout.show(mainPanel, name);
            statusLabel.setText("You are in " + name + " module");
        }
    }

    public void setLoggedInUser(User user) {
        this.loggedInUser = user;
    }
    public User getLoggedInUser() {
        return loggedInUser;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainFrame::new);
    }


}
