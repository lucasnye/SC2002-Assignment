package util;

import entity.user.User;

/**
 * Manages the current user session
 */
public class SessionManager {
    
    private static SessionManager instance;
    private User currentUser;
    
    // Private constructor for singleton pattern
    private SessionManager() {
        this.currentUser = null;
    }
    
    /**
     * Gets the singleton instance of SessionManager
     */
    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }
    
    /**
     * Sets the current logged-in user
     */
    public void login(User user) {
        this.currentUser = user;
    }
    
    /**
     * Logs out the current user
     */
    public void logout() {
        this.currentUser = null;
    }
    
    /**
     * Gets the current logged-in user
     */
    public User getCurrentUser() {
        return currentUser;
    }
    
    /**
     * Checks if a user is currently logged in
     */
    public boolean isLoggedIn() {
        return currentUser != null;
    }
    
    /**
     * Gets the current user's type
     */
    public String getCurrentUserType() {
        if (currentUser == null) {
            return null;
        }
        return currentUser.getUserType();
    }
    
    /**
     * Gets the current user's ID
     */
    public String getCurrentUserId() {
        if (currentUser == null) {
            return null;
        }
        return currentUser.getUserId();
    }
}