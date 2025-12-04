package DBConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnect {

    public static Connection getDBConnection() {
        final String DBURL = "jdbc:mysql://localhost:3307/clothing_warehouse?serverTimezone=UTC&useSSL=false";
        final String USERNAME = "root";
        final String PASSWORD = "1234";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(DBURL, USERNAME, PASSWORD);

        } catch (ClassNotFoundException e) {
            System.out.println(" MySQL JDBC Driver not found.");
        } catch (SQLException e) {
            System.out.println(" Failed to connect to database: " + e.getMessage());
        }

        return null;
    }

    // Test the connection
    public static void main(String[] args) {
        Connection conn = getDBConnection();
        if (conn != null) {
            System.out.println(" your clothing_warehouse Database connected successfully!");
            try { conn.close(); } catch (SQLException ignored) {}
        } else {
            System.out.println(" Connection failed.");
        }
    }
}
