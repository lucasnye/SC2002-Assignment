package entity.user;

/**
 * Represents a Career Center Staff user in the system
 */
public class CareerCenterStaff extends User {
    private static final long serialVersionUID = 1L;
    
    private String staffDepartment;
    
    /**
     * Constructor for CareerCenterStaff
     * @param userId NTU staff account ID
     * @param name Staff member's full name
     * @param email Staff email address
     * @param password Staff member's password
     * @param staffDepartment Department within Career Center
     */
    public CareerCenterStaff(String userId, String name, String email, String password, String staffDepartment) {
        super(userId, name, email, password);  // UPDATED
        this.staffDepartment = staffDepartment;
    }
    
    /**
     * Returns staff department
     */
    public String getStaffDepartment() {
        return staffDepartment;
    }
    
    /**
     * Sets staff department
     */
    public void setStaffDepartment(String staffDepartment) {
        this.staffDepartment = staffDepartment;
    }
    
    /**
     * Returns user type (career center staff)
     */
    @Override
    public String getUserType() {
        return "CAREER_STAFF";
    }
    
    /**
     * Prints staff information
     */
    @Override
    public String toString() {
        return super.toString() + ", Department: " + staffDepartment;
    }
}