package enums;

/**
 * Represents the states of an internship listing
 */

public enum InternshipStatus {
	PENDING("Pending Approval"),
    APPROVED("Approved"),
    REJECTED("Rejected"),
    FILLED("All Slots Filled");
    
    private final String displayName;
    
    InternshipStatus(String displayName) {
        this.displayName = displayName;
    }
    
    /**
     * Returns the internship status for display
     */
    public String getDisplayName() {
        return displayName;
    }
}
