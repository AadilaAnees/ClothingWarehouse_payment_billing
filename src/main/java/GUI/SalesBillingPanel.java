package GUI;

import DAO.*;
import Models.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class SalesBillingPanel extends JPanel {

    private CustomerDAO customerDAO = new CustomerDAO();
    private ClothingItemDAO clothingDAO = new ClothingItemDAO();
    private PaymentDAO paymentDAO = new PaymentDAO();
    private BillingDAO billingDAO = new BillingDAO();
    private BillDetailsDAO billDetailsDAO = new BillDetailsDAO();
    private InvoiceDAO invoiceDAO = new InvoiceDAO();

    private JComboBox<String> customerCombo;
    private JComboBox<String> clothCombo;
    private JTextField qtyField, priceField, totalField;
    private JTable billTable;
    private DefaultTableModel tableModel;
    private JLabel grandTotalLabel;

    // Styling similar to EmployeePanel
    private final Color panelBackground = new Color(245, 245, 245);
    private final Color formBackground = Color.WHITE;
    private final Color buttonColor = new Color(0, 150, 136);
    private final Color backButtonColor = new Color(200, 150, 136);
    private final Color backButtonHover = new Color(200, 100, 136);
    private final Color buttonHover = new Color(0, 137, 123);
    private final Font labelFont = new Font("Segoe UI", Font.BOLD, 14);
    private final Font inputFont = new Font("Segoe UI", Font.PLAIN, 16);

    private MainFrame mainFrame;

    public SalesBillingPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;

        setLayout(new BorderLayout(10, 10));
        setBackground(panelBackground);
        setBorder(new EmptyBorder(10, 10, 10, 10));

        // --- Top Panel: Back Button ---
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBackground(panelBackground);
        JButton btnBack = new JButton("Back");
        btnBack.setBackground(backButtonColor);
        btnBack.setForeground(Color.WHITE);
        btnBack.setFont(labelFont);
        btnBack.setFocusPainted(false);
        btnBack.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnBack.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btnBack.setBackground(backButtonHover); }
            public void mouseExited(MouseEvent e) { btnBack.setBackground(backButtonColor); }
        });
        btnBack.addActionListener(e -> {
            Container topFrame = SwingUtilities.getWindowAncestor(this);
            if (topFrame instanceof MainFrame) {
                ((MainFrame) topFrame).switchPanel("Dashboard");
            }
        });
        topPanel.add(btnBack);
        add(topPanel, BorderLayout.NORTH);

        // --- Left Panel: Form + Buttons ---
        JPanel leftPanel = new JPanel(new BorderLayout(10, 10));
        leftPanel.setBackground(panelBackground);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(formBackground);
        formPanel.setBorder(BorderFactory.createTitledBorder("Sales & Billing"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.ipady = 10;

        // Customer Combo
        JLabel lblCustomer = new JLabel("Customer:"); lblCustomer.setFont(labelFont);
        customerCombo = new JComboBox<>(); customerCombo.setFont(inputFont);
        loadCustomers();

        // Clothing Item Combo
        JLabel lblCloth = new JLabel("Clothing Item:"); lblCloth.setFont(labelFont);
        clothCombo = new JComboBox<>(); clothCombo.setFont(inputFont);
        loadClothingItems();
        clothCombo.addActionListener(e -> loadPrice());

        JLabel lblQty = new JLabel("Quantity:"); lblQty.setFont(labelFont);
        qtyField = new JTextField(); qtyField.setFont(inputFont);
        qtyField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) { updateTotal(); }
        });

        JLabel lblPrice = new JLabel("Price:"); lblPrice.setFont(labelFont);
        priceField = new JTextField(); priceField.setFont(inputFont);
        priceField.setEditable(false);

        JLabel lblTotal = new JLabel("Total:"); lblTotal.setFont(labelFont);
        totalField = new JTextField(); totalField.setFont(inputFont);
        totalField.setEditable(false);

        gbc.gridx = 0; gbc.gridy = 0; formPanel.add(lblCustomer, gbc);
        gbc.gridx = 1; gbc.gridy = 0; formPanel.add(customerCombo, gbc);
        gbc.gridx = 0; gbc.gridy = 1; formPanel.add(lblCloth, gbc);
        gbc.gridx = 1; gbc.gridy = 1; formPanel.add(clothCombo, gbc);
        gbc.gridx = 0; gbc.gridy = 2; formPanel.add(lblQty, gbc);
        gbc.gridx = 1; gbc.gridy = 2; formPanel.add(qtyField, gbc);
        gbc.gridx = 0; gbc.gridy = 3; formPanel.add(lblPrice, gbc);
        gbc.gridx = 1; gbc.gridy = 3; formPanel.add(priceField, gbc);
        gbc.gridx = 0; gbc.gridy = 4; formPanel.add(lblTotal, gbc);
        gbc.gridx = 1; gbc.gridy = 4; formPanel.add(totalField, gbc);

        JPanel buttonPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        buttonPanel.setBackground(formBackground);

        JButton btnAddItem = createButton("Add Item");
        btnAddItem.addActionListener(e -> addItem());

        JButton btnRemoveItem = createButton("Remove Item");
        btnRemoveItem.addActionListener(e -> removeItem());

        JButton btnSave = createButton("Save Bill");
        btnSave.addActionListener(e -> saveBill());

        JButton btnReset = createButton("Reset");
        btnReset.addActionListener(e -> resetForm());

        buttonPanel.add(btnAddItem); buttonPanel.add(btnRemoveItem);
        buttonPanel.add(btnSave); buttonPanel.add(btnReset);

        leftPanel.add(formPanel, BorderLayout.CENTER);
        leftPanel.add(buttonPanel, BorderLayout.SOUTH);

        // --- Right Panel: Table ---
        JPanel rightPanel = new JPanel(new BorderLayout(10, 10));
        rightPanel.setBackground(panelBackground);

        tableModel = new DefaultTableModel(new String[]{"ClothId", "ClothName", "Qty", "Price", "Total"}, 0);
        billTable = new JTable(tableModel);
        billTable.setRowHeight(30);
        JScrollPane tableScroll = new JScrollPane(billTable);
        rightPanel.add(tableScroll, BorderLayout.CENTER);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
        splitPane.setDividerLocation(450);
        add(splitPane, BorderLayout.CENTER);
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
        customerCombo.removeAllItems();
        List<Customer> customers = customerDAO.getAll();
        for (Customer c : customers) {
            customerCombo.addItem(c.getCustomerId() + " - " + c.getCusName());
        }
    }

    private void loadClothingItems() {
        clothCombo.removeAllItems();
        List<ClothingItem> items = clothingDAO.getAll();
        for (ClothingItem c : items) {
            clothCombo.addItem(c.getClothId() + " - " + c.getCategory());
        }
    }


    private void loadPrice() {
        if (clothCombo.getSelectedItem() == null) return;
        String selected = clothCombo.getSelectedItem().toString().split(" - ")[0];
        ClothingItem item = clothingDAO.getById(selected);
        if (item != null) priceField.setText(String.valueOf(item.getPrice()));
        updateTotal();
    }

    private void updateTotal() {
        try {
            double price = Double.parseDouble(priceField.getText());
            int qty = Integer.parseInt(qtyField.getText());
            totalField.setText(String.valueOf(price * qty));
        } catch (Exception e) { totalField.setText("0.0"); }
    }

    private void addItem() {
        if (clothCombo.getSelectedItem() == null || qtyField.getText().isEmpty()) return;
        String[] parts = clothCombo.getSelectedItem().toString().split(" - ");
        String clothId = parts[0], clothName = parts[1];
        int qty = Integer.parseInt(qtyField.getText());
        double price = Double.parseDouble(priceField.getText());
        double total = price * qty;
        tableModel.addRow(new Object[]{clothId, clothName, qty, price, total});
    }

    private void removeItem() {
        int row = billTable.getSelectedRow();
        if (row >= 0) tableModel.removeRow(row);
    }

    private void saveBill() {

        if (customerCombo.getSelectedItem() == null || tableModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Select customer and add at least one item!");
            return;
        }

        try {
            // 1. Extract Customer ID
            String customerId = customerCombo.getSelectedItem().toString().split(" - ")[0];

            // 2. Calculate Grand Total
            double grandTotal = 0;
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                grandTotal += Double.parseDouble(tableModel.getValueAt(i, 4).toString());
            }

            // 3. Insert Payment (return PaymentId)
            String employeeId = null;
            if (mainFrame != null && mainFrame.getLoggedInUser() != null) {
                employeeId = mainFrame.getLoggedInUser().getEmployeeId();
            }

            Payment payment = new Payment(
                    "Cash",
                    grandTotal,
                    new java.sql.Date(System.currentTimeMillis()).toString(),
                    employeeId,
                    customerId
            );

            int paymentId = paymentDAO.insertAndGetId(payment);

            if (paymentId == -1) {
                JOptionPane.showMessageDialog(this, "Failed to save Payment!");
                return;
            }

            // 4. Insert Billing (return BillId)
            Billing billing = new Billing(
                    0,
                    new java.sql.Date(System.currentTimeMillis()).toString(),
                    grandTotal,
                    "Clothing Sale",
                    "paid",
                    String.valueOf(paymentId),
                    customerId
            );

            int billId = billingDAO.insertAndGetId(billing);

            if (billId == -1) {
                JOptionPane.showMessageDialog(this, "Failed to save Billing!");
                return;
            }

            // 5. Insert BillDetails rows
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                String clothId = tableModel.getValueAt(i, 0).toString();
                int qty = Integer.parseInt(tableModel.getValueAt(i, 2).toString());
                double total = Double.parseDouble(tableModel.getValueAt(i, 4).toString());

                billDetailsDAO.insert(new BillDetails(
                        String.valueOf(billId),
                        clothId,
                        qty,
                        total
                ));
            }

            // 6. Create Invoice
            invoiceDAO.insert(new Invoice(
                    new java.sql.Date(System.currentTimeMillis()).toString(),
                    String.valueOf(billId)
            ));

            //  Redirect to Payment Panel
            Container topFrame = SwingUtilities.getWindowAncestor(this);
            if (topFrame instanceof MainFrame) {
                PaymentPanel paymentPanel = new PaymentPanel((MainFrame) topFrame);
                ((MainFrame) topFrame).addPanel("Payment", paymentPanel); // add dynamically
                paymentPanel.loadPaymentData(paymentId, billId);           // load info
                ((MainFrame) topFrame).switchPanel("Payment");            // switch view
            }


            // Clear table & fields
            tableModel.setRowCount(0);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error saving bill: " + e.getMessage());
            e.printStackTrace();
        }
    }





    private void resetForm() {
        qtyField.setText(""); priceField.setText(""); totalField.setText("");
        tableModel.setRowCount(0);
    }

    public void refreshClothingItems() {
        clothCombo.removeAllItems();
        List<ClothingItem> items = clothingDAO.getAll();
        for (ClothingItem c : items) {
            clothCombo.addItem(c.getClothId() + " - " + c.getCategory());
        }
    }
    public void refreshCustomers() {
        loadCustomers();
    }






}
