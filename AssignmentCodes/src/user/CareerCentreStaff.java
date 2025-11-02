package user;

/**
 * Represents a Career Center Staff member.
 * This user role has administrative privileges to manage representative accounts,
 * internship opportunities, and student withdrawal requests.
 */

public class CareerCentreStaff extends User {

    private String staffDepartment;

    /**
     * Constructs a new CareerCenterStaff object.
     *
     * @param userID The staff's NTU account ID[cite: 36].
     * @param name The staff's full name.
     * @param password The staff's password (e.g., "password")[cite: 37].
     * @param staffDepartment The department the staff belongs to[cite: 42].
     */
    public CareerCentreStaff(String userID, String name, String password, String staffDepartment) {
        super(userID, name, password, Role.CAREERCENTRESTAFF);
        this.staffDepartment = staffDepartment;
    }

    public String getStaffDepartment() {
        return staffDepartment;
    }
    
    public void setStaffDepartment(String staffDepartment) {
    	this.staffDepartment = staffDepartment;
    }

    /**
     * Approves or rejects a Company Representative's registration request[cite: 99].
     *
     * @param reg The RepRegistration object to be reviewed.
     * @param approve True to approve, false to reject.
     */
    public void approveRep(RepRegistration reg, boolean approve) {
        if (approve) {
            reg.setStatus(RegistrationStatus.APPROVED);
            System.out.println("Registration for " + reg.getEmail() + " approved.");
            // --- YOUR LOGIC HERE ---
            // e.g., Create a new CompanyRepresentative user object from the
            // registration details and add it to your main list of users.
            
        } else {
            reg.setStatus(RegistrationStatus.REJECTED);
            System.out.println("Registration for " + reg.getEmail() + " rejected.");
            // --- YOUR LOGIC HERE ---
            // e.g., Remove the registration request from the pending list.
        }
    }

    /**
     * Reviews and approves or rejects a submitted internship opportunity[cite: 100].
     *
     * @param opId The ID of the InternshipOpportunity to review.
     * @param approve True to approve, false to reject.
     * @param reason Optional reason for rejection (can be an empty string).
     */
    public void reviewOpportunity(String opId, boolean approve, String reason) {
        // --- To Add when Internship class is out ---
        // 1. Find the InternshipOpportunity object using its opId.
        // 2. If 'approve' is true:
        //    - Set the opportunity's status to "Approved"[cite: 101].
        //    - System.out.println("Internship " + opId + " approved.");
        // 3. If 'approve' is false:
        //    - Set the opportunity's status to "Rejected".
        //    - System.out.println("Internship " + opId + " rejected. Reason: " + reason);
        
        System.out.println("Reviewing opportunity " + opId);
    }

    /**
     * Approves or rejects a student's request to withdraw from an internship[cite: 103].
     *
     * @param appId The ID of the Application to review.
     * @param approve True to approve the withdrawal, false to reject it.
     */
    public void decideWithdrawal(String appId, boolean approve) {
        // --- To Add when Application class is out ---
        // 1. Find the Application object using its appId.
        // 2. If 'approve' is true:
        //    - Set the application's status to "Withdrawn".
        //    - If the student had already accepted this placement, 
        //      re-open the slot for the internship.
        //    - System.out.println("Withdrawal for application " + appId + " approved.");
        // 3. If 'approve' is false:
        //    - The application status remains unchanged.
        //    - System.out.println("Withdrawal for application " + appId + " rejected.");

        System.out.println("Deciding on withdrawal " + appId);
    }
}
