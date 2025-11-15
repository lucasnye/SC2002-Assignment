package entity.user;

import java.io.Serializable;

/**
 * Abstract base class representing a user in the Internship Placement Management System.
 * All user types (Student, CompanyRepresentative, CareerCenterStaff) inherit from this class.
 */
public abstract class User implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String userId;
    private String name;
    private String email;  // ADDED
    private String password;
    
    /**
     * Constructor for User
     * @param userId Unique identifier for the user
     * @param name Full name of the user
     * @param email User's email address
     * @param password User's password (default is "password")
     */
    public User(String userId, String name, String email, String password) {
        this.userId = userId;
        this.name = name;
        this.email = email;  // ADDED
        this.password = password;
    }
    
    // Getters and Setters
    public String getUserId() {
        return userId;
    }
    
    public void setUserId(String userId) {
        this.userId = userId;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    // ADDED
    public String getEmail() {
        return email;
    }
    
    // ADDED
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPassword() {
        return password;
    }
    
    protected void setPassword(String password) {
        this.password = password;
    }
    
    /**
     * Validates login credentials
     * @param inputPassword Password to validate
     * @return true if password matches, false otherwise
     */
    public boolean validatePassword(String inputPassword) {
        return this.password.equals(inputPassword);
    }
    
    /**
     * Changes the user's password
     * @param oldPassword Current password for verification
     * @param newPassword New password to set
     * @return true if password change successful, false otherwise
     */
    public boolean changePassword(String oldPassword, String newPassword) {
        if (validatePassword(oldPassword)) {
            this.password = newPassword;
            return true;
        }
        return false;
    }
    
    /**
     * Abstract method to get the user type
     * Must be implemented by subclasses
     * @return UserType enum value
     */
    public abstract String getUserType();
    
    @Override
    public String toString() {
        return "User ID: " + userId + ", Name: " + name + ", Email: " + email;
    }
}
