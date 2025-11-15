package enums;

public enum RequestStatus {
	PENDING("Pending Review"),
    APPROVED("Approved"),
    REJECTED("Rejected");
    
    private final String displayName;
    
    RequestStatus(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
}
