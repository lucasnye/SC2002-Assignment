package enums;

public enum InternshipStatus {
	PENDING("Pending Approval"),
    APPROVED("Approved"),
    REJECTED("Rejected"),
    FILLED("All Slots Filled");
    
    private final String displayName;
    
    InternshipStatus(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
}
