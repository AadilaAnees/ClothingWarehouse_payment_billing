package DAO;

import DBConnection.DBConnect;
import Models.Employee;
import Models.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDAO {

    // --- Role-based access ---
    private boolean hasPermission(User user, String operation, String position) {
        if (user == null) return false;
        String role = user.getRole();
        switch (operation.toUpperCase()) {
            case "ADD":
                if ("Admin".equalsIgnoreCase(role)) return true;
                if ("Manager".equalsIgnoreCase(role)) {
                    // Manager cannot add another manager
                    return !"Manager".equalsIgnoreCase(position);
                }
                return false;
            case "DELETE":
                return "Admin".equalsIgnoreCase(role);
            case "UPDATE":
                return "Admin".equalsIgnoreCase(role) || "Manager".equalsIgnoreCase(role);
            case "VIEW":
            case "SEARCH":
                return true;
            default:
                return false;
        }
    }

    // --- CRUD Operations with Role Check ---
    public boolean insert(Employee emp, User user) {
        if (emp == null || emp.getEmployeeId().isEmpty() || exists(emp.getEmployeeId())) return false;

        if (!hasPermission(user, "ADD", emp.getEmpPosition())) return false;

        String sql = "INSERT INTO Employee (EmployeeId, EmpName, TellNo, Salary, EmpPosition) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnect.getDBConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, emp.getEmployeeId());
            ps.setString(2, emp.getEmpName());
            ps.setString(3, emp.getTellNo());
            ps.setDouble(4, emp.getSalary());
            ps.setString(5, emp.getEmpPosition());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error inserting Employee: " + e.getMessage());
            return false;
        }
    }

    public boolean update(Employee emp, User user) {
        if (!hasPermission(user, "UPDATE", emp.getEmpPosition())) return false;
        if (emp == null || emp.getEmployeeId().isEmpty()) return false;

        String sql = "UPDATE Employee SET EmpName = ?, TellNo = ?, Salary = ?, EmpPosition = ? WHERE EmployeeId = ?";
        try (Connection conn = DBConnect.getDBConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, emp.getEmpName());
            ps.setString(2, emp.getTellNo());
            ps.setDouble(3, emp.getSalary());
            ps.setString(4, emp.getEmpPosition());
            ps.setString(5, emp.getEmployeeId());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error updating Employee: " + e.getMessage());
            return false;
        }
    }

    public boolean delete(String employeeId, User user) {
        if (!hasPermission(user, "DELETE", null)) return false;
        if (employeeId.isEmpty()) return false;

        String sql = "DELETE FROM Employee WHERE EmployeeId = ?";
        try (Connection conn = DBConnect.getDBConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, employeeId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error deleting Employee: " + e.getMessage());
            return false;
        }
    }

    // --- Read Operations ---
    public Employee getById(String employeeId) {
        String sql = "SELECT * FROM Employee WHERE EmployeeId = ?";
        try (Connection conn = DBConnect.getDBConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, employeeId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Employee(
                        rs.getString("EmployeeId"),
                        rs.getString("EmpName"),
                        rs.getString("TellNo"),
                        rs.getDouble("Salary"),
                        rs.getString("EmpPosition")
                );
            }
        } catch (SQLException e) {
            System.out.println("Error fetching Employee: " + e.getMessage());
        }
        return null;
    }

    public List<Employee> getAll() {
        List<Employee> list = new ArrayList<>();
        String sql = "SELECT * FROM Employee";
        try (Connection conn = DBConnect.getDBConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                list.add(new Employee(
                        rs.getString("EmployeeId"),
                        rs.getString("EmpName"),
                        rs.getString("TellNo"),
                        rs.getDouble("Salary"),
                        rs.getString("EmpPosition")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Error fetching all Employees: " + e.getMessage());
        }
        return list;
    }

    public List<Employee> searchByIdOrName(String keyword) {
        List<Employee> list = new ArrayList<>();
        String sql = "SELECT * FROM Employee WHERE EmployeeId LIKE ? OR EmpName LIKE ?";
        try (Connection conn = DBConnect.getDBConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            String pattern = "%" + keyword + "%";
            ps.setString(1, pattern);
            ps.setString(2, pattern);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Employee(
                        rs.getString("EmployeeId"),
                        rs.getString("EmpName"),
                        rs.getString("TellNo"),
                        rs.getDouble("Salary"),
                        rs.getString("EmpPosition")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Error searching Employees: " + e.getMessage());
        }
        return list;
    }

    public boolean exists(String employeeId) {
        String sql = "SELECT COUNT(*) FROM Employee WHERE EmployeeId = ?";
        try (Connection conn = DBConnect.getDBConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, employeeId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;

        } catch (SQLException e) {
            System.out.println("Error checking Employee ID: " + e.getMessage());
        }
        return false;
    }
}
