package GUI;

import DAO.PaymentDAO;
import DAO.BillingDAO;
import Models.Payment;
import Models.Billing;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;



public class PaymentPanel extends JPanel {


    private MainFrame mainFrame; // Reference to MainFrame to switch panels

    private JButton btnPrintBill;
    private JTextField txtPaymentId;
    private JTextField txtBillId;
    private JComboBox<String> comboPayType;
    private JTextField txtAmount;
    private JTextField txtDate;
    private JButton btnPay, btnBack;



    private PaymentDAO paymentDAO = new PaymentDAO();
    private BillingDAO billingDAO = new BillingDAO();

    public PaymentPanel(MainFrame frame) {
        this.mainFrame = frame;
        setLayout(new BorderLayout(10,10));
        setBorder(new EmptyBorder(10,10,10,10));
        setBackground(new Color(245,245,245));

        // --- Top Panel with Back Button ---
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBackground(new Color(245,245,245));
        btnBack = new JButton("Back");
        btnBack.setBackground(new Color(200,150,136));
        btnBack.setForeground(Color.WHITE);
        btnBack.setFocusPainted(false);
        btnBack.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnBack.addActionListener(e -> mainFrame.switchPanel("SalesBilling"));
        topPanel.add(btnBack);
        add(topPanel, BorderLayout.NORTH);

        // --- Center Form Panel ---
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createTitledBorder("Payment Details"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8,8,8,8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        JLabel lblPaymentId = new JLabel("Payment ID:");
        txtPaymentId = new JTextField();
        txtPaymentId.setEditable(false);

        JLabel lblBillId = new JLabel("Bill ID:");
        txtBillId = new JTextField();
        txtBillId.setEditable(false);

        JLabel lblPayType = new JLabel("Payment Type:");
        comboPayType = new JComboBox<>(new String[]{"Cash", "Card"});

        JLabel lblAmount = new JLabel("Amount:");
        txtAmount = new JTextField();
        txtAmount.setEditable(false);

        JLabel lblDate = new JLabel("Payment Date:");
        txtDate = new JTextField();
        txtDate.setEditable(false);

        gbc.gridx=0; gbc.gridy=0; formPanel.add(lblPaymentId, gbc);
        gbc.gridx=1; gbc.gridy=0; formPanel.add(txtPaymentId, gbc);

        gbc.gridx=0; gbc.gridy=1; formPanel.add(lblBillId, gbc);
        gbc.gridx=1; gbc.gridy=1; formPanel.add(txtBillId, gbc);

        gbc.gridx=0; gbc.gridy=2; formPanel.add(lblPayType, gbc);
        gbc.gridx=1; gbc.gridy=2; formPanel.add(comboPayType, gbc);

        gbc.gridx=0; gbc.gridy=3; formPanel.add(lblAmount, gbc);
        gbc.gridx=1; gbc.gridy=3; formPanel.add(txtAmount, gbc);

        gbc.gridx=0; gbc.gridy=4; formPanel.add(lblDate, gbc);
        gbc.gridx=1; gbc.gridy=4; formPanel.add(txtDate, gbc);

        // --- Bottom Buttons ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        btnPay = new JButton("Complete Payment without bill");
        btnPay.setBackground(new Color(0,150,136));
        btnPay.setForeground(Color.WHITE);
        btnPay.setFocusPainted(false);
        btnPay.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnPay.addActionListener(e -> completePayment());

        buttonPanel.add(btnPay);

        JButton btnPrint = new JButton("Complete Payment & Print Bill as PDF");
        btnPrint.setBackground(new Color(0, 120, 215));
        btnPrint.setForeground(Color.WHITE);
        btnPrint.setFocusPainted(false);
        btnPrint.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnPrint.addActionListener(e -> generateBillPDF());
        buttonPanel.add(btnPrint);




        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    // Method to load Payment info after saving bill
    public void loadPaymentData(int paymentId, int billId) {
        txtPaymentId.setText(String.valueOf(paymentId));
        txtBillId.setText(String.valueOf(billId));

        Payment p = paymentDAO.getById(paymentId);
        if (p != null) {
            txtAmount.setText(String.valueOf(p.getAmount()));
            txtDate.setText(LocalDate.now().toString());
            comboPayType.setSelectedItem(p.getPayType());
        }
    }

    private void completePayment() {
        try {
            int paymentId = Integer.parseInt(txtPaymentId.getText());
            int billId = Integer.parseInt(txtBillId.getText());

            String payType = comboPayType.getSelectedItem().toString();

            // Get logged-in employee ID
            String employeeId = String.valueOf(mainFrame.getLoggedInUser().getUserId());

            // Update payment type if changed
            Payment p = paymentDAO.getById(paymentId);
            if (p != null) {

                p = new Payment(
                        paymentId,
                        payType,
                        p.getAmount(),
                        txtDate.getText(),
                        employeeId,        // <-- set employee who handled payment
                        p.getCustomerId()
                );

                paymentDAO.update(p, paymentId);

                // Also update billing status to "Paid"
                Billing b = billingDAO.getById(billId);
                if (b != null) {
                    b.setStatus("Paid");
                    billingDAO.update(b);
                }
            }

            JOptionPane.showMessageDialog(this, "Payment completed");

            mainFrame.switchPanel("Dashboard"); // go back to dashboard

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error completing payment: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void generateBillPDF() {
        try {
            int billId = Integer.parseInt(txtBillId.getText());
            Billing bill = billingDAO.getById(billId);

            if (bill == null) {
                JOptionPane.showMessageDialog(this, "Bill not found!");
                return;
            }

            // Fetch BillDetails
            DAO.BillDetailsDAO bdDAO = new DAO.BillDetailsDAO();
            java.util.List<Models.BillDetails> items = bdDAO.getByBillId(billId);

            // Create PDF document
            org.apache.pdfbox.pdmodel.PDDocument document = new org.apache.pdfbox.pdmodel.PDDocument();
            org.apache.pdfbox.pdmodel.PDPage page = new org.apache.pdfbox.pdmodel.PDPage();
            document.addPage(page);

            org.apache.pdfbox.pdmodel.PDPageContentStream content = new org.apache.pdfbox.pdmodel.PDPageContentStream(document, page);

            // Define starting position for text
            final int START_X = 50;
            final int START_Y = 750;
            int currentY = START_Y;

            // Add title
            content.beginText();
            content.setFont(org.apache.pdfbox.pdmodel.font.PDType1Font.HELVETICA_BOLD, 20);
            content.newLineAtOffset(220, currentY);
            content.showText("Customer Bill");
            currentY -= 50;
            content.endText();

            // Bill info
            content.beginText();
            content.setFont(org.apache.pdfbox.pdmodel.font.PDType1Font.HELVETICA, 12);
            content.newLineAtOffset(START_X, currentY);
            content.showText("Bill ID: " + billId);
            currentY -= 15;
            content.newLineAtOffset(0, -15);
            content.showText("Payment Date: " + bill.getBillDate());
            currentY -= 15;
            content.newLineAtOffset(0, -15);
            content.showText("Customer ID: " + bill.getCustomerId());
            currentY -= 25;
            content.newLineAtOffset(0, -25); // This offset is cumulative from the previous line

            // Table header - FIX: Replace \t with spaces or use separate calls/manual positioning
            // For simplicity, we'll use separate showText calls to manually position the columns.
            content.showText("Item");
            content.newLineAtOffset(150, 0); // Move cursor right by 150 points for Qty
            content.showText("Qty");
            content.newLineAtOffset(50, 0); // Move cursor right by 50 points for Price
            content.showText("Price");
            content.newLineAtOffset(50, 0); // Move cursor right by 50 points for Total
            content.showText("Total");

            currentY -= 15;
            content.newLineAtOffset(-250, -15); // Move back to START_X and down for the first item (150+50+50 = 250)

            // Table items - FIX: Replace \t with spaces and use separate calls
            for (Models.BillDetails bd : items) {
                // Item
                content.showText(String.valueOf(bd.getClothId()));


                // Qty
                content.newLineAtOffset(150, 0);
                content.showText(String.valueOf(bd.getQuantity()));

                // Price
                content.newLineAtOffset(50, 0);
                content.showText(String.valueOf(bd.getTotalAmount() / bd.getQuantity()));

                // Total
                content.newLineAtOffset(50, 0);
                content.showText(String.valueOf(bd.getTotalAmount()));

                currentY -= 15;
                content.newLineAtOffset(-250, -15); // Move back to START_X and down for the next item
            }

            // Grand total
            content.newLineAtOffset(0, -15);
            content.showText("Grand Total: " + bill.getTotalAmount());

            content.endText();
            content.close();

            // Save PDF
            String fileName = "Bill_" + billId + ".pdf";
            document.save(fileName);
            document.close();

            // Complete Payment after successful PDF generation
            completePayment();
            JOptionPane.showMessageDialog(this, "Bill saved as " + fileName);

        } catch (Exception ex) {
            // Note: The original code showed an error dialog here, but the updated code
            // also completes the payment on success, which seems intended by the button's purpose.
            // I'll keep the error dialog in the catch block.
            JOptionPane.showMessageDialog(this, "Error generating PDF: " + ex.getMessage());
            ex.printStackTrace();
        }
    }


}

