package boundary;

import controller.*;
import entity.user.CompanyRepresentative;
import entity.domain.InternshipOpportunity;
import entity.domain.InternshipApplication;
import enums.InternshipLevel;
import enums.InternshipStatus;
import enums.Major;
import util.SessionManager;
import util.ValidationUtil;
import util.FileHandler;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

/**
 * Company Representative view - Handles company rep menu and displays
 */
public class CompanyRepView {
    
    private Scanner scanner;
    private AuthController authController;
    private CompanyRepController companyRepController;
    private InternshipController internshipController;
    private InternshipView internshipView;
    private SessionManager sessionManager;
    private List<CompanyRepresentative> companyReps;
    
    public CompanyRepView(AuthController authController,
                          CompanyRepController companyRepController,
                          InternshipController internshipController,
                          InternshipView internshipView,
                          List<CompanyRepresentative> companyReps) {
        this.scanner = new Scanner(System.in);
        this.authController = authController;
        this.companyRepController = companyRepController;
        this.internshipController = internshipController;
        this.internshipView = internshipView;
        this.sessionManager = SessionManager.getInstance();
        this.companyReps = companyReps;
    }
    
    /**
     * Displays company representative menu
     */
    public void displayMenu() {
        CompanyRepresentative rep = (CompanyRepresentative) sessionManager.getCurrentUser();
        
        System.out.println("\n========== COMPANY REPRESENTATIVE MENU ==========");
        System.out.println("Welcome, " + rep.getName() + " (" + rep.getCompanyName() + ")");
        System.out.println("1. Create Internship Opportunity");
        System.out.println("2. View My Internship Opportunities");
        System.out.println("3. Edit Internship Opportunity");
        System.out.println("4. Delete Internship Opportunity");
        System.out.println("5. View Applications for My Internships");
        System.out.println("6. Approve/Reject Application");
        System.out.println("7. Toggle Internship Visibility");
        System.out.println("8. Change Password");
        System.out.println("9. Logout");
        System.out.print("Enter choice: ");
        
        String choice = scanner.nextLine().trim();
        
        switch (choice) {
            case "1":
                createInternship(rep);
                break;
            case "2":
                viewMyInternships(rep);
                break;
            case "3":
                editInternship(rep);
                break;
            case "4":
                deleteInternship(rep);
                break;
            case "5":
                viewApplications(rep);
                break;
            case "6":
                processApplication(rep);
                break;
            case "7":
                toggleVisibility(rep);
                break;
            case "8":
                changePassword();
                break;
            case "9":
                authController.logout();
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }
    
    /**
     * Handles registration of new company representative
     */
    public void registerNewRep(String name, String email, String companyName, 
                               String department, String position) {
        
        if (!ValidationUtil.isValidEmail(email)) {
            System.out.println("Invalid email format.");
            return;
        }
        
        if (!ValidationUtil.isNotEmpty(name) || !ValidationUtil.isNotEmpty(companyName) 
            || !ValidationUtil.isNotEmpty(department) || !ValidationUtil.isNotEmpty(position)) {
            System.out.println("All fields are required.");
            return;
        }
        
        // Generate new ID
        String repId = FileHandler.generateCompanyRepId(companyReps);
        
        // Create new representative (not approved yet)
        CompanyRepresentative newRep = new CompanyRepresentative(
            repId, name, email, "password", companyName, department, position
        );
        
        companyReps.add(newRep);
        
        System.out.println("\n=== Registration Successful ===");
        System.out.println("Your Company Representative ID: " + repId);
        System.out.println("Default Password: password");
        System.out.println("Your account is pending approval by Career Center Staff.");
        System.out.println("You will be able to login once approved.");
        
        // Save to file
        FileHandler.saveCompanyReps(companyReps, "assets/company_representative_list.csv");
    }
    
    /**
     * Handles internship creation
     */
    private void createInternship(CompanyRepresentative rep) {
        System.out.println("\n========== CREATE INTERNSHIP OPPORTUNITY ==========");
        
        if (!rep.canCreateOpportunity()) {
            System.out.println("You have reached the maximum limit of 5 internship opportunities.");
            return;
        }
        
        System.out.print("Internship Title: ");
        String title = scanner.nextLine().trim();
        
        System.out.print("Description: ");
        String description = scanner.nextLine().trim();
        
        // Select level
        System.out.println("\nInternship Level:");
        System.out.println("1. BASIC");
        System.out.println("2. INTERMEDIATE");
        System.out.println("3. ADVANCED");

        InternshipLevel level = null;
        do {
            System.out.print("Enter choice: ");
            String levelChoice = scanner.nextLine().trim();
            
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
                default:
                    System.out.println("Invalid level. Try again.");
            }
        } while (level == null);

        
        // Select major
        System.out.println("\nPreferred Major:");
        Major[] majors = Major.values();
        for (int i = 0; i < majors.length; i++) {
            System.out.println((i + 1) + ". " + majors[i].getFullName());
        }
        
        Major preferredMajor = null;
        do {
            System.out.print("Enter choice: ");
            String majorChoice = scanner.nextLine().trim();
            try {
                int majorIndex = Integer.parseInt(majorChoice) - 1;
                if (majorIndex >= 0 && majorIndex < majors.length) {
                    preferredMajor = majors[majorIndex];
                } else {
                    System.out.println("Invalid major. Try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Try again.");
            }
        } while (preferredMajor == null);

        
        // Dates
        System.out.print("Opening Date (yyyy-MM-dd): ");
        String openingDateStr = scanner.nextLine().trim();
        LocalDate openingDate = ValidationUtil.parseDate(openingDateStr);
        
        if (openingDate == null) {
            System.out.println("Invalid date format.");
            return;
        }
        
        System.out.print("Closing Date (yyyy-MM-dd): ");
        String closingDateStr = scanner.nextLine().trim();
        LocalDate closingDate = ValidationUtil.parseDate(closingDateStr);
        
        if (closingDate == null) {
            System.out.println("Invalid date format.");
            return;
        }
        
        // Slots
        int slots = 0;
        do {
            System.out.print("Number of Slots (1-10): ");
            try {
                slots = Integer.parseInt(scanner.nextLine().trim());
                if (!ValidationUtil.isWithinRange(slots, 1, 10)) {
                    System.out.println("Slots must be between 1 and 10. Try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid number. Try again.");
            }
        } while (!ValidationUtil.isWithinRange(slots, 1, 10));
        
        companyRepController.createInternship(rep, title, description, level, 
                                             preferredMajor, openingDate, closingDate, slots);
    }
    
    /**
     * Displays company rep's internships
     */
    private void viewMyInternships(CompanyRepresentative rep) {
        System.out.println("\n========== MY INTERNSHIP OPPORTUNITIES ==========");
        List<InternshipOpportunity> internships = companyRepController.getRepInternships(rep);
        
        if (internships.isEmpty()) {
            System.out.println("You have not created any internship opportunities yet.");
            return;
        }
        
        internshipView.displayInternshipList(internships);
    }
    
    /**
     * Handles internship editing
     */
    private void editInternship(CompanyRepresentative rep) {
        System.out.println("\n========== EDIT INTERNSHIP OPPORTUNITY ==========");
        
        List<InternshipOpportunity> internships = companyRepController.getRepInternships(rep);
        
        if (internships.isEmpty()) {
            System.out.println("You have no internships to edit.");
            return;
        }
        
        // Show only pending and rejected internships (can only edit these)
        List<InternshipOpportunity> editableInternships = internships.stream()
            .filter(i -> i.getStatus() == InternshipStatus.PENDING || i.getStatus() == InternshipStatus.REJECTED)
            .toList();

        if (editableInternships.isEmpty()) {
            System.out.println("You have no editable internships.");
            System.out.println("Note: Only pending or rejected internships can be edited.");
            return;
        }

        internshipView.displayInternshipList(editableInternships);

        System.out.print("\nEnter Internship ID to edit (or 'cancel'): ");
        String internshipId = scanner.nextLine().trim();

        if (internshipId.equalsIgnoreCase("cancel")) {
            return;
        }

        InternshipOpportunity internship = null;
        for (InternshipOpportunity intern : editableInternships) {
            if (intern.getOpportunityId().equalsIgnoreCase(internshipId)) {
                internship = intern;
            }
        }
        
        if (internship == null) {
            System.out.println("Invalid Internship ID.");
            return;
        }
        
        System.out.print("New Title (press Enter to keep current): ");
        String title = scanner.nextLine().trim();
        if (title.isEmpty()) {
            title = internship.getTitle();
        }
        
        System.out.print("New Description (press Enter to keep current): ");
        String description = scanner.nextLine().trim();
        if (description.isEmpty()) {
            description = internship.getDescription();
        }
        
        System.out.print("New Number of Slots (press Enter to keep current): ");
        String slotsStr = scanner.nextLine().trim();
        int slots = internship.getTotalSlots();
        if (!slotsStr.isEmpty()) {
            try {
                slots = Integer.parseInt(slotsStr);
            } catch (NumberFormatException e) {
                System.out.println("Invalid number. Keeping current slots.");
            }
        }
        
        companyRepController.editInternship(rep, internship, title, description, slots);
    }
    
    /**
     * Handles internship deletion
     */
    private void deleteInternship(CompanyRepresentative rep) {
        System.out.println("\n========== DELETE INTERNSHIP OPPORTUNITY ==========");
        
        List<InternshipOpportunity> internships = companyRepController.getRepInternships(rep);
        
        if (internships.isEmpty()) {
            System.out.println("You have no internships to delete.");
            return;
        }
        
        internshipView.displayInternshipList(internships);
        
        System.out.print("\nEnter Internship ID to delete (or 'cancel'): ");
        String internshipId = scanner.nextLine().trim();
        
        if (internshipId.equalsIgnoreCase("cancel")) {
            return;
        }
        
        InternshipOpportunity internship = internshipController.findInternshipById(internshipId);
        
        if (internship == null) {
            System.out.println("Invalid Internship ID.");
            return;
        }
        
        System.out.print("Are you sure you want to delete this internship? (yes/no): ");
        String confirm = scanner.nextLine().trim();
        
        if (confirm.equalsIgnoreCase("yes")) {
            companyRepController.deleteInternship(rep, internship);
        } else {
            System.out.println("Deletion cancelled.");
        }
    }
    
    /**
     * Views applications for rep's internships
     */
    private void viewApplications(CompanyRepresentative rep) {
        System.out.println("\n========== APPLICATIONS FOR MY INTERNSHIPS ==========");
        
        List<InternshipOpportunity> internships = companyRepController.getRepInternships(rep);
        
        if (internships.isEmpty()) {
            System.out.println("You have no internship opportunities.");
            return;
        }
        
        for (InternshipOpportunity internship : internships) {
            List<InternshipApplication> applications = internship.getApplications();
            
            if (!applications.isEmpty()) {
                System.out.println("\n--- " + internship.getTitle() + " (" + internship.getOpportunityId() + ") ---");
                System.out.println("Total Applications: " + applications.size());
                
                for (InternshipApplication app : applications) {
                    System.out.println("\n  Application ID: " + app.getApplicationId());
                    System.out.println("  Student ID: " + app.getStudentId());
                    System.out.println("  Status: " + app.getApplicationStatus().getDisplayName());
                    System.out.println("  Applied on: " + app.getApplicationDate());
                    System.out.println("  Placement Confirmed: " + (app.isPlacementConfirmed() ? "Yes" : "No"));
                }
            }
        }
    }
    
    /**
     * Processes (approve/reject) an application
     */
    private void processApplication(CompanyRepresentative rep) {
        System.out.println("\n========== APPROVE/REJECT APPLICATION ==========");
        
        viewApplications(rep);
        
        System.out.print("\nEnter Application ID to process (or 'cancel'): ");
        String appId = scanner.nextLine().trim();
        
        if (appId.equalsIgnoreCase("cancel")) {
            return;
        }
        
        // Find application (we need a method in studentController)
        InternshipApplication application = findApplicationById(appId, rep);
        
        if (application == null) {
            System.out.println("Invalid Application ID or application not found in your internships.");
            return;
        }
        
        System.out.println("\n1. Approve");
        System.out.println("2. Reject");
        System.out.print("Enter choice: ");
        String choice = scanner.nextLine().trim();
        
        switch (choice) {
            case "1":
                companyRepController.approveApplication(rep, application);
                break;
            case "2":
                companyRepController.rejectApplication(rep, application);
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }
    
    /**
     * Helper to find application by ID within rep's internships
     */
    private InternshipApplication findApplicationById(String appId, CompanyRepresentative rep) {
        List<InternshipOpportunity> internships = companyRepController.getRepInternships(rep);
        
        for (InternshipOpportunity internship : internships) {
            for (InternshipApplication app : internship.getApplications()) {
                if (app.getApplicationId().equals(appId)) {
                    return app;
                }
            }
        }
        
        return null;
    }
    
    /**
     * Toggles internship visibility
     */
    private void toggleVisibility(CompanyRepresentative rep) {
        System.out.println("\n========== TOGGLE INTERNSHIP VISIBILITY ==========");
        
        List<InternshipOpportunity> internships = companyRepController.getRepInternships(rep);
        
        if (internships.isEmpty()) {
            System.out.println("You have no internships.");
            return;
        }
        
        internshipView.displayInternshipList(internships);
        
        System.out.print("\nEnter Internship ID to toggle visibility (or 'cancel'): ");
        String internshipId = scanner.nextLine().trim();
        
        if (internshipId.equalsIgnoreCase("cancel")) {
            return;
        }
        
        InternshipOpportunity internship = internshipController.findInternshipById(internshipId);
        
        if (internship == null) {
            System.out.println("Invalid Internship ID.");
            return;
        }
        
        companyRepController.toggleVisibility(rep, internship);
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