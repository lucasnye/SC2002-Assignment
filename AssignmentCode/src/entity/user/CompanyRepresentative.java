package entity.user;

import entity.domain.InternshipOpportunity;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a Company Representative user in the system
 */
public class CompanyRepresentative extends User {
    private static final long serialVersionUID = 1L;
    private static final int MAX_INTERNSHIP_OPPORTUNITIES = 5;
    
    private String companyName;  // CHANGED from Company object to String
    private String department;
    private String position;
    private boolean registrationApproved;
    private List<InternshipOpportunity> createdOpportunities;
    
    /**
     * Constructor for CompanyRepresentative
     * @param userId Company representative ID (assigned upon approval)
     * @param name Representative's full name
     * @param email Company email address
     * @param password Representative's password
     * @param companyName Name of the company
     * @param department Department within the company
     * @param position Job position/title
     */
    public CompanyRepresentative(String userId, String name, String email, String password, 
                                  String companyName, String department, String position) {
        super(userId, name, email, password);  // UPDATED
        this.companyName = companyName;  // CHANGED
        this.department = department;
        this.position = position;
        this.registrationApproved = false;
        this.createdOpportunities = new ArrayList<>();
    }
    
    // Getters and Setters
    /**
     * Returns company name
     */
    public String getCompanyName() {  // CHANGED
        return companyName;
    }
    
    /**
     * Sets company name
     */
    public void setCompanyName(String companyName) {  // CHANGED
        this.companyName = companyName;
    }
    
    /**
     * Returns company department
     */
    public String getDepartment() {
        return department;
    }
    
    /**
     * Sets company department
     */
    public void setDepartment(String department) {
        this.department = department;
    }
    
    /**
     * Returns position
     */
    public String getPosition() {
        return position;
    }
    
    /**
     * Sets position
     */
    public void setPosition(String position) {
        this.position = position;
    }
    
    /**
     * Returns registration status
     */
    public boolean isRegistrationApproved() {
        return registrationApproved;
    }
    
    /**
     * Sets registration status
     */
    public void setRegistrationApproved(boolean registrationApproved) {
        this.registrationApproved = registrationApproved;
    }
    
    /**
     * Returns internship opportunities created
     */
    public List<InternshipOpportunity> getCreatedOpportunities() {
        return createdOpportunities;
    }
    
    /**
     * Returns if adding an internship opportunity is successful
     */
    public boolean addOpportunity(InternshipOpportunity opportunity) {
        if (createdOpportunities.size() < MAX_INTERNSHIP_OPPORTUNITIES) {
            createdOpportunities.add(opportunity);
            return true;
        }
        return false;
    }
    
    /**
     * Removes internship opportunity
     */
    public void removeOpportunity(InternshipOpportunity opportunity) {
        createdOpportunities.remove(opportunity);
    }
    
    /**
     * Returns if representative can still create new internship opportunities
     */
    public boolean canCreateOpportunity() {
        return createdOpportunities.size() < MAX_INTERNSHIP_OPPORTUNITIES;
    }
    
    /**
     * Returns user type (company representative)
     */
    @Override
    public String getUserType() {
        return "COMPANY_REP";
    }
    
    /**
     * Prints company representative information
     */
    @Override
    public String toString() {
        return super.toString() + ", Company: " + companyName +   // CHANGED
               ", Department: " + department + ", Position: " + position;
    }
}