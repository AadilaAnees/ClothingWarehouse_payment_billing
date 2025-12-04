package DAO;

import DBConnection.DBConnect;
import Models.Billing;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BillingDAO {

    // 1. Insert new Billing
    public boolean insert(Billing bill) {
        String sql = "INSERT INTO Billing (BillDate, Amount, BillDescription, BillStatus, PaymentId, CustomerId) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnect.getDBConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            String[] values = bill.toValuesArray();
            pstmt.setString(1, values[0]); // BillDate
            pstmt.setDouble(2, Double.parseDouble(values[1])); // Amount
            pstmt.setString(3, values[2]); // BillDescription
            pstmt.setString(4, values[3]); // BillStatus
            pstmt.setString(5, values[4]); // PaymentId
            pstmt.setString(6, values[5]); // CustomerId

            int rows = pstmt.executeUpdate();

            // get generated BillId
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                bill.setBillId(rs.getInt(1));
            }

            return rows > 0;

        } catch (SQLException e) {
            System.out.println("Error inserting Billing: " + e.getMessage());
            return false;
        }
    }

    // 2. Update Billing
    public boolean update(Billing bill) {
        String sql = "UPDATE Billing SET BillDate = ?, Amount = ?, BillDescription = ?, BillStatus = ?, PaymentId = ?, CustomerId = ? WHERE BillId = ?";
        try (Connection conn = DBConnect.getDBConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            String[] values = bill.toValuesArray();
            pstmt.setString(1, values[0]); // BillDate
            pstmt.setDouble(2, Double.parseDouble(values[1])); // Amount
            pstmt.setString(3, values[2]); // BillDescription
            pstmt.setString(4, values[3]); // BillStatus
            pstmt.setString(5, values[4]); // PaymentId
            pstmt.setString(6, values[5]); // CustomerId
            pstmt.setInt(7, bill.getBillId()); // BillId

            int rows = pstmt.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            System.out.println("Error updating Billing: " + e.getMessage());
            return false;
        }
    }

    // 3. Delete Billing by BillId
    public boolean delete(int billId) {
        String sql = "DELETE FROM Billing WHERE BillId = ?";
        try (Connection conn = DBConnect.getDBConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, billId);
            int rows = pstmt.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            System.out.println("Error deleting Billing: " + e.getMessage());
            return false;
        }
    }

    // 4. Get Billing by BillId
    public Billing getById(int billId) {
        String sql = "SELECT * FROM Billing WHERE BillId = ?";
        try (Connection conn = DBConnect.getDBConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, billId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new Billing(
                        rs.getInt("BillId"),
                        rs.getString("BillDate"),
                        rs.getDouble("Amount"),
                        rs.getString("BillDescription"),
                        rs.getString("BillStatus"),
                        rs.getString("PaymentId"),
                        rs.getString("CustomerId")
                );
            }

        } catch (SQLException e) {
            System.out.println("Error fetching Billing: " + e.getMessage());
        }
        return null;
    }

    // 5. Get all Billing records
    public List<Billing> getAll() {
        List<Billing> list = new ArrayList<>();
        String sql = "SELECT * FROM Billing";

        try (Connection conn = DBConnect.getDBConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Billing bill = new Billing(
                        rs.getInt("BillId"),
                        rs.getString("BillDate"),
                        rs.getDouble("Amount"),
                        rs.getString("BillDescription"),
                        rs.getString("BillStatus"),
                        rs.getString("PaymentId"),
                        rs.getString("CustomerId")
                );
                list.add(bill);
            }

        } catch (SQLException e) {
            System.out.println("Error fetching all Billing records: " + e.getMessage());
        }

        return list;
    }
    // Insert new Billing and return generated BillId
    public int insertAndGetId(Billing bill) {
        String sql = "INSERT INTO Billing (BillDate, Amount, BillDescription, BillStatus, PaymentId, CustomerId) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnect.getDBConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            String[] values = bill.toValuesArray();
            pstmt.setString(1, values[0]);
            pstmt.setDouble(2, Double.parseDouble(values[1]));
            pstmt.setString(3, values[2]);
            pstmt.setString(4, values[3]);
            pstmt.setString(5, values[4]);
            pstmt.setString(6, values[5]);

            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1); // return generated BillId
            }

        } catch (SQLException e) {
            System.out.println("Error inserting Billing: " + e.getMessage());
        }
        return -1;
    }


}
