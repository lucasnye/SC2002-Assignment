package boundary;

import controller.*;
import entity.user.User;
import util.SessionManager;

import java.util.Scanner;

/**
 * Main view - Entry point and login screen
 */
public class MainView {
    
    private Scanner scanner;
    private AuthController authController;
    private StudentView studentView;
    private CompanyRepView companyRepView;
    private CareerCenterView careerCenterView;
    private SessionManager sessionManager;
    
    public MainView(AuthController authController,
                    StudentView studentView,
                    CompanyRepView companyRepView,
                    CareerCenterView careerCenterView) {
        this.scanner = new Scanner(System.in);
        this.authController = authController;
        this.studentView = studentView;
        this.companyRepView = companyRepView;
        this.careerCenterView = careerCenterView;
        this.sessionManager = SessionManager.getInstance();
    }
    
    /**
     * Displays the main menu and handles navigation
     */
    public void start() {
        System.out.println("==============================================");
        System.out.println("  INTERNSHIP PLACEMENT MANAGEMENT SYSTEM");
        System.out.println("==============================================\n");
        
        boolean running = true;
        
        while (running) {
            if (!sessionManager.isLoggedIn()) {
                displayLoginMenu();
            } else {
                routeToUserView();
            }
        }
    }
    
    /**
     * Displays login menu
     */
    private void displayLoginMenu() {
        System.out.println("\n========== LOGIN MENU ==========");
        System.out.println("1. Login");
        System.out.println("2. Register as Company Representative");
        System.out.println("3. Exit");
        System.out.print("Enter choice: ");
        
        String choice = scanner.nextLine().trim();
        
        switch (choice) {
            case "1":
                handleLogin();
                break;
            case "2":
                handleCompanyRepRegistration();
                break;
            case "3":
                System.out.println("Thank you for using the system. Goodbye!");
                System.exit(0);
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }
    
    /**
     * Handles user login
     */
    private void handleLogin() {
        System.out.println("\n========== LOGIN ==========");
        System.out.println("Students: Use Student ID (e.g., U2310001A)");
        System.out.println("Staff: Use Staff ID (e.g., sng001)");
        System.out.println("Company Reps: Use Email (e.g., john@company.com)");
        System.out.print("\nUser ID / Email: ");
        String userId = scanner.nextLine().trim();

        System.out.print("Password: ");
        String password = scanner.nextLine().trim();

        authController.login(userId, password);
    }
    
    /**
     * Handles company representative registration
     */
    private void handleCompanyRepRegistration() {
        System.out.println("\n========== COMPANY REPRESENTATIVE REGISTRATION ==========");
        System.out.println("Note: Your account will need to be approved by Career Center Staff before you can login.");
        
        System.out.print("Full Name: ");
        String name = scanner.nextLine().trim();
        
        System.out.print("Email Address: ");
        String email = scanner.nextLine().trim();
        
        System.out.print("Company Name: ");
        String companyName = scanner.nextLine().trim();
        
        System.out.print("Department: ");
        String department = scanner.nextLine().trim();
        
        System.out.print("Position: ");
        String position = scanner.nextLine().trim();
        
        // This will be handled by CompanyRepView
        companyRepView.registerNewRep(name, email, companyName, department, position);
    }
    
    /**
     * Routes to appropriate view based on user type
     */
    private void routeToUserView() {
        User currentUser = sessionManager.getCurrentUser();
        String userType = currentUser.getUserType();
        
        switch (userType) {
            case "STUDENT":
                studentView.displayMenu();
                break;
            case "COMPANY_REP":
                companyRepView.displayMenu();
                break;
            case "CAREER_STAFF":
                careerCenterView.displayMenu();
                break;
            default:
                System.out.println("Unknown user type. Logging out...");
                authController.logout();
        }
    }
}