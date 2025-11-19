package enums;

/**
 * Represents the states of an internship application
 */

public enum ApplicationStatus {
	PENDING("Pending Review"),
    SUCCESSFUL("Approved by Company"),
    UNSUCCESSFUL("Rejected by Company"),
    WITHDRAWN("Withdrawn");
    
    private final String displayName;
    
    ApplicationStatus(String displayName) {
        this.displayName = displayName;
    }
    
    /**
     * Returns the application status for display
     */
    public String getDisplayName() {
        return displayName;
    }
}
