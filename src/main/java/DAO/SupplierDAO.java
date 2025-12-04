package DAO;

import DBConnection.DBConnect;
import Models.Supplier;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SupplierDAO {

    // 1. Insert new Supplier
    public boolean insert(Supplier sup) {
        String sql = "INSERT INTO Supplier (SupplierId, SupName, TellNo) VALUES (?, ?, ?)";
        try (Connection conn = DBConnect.getDBConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, sup.getSupplierId());
            pstmt.setString(2, sup.getName());
            pstmt.setString(3, sup.getContact());

            int rows = pstmt.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            System.out.println("Error inserting Supplier: " + e.getMessage());
            return false;
        }
    }

    // 2. Update Supplier
    public boolean update(Supplier sup) {
        String sql = "UPDATE Supplier SET SupName = ?, TellNo = ? WHERE SupplierId = ?";
        try (Connection conn = DBConnect.getDBConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, sup.getName());
            pstmt.setString(2, sup.getContact());
            pstmt.setString(3, sup.getSupplierId());

            int rows = pstmt.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            System.out.println("Error updating Supplier: " + e.getMessage());
            return false;
        }
    }

    // 3. Delete Supplier
    public boolean delete(String supplierId) {
        String sql = "DELETE FROM Supplier WHERE SupplierId = ?";
        try (Connection conn = DBConnect.getDBConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, supplierId);
            int rows = pstmt.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            System.out.println("Error deleting Supplier: " + e.getMessage());
            return false;
        }
    }

    // 4. Get Supplier by ID
    public Supplier getById(String supplierId) {
        String sql = "SELECT * FROM Supplier WHERE SupplierId = ?";
        try (Connection conn = DBConnect.getDBConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, supplierId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new Supplier(
                        rs.getString("SupplierId"),
                        rs.getString("SupName"),
                        rs.getString("TellNo")
                );
            }

        } catch (SQLException e) {
            System.out.println("Error fetching Supplier: " + e.getMessage());
        }
        return null;
    }

    // 5. Get All Suppliers
    public List<Supplier> getAll() {
        List<Supplier> list = new ArrayList<>();
        String sql = "SELECT * FROM Supplier";

        try (Connection conn = DBConnect.getDBConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Supplier sup = new Supplier(
                        rs.getString("SupplierId"),
                        rs.getString("SupName"),
                        rs.getString("TellNo")
                );
                list.add(sup);
            }

        } catch (SQLException e) {
            System.out.println("Error fetching Suppliers: " + e.getMessage());
        }

        return list;
    }
    // 6. Search Suppliers by Name or ID
    public List<Supplier> search(String keyword) {
        List<Supplier> list = new ArrayList<>();
        String sql = "SELECT * FROM Supplier WHERE SupplierId LIKE ? OR SupName LIKE ?";

        try (Connection conn = DBConnect.getDBConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, "%" + keyword + "%");
            pstmt.setString(2, "%" + keyword + "%");

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Supplier sup = new Supplier(
                        rs.getString("SupplierId"),
                        rs.getString("SupName"),
                        rs.getString("TellNo")
                );
                list.add(sup);
            }

        } catch (SQLException e) {
            System.out.println("Error searching Suppliers: " + e.getMessage());
        }

        return list;
    }

}
