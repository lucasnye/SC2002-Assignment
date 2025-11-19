package controller;

import entity.user.CompanyRepresentative;
import entity.user.Student;
import entity.domain.InternshipOpportunity;
import entity.domain.InternshipApplication;
import entity.domain.WithdrawalRequest;
import enums.InternshipStatus;
import enums.ApplicationStatus;
import enums.RequestStatus;
import enums.InternshipLevel;
import enums.Major;
import util.FileHandler;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Handles career center staff operations
 */
public class CareerCenterController {
    
    private List<CompanyRepresentative> companyReps;
    private InternshipController internshipController;
    private StudentController studentController;
    
    public CareerCenterController(List<CompanyRepresentative> companyReps,
                                  InternshipController internshipController,
                                  StudentController studentController) {
        this.companyReps = companyReps;
        this.internshipController = internshipController;
        this.studentController = studentController;
    }
    
    /**
     * Approves a company representative registration
     */
    public boolean approveRepresentative(CompanyRepresentative rep) {
        if (rep.isRegistrationApproved()) {
            System.out.println("This representative is already approved.");
            return false;
        }
        
        rep.setRegistrationApproved(true);
        System.out.println("Company representative approved: " + rep.getName());
        return true;
    }

    public void updateStatus(List<CompanyRepresentative> companyReps, CompanyRepresentative rep) {
        FileHandler.saveCompanyReps(companyReps, "assets/company_representative_list.csv"); // UPDATED to change status of company rep in csv file
    }
    
    /**
     * Rejects a company representative registration
     */
    public boolean rejectRepresentative(CompanyRepresentative rep) {
        if (rep.isRegistrationApproved()) {
            System.out.println("Cannot reject an already approved representative.");
            return false;
        }
        
        // Remove from list
        companyReps.remove(rep);
        System.out.println("Company representative registration rejected.");
        return true;
    }
    
    /**
     * Gets all pending company representative registrations
     */
    public List<CompanyRepresentative> getPendingRepresentatives() {
        return companyReps.stream()
            .filter(rep -> !rep.isRegistrationApproved())
            .collect(Collectors.toList());
    }
    
    /**
     * Approves an internship opportunity
     */
    public boolean approveInternship(InternshipOpportunity internship) {
        if (internship.getStatus() == InternshipStatus.APPROVED) {
            System.out.println("This internship is already approved.");
            return false;
        }
        
        if (internship.getStatus() == InternshipStatus.REJECTED) {
            System.out.println("Cannot approve a rejected internship.");
            return false;
        }
        
        internship.setStatus(InternshipStatus.APPROVED);
        internshipController.updateInternship(); // Save changes to CSV
        System.out.println("Internship opportunity approved: " + internship.getTitle());
        return true;
    }
    
    /**
     * Rejects an internship opportunity
     */
    public boolean rejectInternship(InternshipOpportunity internship) {
        if (internship.getStatus() == InternshipStatus.APPROVED) {
            System.out.println("Cannot reject an already approved internship.");
            return false;
        }
        
        internship.setStatus(InternshipStatus.REJECTED);
        internshipController.updateInternship(); // Save changes to CSV
        System.out.println("Internship opportunity rejected.");
        return true;
    }
    
    /**
     * Gets all pending internship opportunities
     */
    public List<InternshipOpportunity> getPendingInternships() {
        return internshipController.getAllInternships().stream()
            .filter(i -> i.getStatus() == InternshipStatus.PENDING)
            .collect(Collectors.toList());
    }
    
    /**
     * Approves a withdrawal request
     */
    public boolean approveWithdrawal(WithdrawalRequest request, String remarks) {
        if (request.getRequestStatus() != RequestStatus.PENDING) {
            System.out.println("This request has already been processed.");
            return false;
        }
        
        // Find the application
        InternshipApplication application = studentController.findApplicationById(
            request.getApplicationId()
        );
        
        if (application == null) {
            System.out.println("Application not found.");
            return false;
        }
        
        // Approve the withdrawal
        request.approve(remarks);
        
        // If placement was confirmed, free up the slot
        if (application.isPlacementConfirmed()) {
            InternshipOpportunity internship = internshipController.findInternshipById(
                application.getOpportunityId()
            );
            if (internship != null) {
                internship.decrementFilledSlots();
                internshipController.updateInternship(); // Save changes to CSV
            }
        }

        // Withdraw the application
        application.withdraw();
        
        System.out.println("Withdrawal request approved.");
        return true;
    }
    
    /**
     * Rejects a withdrawal request
     */
    public boolean rejectWithdrawal(WithdrawalRequest request, String remarks) {
        if (request.getRequestStatus() != RequestStatus.PENDING) {
            System.out.println("This request has already been processed.");
            return false;
        }
        
        request.reject(remarks);
        
        System.out.println("Withdrawal request rejected.");
        return true;
    }
    
    /**
     * Gets all pending withdrawal requests
     */
    public List<WithdrawalRequest> getPendingWithdrawalRequests() {
        return studentController.getAllWithdrawalRequests().stream()
            .filter(wr -> wr.getRequestStatus() == RequestStatus.PENDING)
            .collect(Collectors.toList());
    }
    
    /**
     * Generates a report of all internships with optional filters
     */
    public List<InternshipOpportunity> generateReport(InternshipStatus status, 
                                                       Major major, 
                                                       InternshipLevel level) {
        List<InternshipOpportunity> internships = internshipController.getAllInternships();
        
        // Apply filters
        if (status != null) {
            internships = internshipController.filterByStatus(internships, status);
        }
        
        if (major != null) {
            internships = internshipController.filterByMajor(internships, major);
        }
        
        if (level != null) {
            internships = internshipController.filterByLevel(internships, level);
        }
        
        // Sort alphabetically
        internships = internshipController.sortByTitle(internships);
        
        return internships;
    }

    public List<CompanyRepresentative> getCompanyReps() {
        return this.companyReps;
    }
}