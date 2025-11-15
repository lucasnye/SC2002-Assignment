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
    public String getApplicationId() {
        return applicationId;
    }
    
    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }
    
    public String getStudentId() {
        return studentId;
    }
    
    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }
    
    public String getOpportunityId() {
        return opportunityId;
    }
    
    public void setOpportunityId(String opportunityId) {
        this.opportunityId = opportunityId;
    }
    
    public ApplicationStatus getApplicationStatus() {
        return applicationStatus;
    }
    
    public void setApplicationStatus(ApplicationStatus applicationStatus) {
        this.applicationStatus = applicationStatus;
    }
    
    public boolean isPlacementConfirmed() {
        return isPlacementConfirmed;
    }
    
    public void setPlacementConfirmed(boolean placementConfirmed) {
        isPlacementConfirmed = placementConfirmed;
    }
    
    public LocalDate getApplicationDate() {
        return applicationDate;
    }
    
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
    
    @Override
    public String toString() {
        return "Application ID: " + applicationId + 
               ", Student: " + studentId + 
               ", Opportunity: " + opportunityId + 
               ", Status: " + applicationStatus + 
               ", Confirmed: " + isPlacementConfirmed;
    }
}