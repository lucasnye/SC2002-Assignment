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
    
    
    /**
     * Returns year of study
     */
    public int getYearOfStudy() {
        return yearOfStudy;
    }
    
    /**
     * Sets year of study
     */
    public void setYearOfStudy(int yearOfStudy) {
        this.yearOfStudy = yearOfStudy;
    }
    
    /**
     * Returns major
     */
    public Major getMajor() {
        return major;
    }
    
    /**
     * Sets major
     */
    public void setMajor(Major major) {
        this.major = major;
    }
    
    /**
     * Returns applications
     */
    public List<InternshipApplication> getApplications() {
        return applications;
    }
    
    /**
     * Returns accepted applications
     */
    public InternshipOpportunity getAcceptedInternship() {
        return acceptedInternship;
    }
    
    /**
     * Sets internship acceptance status
     */
    public void setAcceptedInternship(InternshipOpportunity acceptedInternship) {
        this.acceptedInternship = acceptedInternship;
    }
    
    /**
     * Returns if application successfully added
     */
    public boolean addApplication(InternshipApplication application) {
        if (applications.size() < MAX_APPLICATIONS) {
            applications.add(application);
            return true;
        }
        return false;
    }
    
    /**
     * Removes application
     */
    public void removeApplication(InternshipApplication application) {
        applications.remove(application);
    }
    
    /**
     * Checks if student can apply for internship
     */
    public boolean canApply() {
        return applications.size() < MAX_APPLICATIONS && acceptedInternship == null;
    }
    
    /**
     * Returns number of applications submitted
     */
    public int getApplicationCount() {
        return applications.size();
    }
    
    /**
     * Returns user type (student)
     */
    @Override
    public String getUserType() {
        return "STUDENT";
    }
    
    /**
     * Prints student information
     */
    @Override
    public String toString() {
        return super.toString() + ", Year: " + yearOfStudy + ", Major: " + major;
    }
}