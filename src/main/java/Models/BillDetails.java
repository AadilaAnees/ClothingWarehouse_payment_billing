package Models;
public class BillDetails {
    private String BillId;
    private String ClothId;
    private int Quantity;
    private double TotalAmount;

    public BillDetails(String BillId, String ClothId, int Quantity, double TotalAmount) {
        this.BillId = BillId;
        this.ClothId = ClothId;
        this.Quantity = Quantity;
        this.TotalAmount = TotalAmount;
    }

    public String[] toValuesArray() {
        return new String[]{BillId, ClothId, String.valueOf(Quantity), String.valueOf(TotalAmount)};
    }

    public String getClothId() {
        return ClothId;
    }
    public int getQuantity() {
        return Quantity;
    }


    public double getTotalAmount() {
        return TotalAmount;
    }
}

