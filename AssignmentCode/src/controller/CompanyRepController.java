package controller;

import entity.user.CompanyRepresentative;
import entity.domain.InternshipOpportunity;
import entity.domain.InternshipApplication;
import enums.ApplicationStatus;
import enums.InternshipStatus;

import java.time.LocalDate;
import java.util.List;

/**
 * Handles company representative operations
 */
public class CompanyRepController {
    
    private InternshipController internshipController;
    private StudentController studentController;
    
    public CompanyRepController(InternshipController internshipController, 
                                StudentController studentController) {
        this.internshipController = internshipController;
        this.studentController = studentController;
    }
    
    /**
     * Company rep creates a new internship opportunity
     */
    public boolean createInternship(CompanyRepresentative rep, 
                                    String title, String description,
                                    enums.InternshipLevel level, enums.Major preferredMajor,
                                    LocalDate openingDate, LocalDate closingDate, int totalSlots) {
        
        // Check if rep can create more opportunities (max 5)
        if (!rep.canCreateOpportunity()) {
            System.out.println("You have reached the maximum number of internship opportunities (5).");
            return false;
        }
        
        // Validate dates
        if (!util.ValidationUtil.isValidDateRange(openingDate, closingDate)) {
            System.out.println("Invalid date range. Opening date must be before closing date.");
            return false;
        }
        
        // Create internship
        InternshipOpportunity internship = internshipController.createInternship(
            title, description, level, preferredMajor, openingDate, closingDate,
            rep.getCompanyName(), rep.getUserId(), totalSlots
        );
        
        rep.addOpportunity(internship);
        
        System.out.println("Internship opportunity created! Waiting for Career Center approval.");
        return true;
    }
    
    /**
     * Company rep edits an existing internship (only if not approved yet)
     */
    public boolean editInternship(CompanyRepresentative rep, InternshipOpportunity internship,
                                  String title, String description, int totalSlots) {
        
        // Verify ownership
        if (!internship.getAssignedRepresentativeId().equals(rep.getUserId())) {
            System.out.println("You can only edit your own internship opportunities.");
            return false;
        }
        
        // Check if already approved
        if (internship.getStatus() == InternshipStatus.APPROVED || 
            internship.getStatus() == InternshipStatus.FILLED) {
            System.out.println("Cannot edit an approved internship opportunity.");
            return false;
        }
        
        // Update details
        internship.setTitle(title);
        internship.setDescription(description);
        internship.setTotalSlots(totalSlots);

        // If the internship was rejected, reset status to PENDING for re-approval
        if (internship.getStatus() == InternshipStatus.REJECTED) {
            internship.setStatus(InternshipStatus.PENDING);
            System.out.println("Internship opportunity updated successfully. Status reset to PENDING for Career Center re-approval.");
        } else {
            System.out.println("Internship opportunity updated successfully.");
        }

        internshipController.updateInternship(); // Save changes to CSV

        return true;
    }
    
    /**
     * Company rep deletes an internship (only if pending)
     */
    public boolean deleteInternship(CompanyRepresentative rep, InternshipOpportunity internship) {
        
        // Verify ownership
        if (!internship.getAssignedRepresentativeId().equals(rep.getUserId())) {
            System.out.println("You can only delete your own internship opportunities.");
            return false;
        }
        
        // Check if has applications
        if (!internship.getApplications().isEmpty()) {
            System.out.println("Cannot delete an internship with existing applications.");
            return false;
        }

        // Check status - can only delete PENDING or REJECTED internships
        if (internship.getStatus() != InternshipStatus.PENDING && internship.getStatus() != InternshipStatus.REJECTED) {
            System.out.println("Can only delete pending or rejected internship opportunities.");
            return false;
        }
        
        // Remove from rep's list and system
        rep.removeOpportunity(internship);
        internshipController.removeInternship(internship);
        
        System.out.println("Internship opportunity deleted successfully.");
        return true;
    }
    
    /**
     * Company rep views applications for their internship
     */
    public List<InternshipApplication> viewApplications(CompanyRepresentative rep, 
                                                         InternshipOpportunity internship) {
        
        // Verify ownership
        if (!internship.getAssignedRepresentativeId().equals(rep.getUserId())) {
            System.out.println("You can only view applications for your own internships.");
            return List.of();
        }
        
        return internship.getApplications();
    }
    
    /**
     * Company rep approves a student application
     */
    public boolean approveApplication(CompanyRepresentative rep, InternshipApplication application) {
        
        // Find the internship
        InternshipOpportunity internship = internshipController.findInternshipById(
            application.getOpportunityId()
        );
        
        if (internship == null) {
            System.out.println("Internship not found.");
            return false;
        }
        
        // Verify ownership
        if (!internship.getAssignedRepresentativeId().equals(rep.getUserId())) {
            System.out.println("You can only approve applications for your own internships.");
            return false;
        }
        
        // Check if application is pending
        if (application.getApplicationStatus() != ApplicationStatus.PENDING) {
            System.out.println("This application has already been processed.");
            return false;
        }
        
        // Check if slots available
        if (!internship.hasAvailableSlots()) {
            System.out.println("No available slots for this internship.");
            return false;
        }
        
        // Approve application
        application.setApplicationStatus(ApplicationStatus.SUCCESSFUL);
        
        System.out.println("Application approved successfully!");
        return true;
    }
    
    /**
     * Company rep rejects a student application
     */
    public boolean rejectApplication(CompanyRepresentative rep, InternshipApplication application) {
        
        // Find the internship
        InternshipOpportunity internship = internshipController.findInternshipById(
            application.getOpportunityId()
        );
        
        if (internship == null) {
            System.out.println("Internship not found.");
            return false;
        }
        
        // Verify ownership
        if (!internship.getAssignedRepresentativeId().equals(rep.getUserId())) {
            System.out.println("You can only reject applications for your own internships.");
            return false;
        }
        
        // Check if application is pending
        if (application.getApplicationStatus() != ApplicationStatus.PENDING) {
            System.out.println("This application has already been processed.");
            return false;
        }
        
        // Reject application
        application.setApplicationStatus(ApplicationStatus.UNSUCCESSFUL);
        
        System.out.println("Application rejected.");
        return true;
    }
    
    /**
     * Company rep toggles visibility of an internship
     */
    public boolean toggleVisibility(CompanyRepresentative rep, InternshipOpportunity internship) {
        
        // Verify ownership
        if (!internship.getAssignedRepresentativeId().equals(rep.getUserId())) {
            System.out.println("You can only toggle visibility for your own internships.");
            return false;
        }
        
        // Toggle visibility
        internship.setVisible(!internship.isVisible());

        internshipController.updateInternship(); // Save changes to CSV

        String status = internship.isVisible() ? "visible" : "hidden";
        System.out.println("Internship is now " + status + " to students.");
        return true;
    }
    
    /**
     * Gets all internships created by a company rep
     */
    public List<InternshipOpportunity> getRepInternships(CompanyRepresentative rep) {
        return internshipController.getInternshipsByRep(rep.getUserId());
    }
}