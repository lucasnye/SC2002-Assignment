package enums;

/**
 * Represents the types of users within the application
 */

public enum UserType {
	STUDENT("Student"),
    COMPANY_REP("Company Representative"),
    CAREER_STAFF("Career Center Staff");
    
    private final String displayName;
    
    UserType(String displayName) {
        this.displayName = displayName;
    }
    
    /**
     * Returns the user type for display
     */
    public String getDisplayName() {
        return displayName;
    }
}
