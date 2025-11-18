package main;

import controller.*;
import boundary.*;
import entity.user.Student;
import entity.user.CompanyRepresentative;
import entity.user.CareerCenterStaff;
import util.FileHandler;

import java.util.List;

/**
 * Main application class - Entry point for the Internship Placement Management System
 */
public class MainApplication {
    
    // Data storage
    private List<Student> students;
    private List<CompanyRepresentative> companyReps;
    private List<CareerCenterStaff> staff;
    
    // Controllers
    private AuthController authController;
    private InternshipController internshipController;
    private StudentController studentController;
    private CompanyRepController companyRepController;
    private CareerCenterController careerCenterController;
    
    // Views
    private InternshipView internshipView;
    private StudentView studentView;
    private CompanyRepView companyRepView;
    private CareerCenterView careerCenterView;
    private MainView mainView;
    
    /**
     * Main method - Entry point
     */
    public static void main(String[] args) {
        MainApplication app = new MainApplication();
        app.initialize();
        app.start();
    }
    
    /**
     * Initializes the application - loads data and creates controllers/views
     */
    private void initialize() {
        System.out.println("==============================================");
        System.out.println("  INITIALISING SYSTEM...");
        System.out.println("==============================================\n");
        
        // Load data from CSV files
        loadData();
        
        // Create controllers
        createControllers();
        
        // Create views
        createViews();
        
        System.out.println("\n==============================================");
        System.out.println("  SYSTEM INITIALISATION COMPLETE");
        System.out.println("==============================================\n");
    }
    
    /**
     * Loads all data from CSV files
     */
    private void loadData() {
        System.out.println("Loading data from files...\n");
        
        // Load students
        students = FileHandler.loadStudents("assets/student_list.csv");
        
        // Load staff
        staff = FileHandler.loadStaff("assets/staff_list.csv");
        
        // Load company representatives (may be empty initially)
        companyReps = FileHandler.loadCompanyReps("assets/company_representative_list.csv");
        
        System.out.println("\nData loading complete!");
        System.out.println("Students: " + students.size());
        System.out.println("Staff: " + staff.size());
        System.out.println("Company Representatives: " + companyReps.size());
    }
    
    /**
     * Creates all controllers
     */
    private void createControllers() {
        System.out.println("\nInitialising controllers...");
        
        // Create internship controller (manages all internships)
        internshipController = new InternshipController();
        
        // Create student controller (manages applications and withdrawals)
        studentController = new StudentController(internshipController);
        
        // Create company rep controller (manages internship creation and application processing)
        companyRepController = new CompanyRepController(internshipController, studentController);
        
        // Create career center controller (manages approvals and reports)
        careerCenterController = new CareerCenterController(
            companyReps, internshipController, studentController
        );
        
        // Create auth controller (manages login/logout)
        authController = new AuthController(students, companyReps, staff);
        
        System.out.println("Controllers initialised successfully!");
    }
    
    /**
     * Creates all views
     */
    private void createViews() {
        System.out.println("Initialising views...");
        
        // Create reusable internship view
        internshipView = new InternshipView();
        
        // Create student view
        studentView = new StudentView(
            authController, studentController, internshipController, internshipView
        );
        
        // Create company rep view
        companyRepView = new CompanyRepView(
            authController, companyRepController, internshipController, 
            internshipView, companyReps
        );
        
        // Create career center view
        careerCenterView = new CareerCenterView(
            authController, careerCenterController, internshipView
        );
        
        // Create main view (entry point)
        mainView = new MainView(
            authController, studentView, companyRepView, careerCenterView
        );
        
        System.out.println("Views initialised successfully!");
    }
    
    /**
     * Starts the application
     */
    private void start() {
        // Start the main view (login screen and main menu loop)
        mainView.start();
    }
}
