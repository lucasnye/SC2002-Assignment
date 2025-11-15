package boundary;

import controller.*;
import entity.user.Student;
import entity.domain.InternshipOpportunity;
import entity.domain.InternshipApplication;
import enums.ApplicationStatus;
import util.SessionManager;

import java.util.List;
import java.util.Scanner;

/**
 * Student view - Handles student menu and displays
 */
public class StudentView {
    
    private Scanner scanner;
    private AuthController authController;
    private StudentController studentController;
    private InternshipController internshipController;
    private InternshipView internshipView;
    private SessionManager sessionManager;
    
    public StudentView(AuthController authController,
                       StudentController studentController,
                       InternshipController internshipController,
                       InternshipView internshipView) {
        this.scanner = new Scanner(System.in);
        this.authController = authController;
        this.studentController = studentController;
        this.internshipController = internshipController;
        this.internshipView = internshipView;
        this.sessionManager = SessionManager.getInstance();
    }
    
    /**
     * Displays student menu
     */
    public void displayMenu() {
        Student student = (Student) sessionManager.getCurrentUser();
        
        System.out.println("\n========== STUDENT MENU ==========");
        System.out.println("Welcome, " + student.getName() + "!");
        System.out.println("1. View Available Internships");
        System.out.println("2. Apply for Internship");
        System.out.println("3. View My Applications");
        System.out.println("4. Accept Placement");
        System.out.println("5. Request Withdrawal");
        System.out.println("6. Change Password");
        System.out.println("7. Logout");
        System.out.print("Enter choice: ");
        
        String choice = scanner.nextLine().trim();
        
        switch (choice) {
            case "1":
                viewAvailableInternships(student);
                break;
            case "2":
                applyForInternship(student);
                break;
            case "3":
                viewMyApplications(student);
                break;
            case "4":
                acceptPlacement(student);
                break;
            case "5":
                requestWithdrawal(student);
                break;
            case "6":
                changePassword();
                break;
            case "7":
                authController.logout();
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }
    
    /**
     * Displays available internships for the student
     */
    private void viewAvailableInternships(Student student) {
        System.out.println("\n========== AVAILABLE INTERNSHIPS ==========");
        List<InternshipOpportunity> internships = internshipController.getInternshipsForStudent(student);
        
        if (internships.isEmpty()) {
            System.out.println("No internships available for your profile.");
            return;
        }
        
        internshipView.displayInternshipList(internships);
    }
    
    /**
     * Handles internship application
     */
    private void applyForInternship(Student student) {
        System.out.println("\n========== APPLY FOR INTERNSHIP ==========");
        
        // Check if student can apply
        if (!student.canApply()) {
            System.out.println("You cannot apply for more internships.");
            System.out.println("Reason: Already have 3 applications or accepted an internship.");
            return;
        }
        
        // Show available internships
        List<InternshipOpportunity> internships = internshipController.getInternshipsForStudent(student);
        
        if (internships.isEmpty()) {
            System.out.println("No internships available for you to apply.");
            return;
        }
        
        internshipView.displayInternshipList(internships);
        
        System.out.print("\nEnter Internship ID to apply (or 'cancel' to go back): ");
        String internshipId = scanner.nextLine().trim();
        
        if (internshipId.equalsIgnoreCase("cancel")) {
            return;
        }
        
        InternshipOpportunity internship = internshipController.findInternshipById(internshipId);
        
        if (internship == null) {
            System.out.println("Invalid Internship ID.");
            return;
        }
        
        studentController.applyForInternship(student, internship);
    }
    
    /**
     * Displays student's applications
     */
    private void viewMyApplications(Student student) {
        System.out.println("\n========== MY APPLICATIONS ==========");
        List<InternshipApplication> applications = studentController.getStudentApplications(student);
        
        if (applications.isEmpty()) {
            System.out.println("You have no applications yet.");
            return;
        }
        
        for (InternshipApplication app : applications) {
            InternshipOpportunity internship = internshipController.findInternshipById(app.getOpportunityId());
            
            System.out.println("\n--- Application " + app.getApplicationId() + " ---");
            if (internship != null) {
                System.out.println("Internship: " + internship.getTitle());
                System.out.println("Company: " + internship.getCompanyName());
            }
            System.out.println("Status: " + app.getApplicationStatus().getDisplayName());
            System.out.println("Applied on: " + app.getApplicationDate());
            System.out.println("Placement Confirmed: " + (app.isPlacementConfirmed() ? "Yes" : "No"));
        }
    }
    
    /**
     * Handles placement acceptance
     */
    private void acceptPlacement(Student student) {
        System.out.println("\n========== ACCEPT PLACEMENT ==========");
        
        List<InternshipApplication> applications = studentController.getStudentApplications(student);
        List<InternshipApplication> successfulApps = applications.stream()
            .filter(app -> app.getApplicationStatus() == ApplicationStatus.SUCCESSFUL 
                        && !app.isPlacementConfirmed())
            .toList();
        
        if (successfulApps.isEmpty()) {
            System.out.println("You have no approved applications to accept.");
            return;
        }
        
        System.out.println("Approved Applications:");
        for (InternshipApplication app : successfulApps) {
            InternshipOpportunity internship = internshipController.findInternshipById(app.getOpportunityId());
            System.out.println("\nApplication ID: " + app.getApplicationId());
            if (internship != null) {
                System.out.println("Internship: " + internship.getTitle());
                System.out.println("Company: " + internship.getCompanyName());
            }
        }
        
        System.out.print("\nEnter Application ID to accept (or 'cancel'): ");
        String appId = scanner.nextLine().trim();
        
        if (appId.equalsIgnoreCase("cancel")) {
            return;
        }
        
        InternshipApplication application = studentController.findApplicationById(appId);
        
        if (application == null) {
            System.out.println("Invalid Application ID.");
            return;
        }
        
        System.out.println("WARNING: Accepting this placement will withdraw all your other applications.");
        System.out.print("Are you sure? (yes/no): ");
        String confirm = scanner.nextLine().trim();
        
        if (confirm.equalsIgnoreCase("yes")) {
            studentController.acceptPlacement(student, application);
        } else {
            System.out.println("Placement acceptance cancelled.");
        }
    }
    
    /**
     * Handles withdrawal request
     */
    private void requestWithdrawal(Student student) {
        System.out.println("\n========== REQUEST WITHDRAWAL ==========");
        
        List<InternshipApplication> applications = studentController.getStudentApplications(student);
        List<InternshipApplication> withdrawableApps = applications.stream()
            .filter(InternshipApplication::canBeWithdrawn)
            .toList();
        
        if (withdrawableApps.isEmpty()) {
            System.out.println("You have no applications that can be withdrawn.");
            return;
        }
        
        System.out.println("Applications that can be withdrawn:");
        for (InternshipApplication app : withdrawableApps) {
            InternshipOpportunity internship = internshipController.findInternshipById(app.getOpportunityId());
            System.out.println("\nApplication ID: " + app.getApplicationId());
            if (internship != null) {
                System.out.println("Internship: " + internship.getTitle());
            }
            System.out.println("Status: " + app.getApplicationStatus().getDisplayName());
            System.out.println("Confirmed: " + (app.isPlacementConfirmed() ? "Yes" : "No"));
        }
        
        System.out.print("\nEnter Application ID to withdraw (or 'cancel'): ");
        String appId = scanner.nextLine().trim();
        
        if (appId.equalsIgnoreCase("cancel")) {
            return;
        }
        
        InternshipApplication application = studentController.findApplicationById(appId);
        
        if (application == null) {
            System.out.println("Invalid Application ID.");
            return;
        }
        
        System.out.print("Reason for withdrawal: ");
        String reason = scanner.nextLine().trim();
        
        studentController.requestWithdrawal(student, application, reason);
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
}