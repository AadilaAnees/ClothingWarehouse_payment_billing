package DAO;

import DBConnection.DBConnect;
import Models.Payment;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PaymentDAO {

    // 1. Insert new Payment
    public boolean insert(Payment payment) {
        String sql = "INSERT INTO Payment (PayType, Amount, PayDate, EmployeeId, CustomerId) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnect.getDBConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            String[] values = payment.toValuesArray();
            pstmt.setString(1, values[0]);                  // PayType
            pstmt.setDouble(2, Double.parseDouble(values[1])); // Amount
            pstmt.setDate(3, Date.valueOf(values[2]));     // PayDate
            pstmt.setString(4, values[3]);                 // EmployeeId
            pstmt.setString(5, values[4]);                 // CustomerId

            int rows = pstmt.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            System.out.println("Error inserting Payment: " + e.getMessage());
            return false;
        }
    }

    // 2. Update Payment by PaymentId
    public boolean update(Payment payment, int paymentId) {
        String sql = "UPDATE Payment SET PayType = ?, Amount = ?, PayDate = ?, EmployeeId = ?, CustomerId = ? WHERE PaymentId = ?";
        try (Connection conn = DBConnect.getDBConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            String[] values = payment.toValuesArray();
            pstmt.setString(1, values[0]);
            pstmt.setDouble(2, Double.parseDouble(values[1]));
            pstmt.setDate(3, Date.valueOf(values[2]));
            pstmt.setString(4, values[3]);
            pstmt.setString(5, values[4]);
            pstmt.setInt(6, paymentId);

            int rows = pstmt.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            System.out.println("Error updating Payment: " + e.getMessage());
            return false;
        }
    }

    // 3. Delete Payment by PaymentId
    public boolean delete(int paymentId) {
        String sql = "DELETE FROM Payment WHERE PaymentId = ?";
        try (Connection conn = DBConnect.getDBConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, paymentId);
            int rows = pstmt.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            System.out.println("Error deleting Payment: " + e.getMessage());
            return false;
        }
    }

    // 4. Get Payment by PaymentId
    public Payment getById(int paymentId) {
        String sql = "SELECT * FROM Payment WHERE PaymentId = ?";
        try (Connection conn = DBConnect.getDBConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, paymentId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new Payment(
                        rs.getString("PayType"),
                        rs.getDouble("Amount"),
                        rs.getDate("PayDate").toString(),
                        rs.getString("EmployeeId"),
                        rs.getString("CustomerId")
                );
            }

        } catch (SQLException e) {
            System.out.println("Error fetching Payment: " + e.getMessage());
        }
        return null;
    }

    // 5. Get all Payments
    public List<Payment> getAll() {
        List<Payment> list = new ArrayList<>();
        String sql = "SELECT * FROM Payment";

        try (Connection conn = DBConnect.getDBConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Payment payment = new Payment(
                        rs.getString("PayType"),
                        rs.getDouble("Amount"),
                        rs.getDate("PayDate").toString(),
                        rs.getString("EmployeeId"),
                        rs.getString("CustomerId")
                );
                list.add(payment);
            }

        } catch (SQLException e) {
            System.out.println("Error fetching all Payments: " + e.getMessage());
        }

        return list;
    }
    // Insert new Payment and return generated PaymentId
    public int insertAndGetId(Payment p) {
        String sql = "INSERT INTO Payment (PayType, Amount, PayDate, EmployeeId, CustomerId) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBConnect.getDBConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, p.getPayType());                 // PayType
            ps.setDouble(2, p.getAmount());                  // Amount
            ps.setDate(3, Date.valueOf(p.getPayDate()));     // PayDate
            ps.setString(4, p.getEmployeeId());              // EmployeeId
            ps.setString(5, p.getCustomerId());              // CustomerId

            int affected = ps.executeUpdate();
            if (affected == 0) return -1;

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1); // return generated PaymentId
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

}
