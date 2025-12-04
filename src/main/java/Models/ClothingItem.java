package Models;
public class ClothingItem {
    private String ClothId;
    private String Color;
    private String Material;
    private String Size;
    private String Category;
    private double Price;
    private String SupplierId;

    public ClothingItem(String ClothId, String Color, String Material, String Size, String Category, double Price, String SupplierId) {
        this.ClothId = ClothId;
        this.Color = Color;
        this.Material = Material;
        this.Size = Size;
        this.Category = Category;
        this.Price = Price;
        this.SupplierId = SupplierId;
    }

    // Add getters
    public String getClothId() {
        return ClothId;
    }

    public String getCategory() {
        return Category;
    }

    public double getPrice() {
        return Price;
    }

    // Optional: other getters
    public String getColor() { return Color; }
    public String getMaterial() { return Material; }
    public String getSize() { return Size; }
    public String getSupplierId() { return SupplierId; }

    public String[] toValuesArray() {
        return new String[]{ClothId, Color, Material, Size, Category, String.valueOf(Price), SupplierId};
    }
}
