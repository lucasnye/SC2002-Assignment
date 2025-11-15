package enums;

public enum UserType {
	STUDENT("Student"),
    COMPANY_REP("Company Representative"),
    CAREER_STAFF("Career Center Staff");
    
    private final String displayName;
    
    UserType(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
}
