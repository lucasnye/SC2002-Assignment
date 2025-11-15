package entity.domain;

import enums.InternshipLevel;
import enums.InternshipStatus;
import enums.Major;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents an internship opportunity in the system
 */
public class InternshipOpportunity implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final int MAX_SLOTS = 10;
    
    private String opportunityId;
    private String title;
    private String description;
    private InternshipLevel level;
    private Major preferredMajor;
    private LocalDate openingDate;
    private LocalDate closingDate;
    private InternshipStatus status;
    private String companyName;
    private String assignedRepresentativeId;
    private int totalSlots;
    private int filledSlots;
    private boolean isVisible;
    private List<InternshipApplication> applications;
    
    /**
     * Constructor for InternshipOpportunity
     */
    public InternshipOpportunity(String opportunityId, String title, String description,
                                  InternshipLevel level, Major preferredMajor,
                                  LocalDate openingDate, LocalDate closingDate,
                                  String companyName, String assignedRepresentativeId,
                                  int totalSlots) {
        this.opportunityId = opportunityId;
        this.title = title;
        this.description = description;
        this.level = level;
        this.preferredMajor = preferredMajor;
        this.openingDate = openingDate;
        this.closingDate = closingDate;
        this.status = InternshipStatus.PENDING;
        this.companyName = companyName;
        this.assignedRepresentativeId = assignedRepresentativeId;
        this.totalSlots = Math.min(totalSlots, MAX_SLOTS);
        this.filledSlots = 0;
        this.isVisible = false;
        this.applications = new ArrayList<>();
    }
    
    // Getters and Setters
    public String getOpportunityId() {
        return opportunityId;
    }
    
    public void setOpportunityId(String opportunityId) {
        this.opportunityId = opportunityId;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public InternshipLevel getLevel() {
        return level;
    }
    
    public void setLevel(InternshipLevel level) {
        this.level = level;
    }
    
    public Major getPreferredMajor() {
        return preferredMajor;
    }
    
    public void setPreferredMajor(Major preferredMajor) {
        this.preferredMajor = preferredMajor;
    }
    
    public LocalDate getOpeningDate() {
        return openingDate;
    }
    
    public void setOpeningDate(LocalDate openingDate) {
        this.openingDate = openingDate;
    }
    
    public LocalDate getClosingDate() {
        return closingDate;
    }
    
    public void setClosingDate(LocalDate closingDate) {
        this.closingDate = closingDate;
    }
    
    public InternshipStatus getStatus() {
        return status;
    }
    
    public void setStatus(InternshipStatus status) {
        this.status = status;
    }
    
    public String getCompanyName() {
        return companyName;
    }
    
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
    
    public String getAssignedRepresentativeId() {
        return assignedRepresentativeId;
    }
    
    public void setAssignedRepresentativeId(String assignedRepresentativeId) {
        this.assignedRepresentativeId = assignedRepresentativeId;
    }
    
    public int getTotalSlots() {
        return totalSlots;
    }
    
    public void setTotalSlots(int totalSlots) {
        this.totalSlots = Math.min(totalSlots, MAX_SLOTS);
    }
    
    public int getFilledSlots() {
        return filledSlots;
    }
    
    public void setFilledSlots(int filledSlots) {
        this.filledSlots = filledSlots;
    }
    
    public boolean isVisible() {
        return isVisible;
    }
    
    public void setVisible(boolean visible) {
        isVisible = visible;
    }
    
    public List<InternshipApplication> getApplications() {
        return applications;
    }
    
    /**
     * Adds an application to this internship opportunity
     */
    public void addApplication(InternshipApplication application) {
        applications.add(application);
    }
    
    /**
     * Removes an application from this internship opportunity
     */
    public void removeApplication(InternshipApplication application) {
        applications.remove(application);
    }
    
    /**
     * Checks if there are available slots
     */
    public boolean hasAvailableSlots() {
        return filledSlots < totalSlots;
    }
    
    /**
     * Increments filled slots when a student confirms placement
     */
    public void incrementFilledSlots() {
        if (filledSlots < totalSlots) {
            filledSlots++;
            if (filledSlots == totalSlots) {
                status = InternshipStatus.FILLED;
            }
        }
    }
    
    /**
     * Decrements filled slots when a placement is withdrawn
     */
    public void decrementFilledSlots() {
        if (filledSlots > 0) {
            filledSlots--;
            if (status == InternshipStatus.FILLED) {
                status = InternshipStatus.APPROVED;
            }
        }
    }
    
    /**
     * Checks if the internship is accepting applications
     */
    public boolean isAcceptingApplications() {
        LocalDate today = LocalDate.now();
        return status == InternshipStatus.APPROVED 
               && isVisible 
               && hasAvailableSlots()
               && !today.isBefore(openingDate)
               && !today.isAfter(closingDate);
    }
    
    @Override
    public String toString() {
        return "ID: " + opportunityId + ", Title: " + title + 
               ", Company: " + companyName + ", Level: " + level + 
               ", Status: " + status + ", Slots: " + filledSlots + "/" + totalSlots;
    }
}