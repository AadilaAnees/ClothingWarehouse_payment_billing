package DAO;

import DBConnection.DBConnect;
import Models.Customer;
import Models.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO {

    // --- Role-based access ---
    private boolean hasPermission(User user, String operation) {
        if (user == null) return false;
        String role = user.getRole();
        switch (operation.toUpperCase()) {
            case "ADD":
            case "UPDATE":
                return "Admin".equalsIgnoreCase(role) ||
                        "Manager".equalsIgnoreCase(role) ||
                        "Staff".equalsIgnoreCase(role); // Staff can add & update
            case "DELETE":
                return "Admin".equalsIgnoreCase(role) || "Manager".equalsIgnoreCase(role); // Only Admin/Manager
            case "VIEW":
            case "SEARCH":
                return true;
            default:
                return false;
        }
    }

    // --- CRUD Operations with Role Check ---
    public boolean insert(Customer customer, User user) {
        if (!hasPermission(user, "ADD")) return false;
        if (customer == null || customer.getCustomerId().isEmpty() || exists(customer.getCustomerId())) return false;

        String sql = "INSERT INTO Customer (CustomerId, CusName, Address, TelNo) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnect.getDBConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, customer.getCustomerId());
            ps.setString(2, customer.getCusName());
            ps.setString(3, customer.getAddress());
            ps.setString(4, customer.getTelNo());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error inserting Customer: " + e.getMessage());
            return false;
        }
    }

    public boolean update(Customer customer, User user) {
        if (!hasPermission(user, "UPDATE")) return false;
        if (customer == null || customer.getCustomerId().isEmpty()) return false;

        String sql = "UPDATE Customer SET CusName = ?, Address = ?, TelNo = ? WHERE CustomerId = ?";
        try (Connection conn = DBConnect.getDBConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, customer.getCusName());
            ps.setString(2, customer.getAddress());
            ps.setString(3, customer.getTelNo());
            ps.setString(4, customer.getCustomerId());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error updating Customer: " + e.getMessage());
            return false;
        }
    }

    public boolean delete(String customerId, User user) {
        if (!hasPermission(user, "DELETE")) return false;
        if (customerId.isEmpty()) return false;

        String sql = "DELETE FROM Customer WHERE CustomerId = ?";
        try (Connection conn = DBConnect.getDBConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, customerId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error deleting Customer: " + e.getMessage());
            return false;
        }
    }

    // --- Read Operations (all roles can access) ---
    public Customer getById(String customerId) {
        String sql = "SELECT * FROM Customer WHERE CustomerId = ?";
        try (Connection conn = DBConnect.getDBConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, customerId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Customer(
                        rs.getString("CustomerId"),
                        rs.getString("CusName"),
                        rs.getString("Address"),
                        rs.getString("TelNo")
                );
            }
        } catch (SQLException e) {
            System.out.println("Error fetching Customer: " + e.getMessage());
        }
        return null;
    }

    public List<Customer> getAll() {
        List<Customer> list = new ArrayList<>();
        String sql = "SELECT * FROM Customer";
        try (Connection conn = DBConnect.getDBConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                list.add(new Customer(
                        rs.getString("CustomerId"),
                        rs.getString("CusName"),
                        rs.getString("Address"),
                        rs.getString("TelNo")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Error fetching all Customers: " + e.getMessage());
        }
        return list;
    }

    public List<Customer> searchByIdOrName(String keyword) {
        List<Customer> list = new ArrayList<>();
        String sql = "SELECT * FROM Customer WHERE CustomerId LIKE ? OR CusName LIKE ?";
        try (Connection conn = DBConnect.getDBConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            String pattern = "%" + keyword + "%";
            ps.setString(1, pattern);
            ps.setString(2, pattern);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Customer(
                        rs.getString("CustomerId"),
                        rs.getString("CusName"),
                        rs.getString("Address"),
                        rs.getString("TelNo")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Error searching Customers: " + e.getMessage());
        }
        return list;
    }

    // --- Check if Customer exists ---
    public boolean exists(String customerId) {
        String sql = "SELECT COUNT(*) FROM Customer WHERE CustomerId = ?";
        try (Connection conn = DBConnect.getDBConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, customerId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;

        } catch (SQLException e) {
            System.out.println("Error checking Customer ID: " + e.getMessage());
        }
        return false;
    }
}
