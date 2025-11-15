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
    public String getCompanyName() {  // CHANGED
        return companyName;
    }
    
    public void setCompanyName(String companyName) {  // CHANGED
        this.companyName = companyName;
    }
    
    public String getDepartment() {
        return department;
    }
    
    public void setDepartment(String department) {
        this.department = department;
    }
    
    public String getPosition() {
        return position;
    }
    
    public void setPosition(String position) {
        this.position = position;
    }
    
    public boolean isRegistrationApproved() {
        return registrationApproved;
    }
    
    public void setRegistrationApproved(boolean registrationApproved) {
        this.registrationApproved = registrationApproved;
    }
    
    public List<InternshipOpportunity> getCreatedOpportunities() {
        return createdOpportunities;
    }
    
    public boolean addOpportunity(InternshipOpportunity opportunity) {
        if (createdOpportunities.size() < MAX_INTERNSHIP_OPPORTUNITIES) {
            createdOpportunities.add(opportunity);
            return true;
        }
        return false;
    }
    
    public void removeOpportunity(InternshipOpportunity opportunity) {
        createdOpportunities.remove(opportunity);
    }
    
    public boolean canCreateOpportunity() {
        return createdOpportunities.size() < MAX_INTERNSHIP_OPPORTUNITIES;
    }
    
    @Override
    public String getUserType() {
        return "COMPANY_REP";
    }
    
    @Override
    public String toString() {
        return super.toString() + ", Company: " + companyName +   // CHANGED
               ", Department: " + department + ", Position: " + position;
    }
}