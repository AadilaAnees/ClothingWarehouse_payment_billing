package Models;

public class Customer {
    private String customerId;
    private String cusName;
    private String address;
    private String telNo;

    // Default constructor
    public Customer() {}

    // Full constructor
    public Customer(String customerId, String cusName, String address, String telNo) {
        this.customerId = customerId;
        this.cusName = cusName;
        this.address = address;
        this.telNo = telNo;
    }

    // Getters and setters
    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }

    public String getCusName() { return cusName; }
    public void setCusName(String cusName) { this.cusName = cusName; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getTelNo() { return telNo; }
    public void setTelNo(String telNo) { this.telNo = telNo; }

    // Helper to convert to String array (for table models etc.)
    public String[] toValuesArray() {
        return new String[]{customerId, cusName, address, telNo};
    }
}
