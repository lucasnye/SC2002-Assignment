package controller;

import entity.user.Student;
import entity.domain.InternshipOpportunity;
import entity.domain.InternshipApplication;
import entity.domain.WithdrawalRequest;
import enums.ApplicationStatus;
import enums.RequestStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Handles student-specific operations
 */
public class StudentController {
    
    private List<InternshipApplication> applications;
    private List<WithdrawalRequest> withdrawalRequests;
    private InternshipController internshipController;
    private int nextApplicationId;
    private int nextWithdrawalId;
    
    public StudentController(InternshipController internshipController) {
        this.applications = new ArrayList<>();
        this.withdrawalRequests = new ArrayList<>();
        this.internshipController = internshipController;
        this.nextApplicationId = 1;
        this.nextWithdrawalId = 1;
    }
    
    /**
     * Student applies for an internship
     */
    public boolean applyForInternship(Student student, InternshipOpportunity internship) {
        // Check if student can apply (max 3 applications)
        if (!student.canApply()) {
            System.out.println("You have reached the maximum number of applications (3) or already accepted an internship.");
            return false;
        }
        
        // Check if student already applied for this internship
        if (hasAppliedFor(student, internship)) {
            System.out.println("You have already applied for this internship.");
            return false;
        }
        
        // Check eligibility
        if (!internshipController.isEligibleForLevel(student, internship.getLevel())) {
            System.out.println("You are not eligible for this internship level. Year 1-2 can only apply for BASIC level.");
            return false;
        }
        
        // Check if internship is accepting applications
        if (!internship.isAcceptingApplications()) {
            System.out.println("This internship is not currently accepting applications.");
            return false;
        }
        
        // Check major match
        if (internship.getPreferredMajor() != student.getMajor()) {
            System.out.println("This internship is for " + internship.getPreferredMajor() + " major only.");
            return false;
        }
        
        // Create application
        String applicationId = generateApplicationId();
        InternshipApplication application = new InternshipApplication(
            applicationId, student.getUserId(), internship.getOpportunityId()
        );
        
        applications.add(application);
        student.addApplication(application);
        internship.addApplication(application);
        
        System.out.println("Application submitted successfully!");
        return true;
    }
    
    /**
     * Checks if student has already applied for an internship
     */
    private boolean hasAppliedFor(Student student, InternshipOpportunity internship) {
        return applications.stream()
            .anyMatch(app -> app.getStudentId().equals(student.getUserId()) 
                          && app.getOpportunityId().equals(internship.getOpportunityId())
                          && app.getApplicationStatus() != ApplicationStatus.WITHDRAWN);
    }
    
    /**
     * Student accepts an internship placement
     */
    public boolean acceptPlacement(Student student, InternshipApplication application) {
        // Verify application belongs to student
        if (!application.getStudentId().equals(student.getUserId())) {
            System.out.println("This application does not belong to you.");
            return false;
        }
        
        // Check if application is successful
        if (application.getApplicationStatus() != ApplicationStatus.SUCCESSFUL) {
            System.out.println("This application has not been approved yet.");
            return false;
        }
        
        // Check if already confirmed
        if (application.isPlacementConfirmed()) {
            System.out.println("You have already accepted this placement.");
            return false;
        }

        // Find internship and check if slots are still available
        InternshipOpportunity internship = internshipController.findInternshipById(application.getOpportunityId());
        if (internship == null) {
            System.out.println("Internship not found.");
            return false;
        }

        // Check if slots are still available (important for race conditions)
        if (!internship.hasAvailableSlots()) {
            System.out.println("Sorry, all slots for this internship have been filled.");
            System.out.println("Another student has already accepted this position.");
            return false;
        }

        // Confirm placement
        application.confirmPlacement();

        // Update slots
        internship.incrementFilledSlots();
        student.setAcceptedInternship(internship);
        internshipController.updateInternship(); // Save changes to CSV

        // Withdraw all other applications
        withdrawOtherApplications(student, application);
        
        System.out.println("Placement accepted successfully! All other applications have been withdrawn.");
        return true;
    }
    
    /**
     * Withdraws all other applications when student accepts a placement
     */
    private void withdrawOtherApplications(Student student, InternshipApplication acceptedApplication) {
        for (InternshipApplication app : student.getApplications()) {
            if (!app.getApplicationId().equals(acceptedApplication.getApplicationId())) {
                if (app.getApplicationStatus() != ApplicationStatus.WITHDRAWN) {
                    app.withdraw();
                }
            }
        }
    }
    
    /**
     * Student requests withdrawal from an application
     */
    public boolean requestWithdrawal(Student student, InternshipApplication application, String reason) {
        // Verify application belongs to student
        if (!application.getStudentId().equals(student.getUserId())) {
            System.out.println("This application does not belong to you.");
            return false;
        }
        
        // Check if application can be withdrawn
        if (!application.canBeWithdrawn()) {
            System.out.println("This application cannot be withdrawn.");
            return false;
        }
        
        // Check if already has a pending withdrawal request
        boolean hasPendingRequest = withdrawalRequests.stream()
            .anyMatch(wr -> wr.getApplicationId().equals(application.getApplicationId())
                         && wr.getRequestStatus() == RequestStatus.PENDING);
        
        if (hasPendingRequest) {
            System.out.println("You already have a pending withdrawal request for this application.");
            return false;
        }
        
        // Create withdrawal request
        String requestId = generateWithdrawalId();
        boolean isBeforePlacement = !application.isPlacementConfirmed();
        
        WithdrawalRequest request = new WithdrawalRequest(
            requestId, student.getUserId(), application.getApplicationId(), isBeforePlacement
        );
        request.setRemarks(reason);
        
        withdrawalRequests.add(request);
        
        System.out.println("Withdrawal request submitted. Waiting for Career Center approval.");
        return true;
    }
    
    /**
     * Gets all applications for a student
     */
    public List<InternshipApplication> getStudentApplications(Student student) {
        return applications.stream()
            .filter(app -> app.getStudentId().equals(student.getUserId()))
            .collect(Collectors.toList());
    }
    
    /**
     * Gets withdrawal requests for a student
     */
    public List<WithdrawalRequest> getStudentWithdrawalRequests(Student student) {
        return withdrawalRequests.stream()
            .filter(wr -> wr.getStudentId().equals(student.getUserId()))
            .collect(Collectors.toList());
    }
    
    /**
     * Finds an application by ID
     */
    public InternshipApplication findApplicationById(String applicationId) {
        return applications.stream()
            .filter(app -> app.getApplicationId().equals(applicationId))
            .findFirst()
            .orElse(null);
    }
    
    /**
     * Gets all applications
     */
    public List<InternshipApplication> getAllApplications() {
        return new ArrayList<>(applications);
    }
    
    /**
     * Gets all withdrawal requests
     */
    public List<WithdrawalRequest> getAllWithdrawalRequests() {
        return new ArrayList<>(withdrawalRequests);
    }
    
    /**
     * Generates a unique application ID
     */
    private String generateApplicationId() {
        return String.format("APP%04d", nextApplicationId++);
    }
    
    /**
     * Generates a unique withdrawal request ID
     */
    private String generateWithdrawalId() {
        return String.format("WR%04d", nextWithdrawalId++);
    }
}