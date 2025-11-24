package DAO;

import DBConnection.DBConnect;
import Models.User;
import DAO.EmployeeDAO;
import Models.Employee;
import utils.PasswordUtil;   // <-- ADD THIS

import java.sql.*;

public class UserDAO {

    // REGISTER USER with employee rules
    public boolean signup(User user) {
        if (user == null || user.getUsername().isEmpty() || user.getPassword().isEmpty() || user.getRole().isEmpty()) {
            System.out.println("Invalid user data.");
            return false;
        }

        if (usernameExists(user.getUsername())) {
            System.out.println("Username already exists: " + user.getUsername());
            return false;
        }

        // --- Employee-related rules ---
        EmployeeDAO empDao = new EmployeeDAO();

        if ("Admin".equalsIgnoreCase(user.getRole())) {
            if (adminExists()) {
                System.out.println("Admin account already exists!");
                return false;
            }
            user.setEmployeeId(null);
        } else {
            if (user.getEmployeeId() == null || user.getEmployeeId().isEmpty()) {
                System.out.println("Employee ID required for Manager or Staff account.");
                return false;
            }

            Employee emp = empDao.getById(user.getEmployeeId());
            if (emp == null) {
                System.out.println("No employee found with ID: " + user.getEmployeeId());
                return false;
            }

            if ("Manager".equalsIgnoreCase(user.getRole())) {
                if (!"Manager".equalsIgnoreCase(emp.getEmpPosition())) {
                    System.out.println("Only employees with position 'Manager' can create Manager account.");
                    return false;
                }
                if (managerExists(user.getEmployeeId())) {
                    System.out.println("This employee already has a manager account.");
                    return false;
                }
            } else {
                if ("Manager".equalsIgnoreCase(emp.getEmpPosition())) {
                    System.out.println("Managers cannot create staff accounts.");
                    return false;
                }
                if (employeeHasAccount(user.getEmployeeId())) {
                    System.out.println("This employee already has an account.");
                    return false;
                }
            }
        }

        String sql = "INSERT INTO Users (Username, Password, Role, EmployeeId) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnect.getDBConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // HASH PASSWORD BEFORE SAVING
            String hashedPassword = PasswordUtil.hashPassword(user.getPassword());

            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, hashedPassword);
            pstmt.setString(3, user.getRole());
            pstmt.setString(4, user.getEmployeeId());

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Signup Error: " + e.getMessage());
            return false;
        }
    }

    // LOGIN USER (with hashed password)
    public User login(String username, String password) {
        if (username.isEmpty() || password.isEmpty()) return null;

        String sql = "SELECT * FROM Users WHERE Username = ?";

        try (Connection conn = DBConnect.getDBConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {

                String storedHash = rs.getString("Password");

                // compare hashed password
                if (!PasswordUtil.checkPassword(password, storedHash)) {
                    return null; // WRONG PASSWORD
                }

                // password valid → return User object
                return new User(
                        rs.getInt("UserId"),
                        rs.getString("Username"),
                        storedHash,
                        rs.getString("Role"),
                        rs.getString("EmployeeId")
                );
            }

        } catch (SQLException e) {
            System.out.println("Login Error: " + e.getMessage());
        }

        return null;
    }

    public boolean usernameExists(String username) {
        String sql = "SELECT UserId FROM Users WHERE Username = ?";
        try (Connection conn = DBConnect.getDBConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();

        } catch (SQLException e) {
            System.out.println("Check User Error: " + e.getMessage());
            return false;
        }
    }

    private boolean adminExists() {
        String sql = "SELECT UserId FROM Users WHERE Role='Admin'";
        try (Connection conn = DBConnect.getDBConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            return rs.next();
        } catch (SQLException e) {
            System.out.println("Check Admin Error: " + e.getMessage());
            return false;
        }
    }

    private boolean managerExists(String empId) {
        String sql = "SELECT UserId FROM Users WHERE Role='Manager' AND EmployeeId=?";
        try (Connection conn = DBConnect.getDBConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, empId);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.out.println("Check Manager Error: " + e.getMessage());
            return false;
        }
    }

    private boolean employeeHasAccount(String empId) {
        String sql = "SELECT UserId FROM Users WHERE EmployeeId=?";
        try (Connection conn = DBConnect.getDBConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, empId);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.out.println("Check Employee Account Error: " + e.getMessage());
            return false;
        }
    }
}
