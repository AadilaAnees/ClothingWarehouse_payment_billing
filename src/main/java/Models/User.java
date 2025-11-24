package Models;

public class User {
    private int userId;
    private String username;
    private String password; // This will store HASHED password
    private String role;
    private String employeeId;

    public User() {}

    // Constructor for fetching from DB (already hashed)
    public User(int userId, String username, String password, String role, String employeeId) {
        this.userId = userId;
        this.username = username;
        this.password = password; // hashed password from DB
        this.role = role;
        this.employeeId = employeeId;
    }

    // Constructor for creating new user (password will be hashed in DAO)
    public User(String username, String password, String role, String employeeId) {
        this.username = username;
        this.password = password; // plain password for now (DAO will hash)
        this.role = role;
        this.employeeId = employeeId;
    }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }

    // Setter allows DAO to store hashed password
    public void setPassword(String password) {
        this.password = password; // may be plain or hashed depending on DAO
    }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getEmployeeId() { return employeeId; }
    public void setEmployeeId(String employeeId) { this.employeeId = employeeId; }
}
