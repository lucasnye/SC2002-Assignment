package controller;

import entity.user.User;
import entity.user.Student;
import entity.user.CompanyRepresentative;
import entity.user.CareerCenterStaff;
import util.SessionManager;
import util.ValidationUtil;

import java.util.List;

/**
 * Handles user authentication operations
 */
public class AuthController {
    
    private List<Student> students;
    private List<CompanyRepresentative> companyReps;
    private List<CareerCenterStaff> staff;
    private SessionManager sessionManager;
    
    public AuthController(List<Student> students, 
                         List<CompanyRepresentative> companyReps,
                         List<CareerCenterStaff> staff) {
        this.students = students;
        this.companyReps = companyReps;
        this.staff = staff;
        this.sessionManager = SessionManager.getInstance();
    }
    
    /**
     * Authenticates a user and logs them in
     * @param userId User ID
     * @param password Password
     * @return true if login successful, false otherwise
     */
    public boolean login(String userId, String password) {
        User user = findUser(userId);
        
        if (user == null) {
            System.out.println("User not found.");
            return false;
        }
        
        // Check if company representative is approved
        if (user instanceof CompanyRepresentative) {
            CompanyRepresentative rep = (CompanyRepresentative) user;
            if (!rep.isRegistrationApproved()) {
                System.out.println("Your account is pending approval by Career Center Staff.");
                return false;
            }
        }
        
        // Validate password
        if (!user.validatePassword(password)) {
            System.out.println("Incorrect password.");
            return false;
        }
        
        // Login successful
        sessionManager.login(user);
        System.out.println("Login successful! Welcome, " + user.getName());
        return true;
    }
    
    /**
     * Logs out the current user
     */
    public void logout() {
        if (sessionManager.isLoggedIn()) {
            System.out.println("Goodbye, " + sessionManager.getCurrentUser().getName());
            sessionManager.logout();
        }
    }
    
    /**
     * Changes the password for the current user
     * @param oldPassword Current password
     * @param newPassword New password
     * @return true if password changed successfully
     */
    public boolean changePassword(String oldPassword, String newPassword) {
        if (!sessionManager.isLoggedIn()) {
            System.out.println("Please login first.");
            return false;
        }
        
        if (!ValidationUtil.isNotEmpty(newPassword)) {
            System.out.println("New password cannot be empty.");
            return false;
        }
        
        User currentUser = sessionManager.getCurrentUser();
        
        if (currentUser.changePassword(oldPassword, newPassword)) {
            System.out.println("Password changed successfully!");
            return true;
        } else {
            System.out.println("Incorrect old password.");
            return false;
        }
    }
    
    /**
     * Finds a user by their ID across all user lists
     * @param userId User ID to search for
     * @return User object if found, null otherwise
     */
    private User findUser(String userId) {
        // Check if it's a student ID
        if (ValidationUtil.isValidStudentId(userId)) {
            for (Student student : students) {
                if (student.getUserId().equals(userId)) {
                    return student;
                }
            }
        }
        
        // Check if it's a staff ID
        if (ValidationUtil.isValidStaffId(userId)) {
            for (CareerCenterStaff staffMember : staff) {
                if (staffMember.getUserId().equals(userId)) {
                    return staffMember;
                }
            }
        }
        
        // Check company representatives (by ID)
        for (CompanyRepresentative rep : companyReps) {
            if (rep.getUserId().equals(userId)) {
                return rep;
            }
        }
        
        return null;
    }
    
    /**
     * Gets the current logged-in user
     */
    public User getCurrentUser() {
        return sessionManager.getCurrentUser();
    }
    
    /**
     * Checks if a user is currently logged in
     */
    public boolean isLoggedIn() {
        return sessionManager.isLoggedIn();
    }
}