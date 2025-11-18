package boundary;

import controller.*;
import entity.user.CompanyRepresentative;
import entity.domain.InternshipOpportunity;
import entity.domain.WithdrawalRequest;
import enums.InternshipStatus;
import enums.InternshipLevel;
import enums.Major;
import util.SessionManager;

import java.util.List;
import java.util.Scanner;

/**
 * Career Center Staff view - Handles staff menu and displays
 */
public class CareerCenterView {
    
    private Scanner scanner;
    private AuthController authController;
    private CareerCenterController careerCenterController;
    private InternshipView internshipView;
    private SessionManager sessionManager;
    
    public CareerCenterView(AuthController authController,
                            CareerCenterController careerCenterController,
                            InternshipView internshipView) {
        this.scanner = new Scanner(System.in);
        this.authController = authController;
        this.careerCenterController = careerCenterController;
        this.internshipView = internshipView;
        this.sessionManager = SessionManager.getInstance();
    }
    
    /**
     * Displays career center staff menu
     */
    public void displayMenu() {
        System.out.println("\n========== CAREER CENTER STAFF MENU ==========");
        System.out.println("Welcome, " + sessionManager.getCurrentUser().getName() + "!");
        System.out.println("1. Approve/Reject Company Representative Registrations");
        System.out.println("2. Approve/Reject Internship Opportunities");
        System.out.println("3. Process Withdrawal Requests");
        System.out.println("4. Generate Internship Report");
        System.out.println("5. Change Password");
        System.out.println("6. Logout");
        System.out.print("Enter choice: ");
        
        String choice = scanner.nextLine().trim();
        
        switch (choice) {
            case "1":
                processRepresentativeRegistrations();
                break;
            case "2":
                processInternshipApprovals();
                break;
            case "3":
                processWithdrawalRequests();
                break;
            case "4":
                generateReport();
                break;
            case "5":
                changePassword();
                break;
            case "6":
                authController.logout();
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }
    
    /**
     * Processes company representative registrations
     */
    private void processRepresentativeRegistrations() {
        System.out.println("\n========== PENDING COMPANY REPRESENTATIVE REGISTRATIONS ==========");
        
        List<CompanyRepresentative> pendingReps = careerCenterController.getPendingRepresentatives();
        
        if (pendingReps.isEmpty()) {
            System.out.println("No pending registrations.");
            return;
        }
        
        for (CompanyRepresentative rep : pendingReps) {
            System.out.println("\n--- Representative ---");
            System.out.println("ID: " + rep.getUserId());
            System.out.println("Name: " + rep.getName());
            System.out.println("Email: " + rep.getEmail());
            System.out.println("Company: " + rep.getCompanyName());
            System.out.println("Department: " + rep.getDepartment());
            System.out.println("Position: " + rep.getPosition());
        }
        
        System.out.print("\nEnter Representative ID to process (or 'cancel'): ");
        String repId = scanner.nextLine().trim();
        
        if (repId.equalsIgnoreCase("cancel")) {
            return;
        }
        
        CompanyRepresentative rep = findRepById(pendingReps, repId);
        
        if (rep == null) {
            System.out.println("Invalid Representative ID.");
            return;
        }
        
        System.out.println("\n1. Approve");
        System.out.println("2. Reject");
        System.out.print("Enter choice: ");
        String choice = scanner.nextLine().trim();
        
        switch (choice) {
            case "1":
                careerCenterController.approveRepresentative(rep);
                careerCenterController.updateStatus(careerCenterController.getCompanyReps(), rep);
                break;
            case "2":
                careerCenterController.rejectRepresentative(rep);
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }
    
    /**
     * Processes internship opportunity approvals
     */
    private void processInternshipApprovals() {
        System.out.println("\n========== PENDING INTERNSHIP OPPORTUNITIES ==========");
        
        List<InternshipOpportunity> pendingInternships = careerCenterController.getPendingInternships();
        
        if (pendingInternships.isEmpty()) {
            System.out.println("No pending internship opportunities.");
            return;
        }
        
        internshipView.displayInternshipList(pendingInternships);
        
        System.out.print("\nEnter Internship ID to process (or 'cancel'): ");
        String internshipId = scanner.nextLine().trim();
        
        if (internshipId.equalsIgnoreCase("cancel")) {
            return;
        }
        
        InternshipOpportunity internship = findInternshipById(pendingInternships, internshipId);
        
        if (internship == null) {
            System.out.println("Invalid Internship ID.");
            return;
        }
        
        internshipView.displayInternshipDetails(internship);
        
        System.out.println("\n1. Approve");
        System.out.println("2. Reject");
        System.out.print("Enter choice: ");
        String choice = scanner.nextLine().trim();
        
        switch (choice) {
            case "1":
                careerCenterController.approveInternship(internship);
                break;
            case "2":
                careerCenterController.rejectInternship(internship);
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }
    
    /**
     * Processes withdrawal requests
     */
    private void processWithdrawalRequests() {
        System.out.println("\n========== PENDING WITHDRAWAL REQUESTS ==========");
        
        List<WithdrawalRequest> pendingRequests = careerCenterController.getPendingWithdrawalRequests();
        
        if (pendingRequests.isEmpty()) {
            System.out.println("No pending withdrawal requests.");
            return;
        }
        
        for (WithdrawalRequest request : pendingRequests) {
            System.out.println("\n--- Withdrawal Request ---");
            System.out.println("Request ID: " + request.getRequestId());
            System.out.println("Student ID: " + request.getStudentId());
            System.out.println("Application ID: " + request.getApplicationId());
            System.out.println("Before Placement: " + (request.isBeforePlacement() ? "Yes" : "No"));
            System.out.println("Request Date: " + request.getRequestDate());
            System.out.println("Reason: " + request.getRemarks());
        }
        
        System.out.print("\nEnter Request ID to process (or 'cancel'): ");
        String requestId = scanner.nextLine().trim();
        
        if (requestId.equalsIgnoreCase("cancel")) {
            return;
        }
        
        WithdrawalRequest request = findRequestById(pendingRequests, requestId);
        
        if (request == null) {
            System.out.println("Invalid Request ID.");
            return;
        }
        
        System.out.println("\n1. Approve");
        System.out.println("2. Reject");
        System.out.print("Enter choice: ");
        String choice = scanner.nextLine().trim();
        
        System.out.print("Remarks: ");
        String remarks = scanner.nextLine().trim();
        
        switch (choice) {
            case "1":
                careerCenterController.approveWithdrawal(request, remarks);
                break;
            case "2":
                careerCenterController.rejectWithdrawal(request, remarks);
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }
    
    /**
     * Generates internship report with filters
     */
    private void generateReport() {
        System.out.println("\n========== GENERATE INTERNSHIP REPORT ==========");
        
        System.out.println("Apply filters (leave blank to skip):");
        
        // Status filter
        System.out.println("\nFilter by Status:");
        System.out.println("1. PENDING");
        System.out.println("2. APPROVED");
        System.out.println("3. REJECTED");
        System.out.println("4. FILLED");
        System.out.println("(Press Enter to skip)");
        System.out.print("Enter choice: ");
        String statusChoice = scanner.nextLine().trim();
        
        InternshipStatus status = null;
        if (!statusChoice.isEmpty()) {
            switch (statusChoice) {
                case "1":
                    status = InternshipStatus.PENDING;
                    break;
                case "2":
                    status = InternshipStatus.APPROVED;
                    break;
                case "3":
                    status = InternshipStatus.REJECTED;
                    break;
                case "4":
                    status = InternshipStatus.FILLED;
                    break;
            }
        }
        
        // Major filter
        System.out.println("\nFilter by Major:");
        Major[] majors = Major.values();
        for (int i = 0; i < majors.length; i++) {
            System.out.println((i + 1) + ". " + majors[i].getFullName());
        }
        System.out.println("(Press Enter to skip)");
        System.out.print("Enter choice: ");
        String majorChoice = scanner.nextLine().trim();
        
        Major major = null;
        if (!majorChoice.isEmpty()) {
            try {
                int majorIndex = Integer.parseInt(majorChoice) - 1;
                if (majorIndex >= 0 && majorIndex < majors.length) {
                    major = majors[majorIndex];
                }
            } catch (NumberFormatException e) {
                // Skip filter
            }
        }
        
        // Level filter
        System.out.println("\nFilter by Level:");
        System.out.println("1. BASIC");
        System.out.println("2. INTERMEDIATE");
        System.out.println("3. ADVANCED");
        System.out.println("(Press Enter to skip)");
        System.out.print("Enter choice: ");
        String levelChoice = scanner.nextLine().trim();
        
        InternshipLevel level = null;
        if (!levelChoice.isEmpty()) {
            switch (levelChoice) {
                case "1":
                    level = InternshipLevel.BASIC;
                    break;
                case "2":
                    level = InternshipLevel.INTERMEDIATE;
                    break;
                case "3":
                    level = InternshipLevel.ADVANCED;
                    break;
            }
        }
        
        // Generate report
        List<InternshipOpportunity> report = careerCenterController.generateReport(status, major, level);
        
        System.out.println("\n========== REPORT RESULTS ==========");
        internshipView.displayInternshipList(report);
    }
    
    /**
     * Handles password change
     */
    private void changePassword() {
        System.out.println("\n========== CHANGE PASSWORD ==========");
        System.out.print("Enter old password: ");
        String oldPassword = scanner.nextLine().trim();
        
        System.out.print("Enter new password: ");
        String newPassword = scanner.nextLine().trim();
        
        System.out.print("Confirm new password: ");
        String confirmPassword = scanner.nextLine().trim();
        
        if (!newPassword.equals(confirmPassword)) {
            System.out.println("Passwords do not match!");
            return;
        }
        
        if (authController.changePassword(oldPassword, newPassword)) {
            System.out.println("Password changed successfully! Please login again.");
            authController.logout();
        }
    }
    
    /**
     * Helper to find representative by ID
     */
    private CompanyRepresentative findRepById(List<CompanyRepresentative> reps, String repId) {
        return reps.stream()
            .filter(rep -> rep.getUserId().equals(repId))
            .findFirst()
            .orElse(null);
    }
    
    /**
     * Helper to find internship by ID
     */
    private InternshipOpportunity findInternshipById(List<InternshipOpportunity> internships, String id) {
        return internships.stream()
            .filter(i -> i.getOpportunityId().equals(id))
            .findFirst()
            .orElse(null);
    }
    
    /**
     * Helper to find withdrawal request by ID
     */
    private WithdrawalRequest findRequestById(List<WithdrawalRequest> requests, String id) {
        return requests.stream()
            .filter(r -> r.getRequestId().equals(id))
            .findFirst()
            .orElse(null);
    }
}