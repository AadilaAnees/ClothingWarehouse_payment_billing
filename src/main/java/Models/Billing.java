package Models;

public class Billing {
    private int BillId; // auto-generated primary key
    private String BillDate;
    private double Amount;
    private String BillDescription;
    private String BillStatus;
    private String PaymentId;
    private String CustomerId;

    // Constructor without BillId (for inserting new billing)
    public Billing(String BillDate, double Amount, String BillDescription, String BillStatus, String PaymentId, String CustomerId) {
        this.BillDate = BillDate;
        this.Amount = Amount;
        this.BillDescription = BillDescription;
        this.BillStatus = BillStatus;
        this.PaymentId = PaymentId;
        this.CustomerId = CustomerId;
    }

    // Constructor with BillId (for retrieving existing billing)
    public Billing(int BillId, String BillDate, double Amount, String BillDescription, String BillStatus, String PaymentId, String CustomerId) {
        this.BillId = BillId;
        this.BillDate = BillDate;
        this.Amount = Amount;
        this.BillDescription = BillDescription;
        this.BillStatus = BillStatus;
        this.PaymentId = PaymentId;
        this.CustomerId = CustomerId;
    }

    public int getBillId() {
        return BillId;
    }

    public void setBillId(int billId) {
        this.BillId = billId;
    }

    public String[] toValuesArray() {
        return new String[]{BillDate, String.valueOf(Amount), BillDescription, BillStatus, PaymentId, CustomerId};
    }

    public void setStatus(String paid) {
    }

    public String getBillDate() {
        return BillDate;
    }

    public String getCustomerId() {
        return CustomerId;
    }

    public String getTotalAmount() {
        return String.valueOf(Amount);
    }
}
