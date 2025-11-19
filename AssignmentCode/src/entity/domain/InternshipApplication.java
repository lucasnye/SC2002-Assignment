package entity.domain;

import enums.ApplicationStatus;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Represents a student's application to an internship opportunity
 */
public class InternshipApplication implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String applicationId;
    private String studentId;
    private String opportunityId;
    private ApplicationStatus applicationStatus;
    private boolean isPlacementConfirmed;
    private LocalDate applicationDate;
    
    /**
     * Constructor for InternshipApplication
     */
    public InternshipApplication(String applicationId, String studentId, String opportunityId) {
        this.applicationId = applicationId;
        this.studentId = studentId;
        this.opportunityId = opportunityId;
        this.applicationStatus = ApplicationStatus.PENDING;
        this.isPlacementConfirmed = false;
        this.applicationDate = LocalDate.now();
    }
    
    // Getters and Setters
    /**
     * Returns application ID
     */
    public String getApplicationId() {
        return applicationId;
    }
    
    /**
     * Sets application ID
     */
    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }
    
    /**
     * Returns student ID
     */
    public String getStudentId() {
        return studentId;
    }
    
    /**
     * Sets student ID
     */
    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }
    
    /**
     * Returns internship ID
     */
    public String getOpportunityId() {
        return opportunityId;
    }
    
    /**
     * Sets internship ID
     */
    public void setOpportunityId(String opportunityId) {
        this.opportunityId = opportunityId;
    }
    
    /**
     * Returns application status
     */
    public ApplicationStatus getApplicationStatus() {
        return applicationStatus;
    }
    
    /**
     * Sets application ID
     */
    public void setApplicationStatus(ApplicationStatus applicationStatus) {
        this.applicationStatus = applicationStatus;
    }
    
    /**
     * Checks if student is placed in internship
     */
    public boolean isPlacementConfirmed() {
        return isPlacementConfirmed;
    }
    
    /**
     * Sets internship placement status
     */
    public void setPlacementConfirmed(boolean placementConfirmed) {
        isPlacementConfirmed = placementConfirmed;
    }
    
    /**
     * Get application date
     */
    public LocalDate getApplicationDate() {
        return applicationDate;
    }
    
    /**
     * Set application date
     */
    public void setApplicationDate(LocalDate applicationDate) {
        this.applicationDate = applicationDate;
    }
    
    /**
     * Confirms the placement for this application
     */
    public void confirmPlacement() {
        if (applicationStatus == ApplicationStatus.SUCCESSFUL) {
            isPlacementConfirmed = true;
        }
    }
    
    /**
     * Withdraws the application
     */
    public void withdraw() {
        applicationStatus = ApplicationStatus.WITHDRAWN;
    }
    
    /**
     * Checks if the application can be withdrawn
     */
    public boolean canBeWithdrawn() {
        return applicationStatus == ApplicationStatus.PENDING 
               || applicationStatus == ApplicationStatus.SUCCESSFUL;
    }
    
    /**
     * Print function to display application info
     */
    @Override
    public String toString() {
        return "Application ID: " + applicationId + 
               ", Student: " + studentId + 
               ", Opportunity: " + opportunityId + 
               ", Status: " + applicationStatus + 
               ", Confirmed: " + isPlacementConfirmed;
    }
}