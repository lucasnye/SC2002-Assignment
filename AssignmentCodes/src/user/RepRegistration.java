package user;

/**
 * Represents a registration request from a new Company Representative.
 * This object holds the details needed for a Career Center Staff to approve or reject the request.
 */
public class RepRegistration {
    
    private String email;
    private String companyName;
    private String department;
    private String position;
    private RegistrationStatus status;

    /**
     * Constructs a new RepRegistration object.
     * The status is automatically set to PENDING upon creation.
     *
     * @param email The representative's email (which will be their ID)[cite: 35].
     * @param companyName The name of the company.
     * @param department The representative's department.
     * @param position The representative's position in the company.
     */
    public RepRegistration(String email, String companyName, String department, String position) {
        this.email = email;
        this.companyName = companyName;
        this.department = department;
        this.position = position;
        this.status = RegistrationStatus.PENDING; // Default status
    }

    // --- Getters ---
    // Setters are primarily for the CareerCenterStaff, not the representative

    public String getEmail() {
        return email;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getDepartment() {
        return department;
    }

    public String getPosition() {
        return position;
    }

    public RegistrationStatus getStatus() {
        return status;
    }

    /**
     * Updates the status of the registration request.
     * This method would typically be called by a CareerCenterStaff.
     *
     * @param status The new status (APPROVED or REJECTED).
     */
    public void setStatus(RegistrationStatus status) {
        this.status = status;
    }
}
