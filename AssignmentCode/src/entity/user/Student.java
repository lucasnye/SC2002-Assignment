package entity.user;

import enums.Major;
import entity.domain.InternshipApplication;
import entity.domain.InternshipOpportunity;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a Student user in the system
 */
public class Student extends User {
    private static final long serialVersionUID = 1L;
    private static final int MAX_APPLICATIONS = 3;
    
    private int yearOfStudy;
    private Major major;
    private List<InternshipApplication> applications;
    private InternshipOpportunity acceptedInternship;
    
    /**
     * Constructor for Student
     * @param userId Student ID (format: U followed by 7 digits and a letter, e.g., U2345123F)
     * @param name Student's full name
     * @param email Student's email address
     * @param password Student's password
     * @param yearOfStudy Year of study (1-4)
     * @param major Student's major
     */
    public Student(String userId, String name, String email, String password, int yearOfStudy, Major major) {
        super(userId, name, email, password);  // UPDATED
        this.yearOfStudy = yearOfStudy;
        this.major = major;
        this.applications = new ArrayList<>();
        this.acceptedInternship = null;
    }
    
    // Rest of the code remains the same...
    // (Keep all the other methods as they were)
    
    public int getYearOfStudy() {
        return yearOfStudy;
    }
    
    public void setYearOfStudy(int yearOfStudy) {
        this.yearOfStudy = yearOfStudy;
    }
    
    public Major getMajor() {
        return major;
    }
    
    public void setMajor(Major major) {
        this.major = major;
    }
    
    public List<InternshipApplication> getApplications() {
        return applications;
    }
    
    public InternshipOpportunity getAcceptedInternship() {
        return acceptedInternship;
    }
    
    public void setAcceptedInternship(InternshipOpportunity acceptedInternship) {
        this.acceptedInternship = acceptedInternship;
    }
    
    public boolean addApplication(InternshipApplication application) {
        if (applications.size() < MAX_APPLICATIONS) {
            applications.add(application);
            return true;
        }
        return false;
    }
    
    public void removeApplication(InternshipApplication application) {
        applications.remove(application);
    }
    
    public boolean canApply() {
        return applications.size() < MAX_APPLICATIONS && acceptedInternship == null;
    }
    
    public int getApplicationCount() {
        return applications.size();
    }
    
    @Override
    public String getUserType() {
        return "STUDENT";
    }
    
    @Override
    public String toString() {
        return super.toString() + ", Year: " + yearOfStudy + ", Major: " + major;
    }
}