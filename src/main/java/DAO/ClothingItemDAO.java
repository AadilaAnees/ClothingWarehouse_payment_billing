package DAO;

import DBConnection.DBConnect;
import Models.ClothingItem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClothingItemDAO {

    // 1. Insert new ClothingItem
    public boolean insert(ClothingItem item) {
        String sql = "INSERT INTO ClothingItem (ClothId, Color, Material, Size, Category, Price, SupplierId) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnect.getDBConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, item.toValuesArray()[0]); // ClothId
            pstmt.setString(2, item.toValuesArray()[1]); // Color
            pstmt.setString(3, item.toValuesArray()[2]); // Material
            pstmt.setString(4, item.toValuesArray()[3]); // Size
            pstmt.setString(5, item.toValuesArray()[4]); // Category
            pstmt.setDouble(6, Double.parseDouble(item.toValuesArray()[5])); // Price
            pstmt.setString(7, item.toValuesArray()[6]); // SupplierId

            int rows = pstmt.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            System.out.println("Error inserting ClothingItem: " + e.getMessage());
            return false;
        }
    }

    // 2. Update ClothingItem
    public boolean update(ClothingItem item) {
        String sql = "UPDATE ClothingItem SET Color = ?, Material = ?, Size = ?, Category = ?, Price = ?, SupplierId = ? WHERE ClothId = ?";
        try (Connection conn = DBConnect.getDBConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, item.toValuesArray()[1]); // Color
            pstmt.setString(2, item.toValuesArray()[2]); // Material
            pstmt.setString(3, item.toValuesArray()[3]); // Size
            pstmt.setString(4, item.toValuesArray()[4]); // Category
            pstmt.setDouble(5, Double.parseDouble(item.toValuesArray()[5])); // Price
            pstmt.setString(6, item.toValuesArray()[6]); // SupplierId
            pstmt.setString(7, item.toValuesArray()[0]); // ClothId

            int rows = pstmt.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            System.out.println("Error updating ClothingItem: " + e.getMessage());
            return false;
        }
    }

    // 3. Delete ClothingItem by ClothId
    public boolean delete(String clothId) {
        String sql = "DELETE FROM ClothingItem WHERE ClothId = ?";
        try (Connection conn = DBConnect.getDBConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, clothId);
            int rows = pstmt.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            System.out.println("Error deleting ClothingItem: " + e.getMessage());
            return false;
        }
    }

    // 4. Get ClothingItem by ID
    public ClothingItem getById(String clothId) {
        String sql = "SELECT * FROM ClothingItem WHERE ClothId = ?";
        try (Connection conn = DBConnect.getDBConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, clothId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new ClothingItem(
                        rs.getString("ClothId"),
                        rs.getString("Color"),
                        rs.getString("Material"),
                        rs.getString("Size"),
                        rs.getString("Category"),
                        rs.getDouble("Price"),
                        rs.getString("SupplierId")
                );
            }

        } catch (SQLException e) {
            System.out.println("Error fetching ClothingItem: " + e.getMessage());
        }
        return null;
    }

    // 5. Get all ClothingItems
    public List<ClothingItem> getAll() {
        List<ClothingItem> list = new ArrayList<>();
        String sql = "SELECT * FROM ClothingItem";

        try (Connection conn = DBConnect.getDBConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                ClothingItem item = new ClothingItem(
                        rs.getString("ClothId"),
                        rs.getString("Color"),
                        rs.getString("Material"),
                        rs.getString("Size"),
                        rs.getString("Category"),
                        rs.getDouble("Price"),
                        rs.getString("SupplierId")
                );
                list.add(item);
            }

        } catch (SQLException e) {
            System.out.println("Error fetching all ClothingItems: " + e.getMessage());
        }

        return list;
    }

}
