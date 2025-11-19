package test;

import controller.AuthController;
import entity.user.CompanyRepresentative;
import entity.user.Student;
import entity.user.CareerCenterStaff;
import util.FileHandler;

import java.util.List;

/**
 * Simple test to verify company rep email-based authentication
 */
public class AuthTest {
    public static void main(String[] args) {
        System.out.println("=== Testing Company Rep Email Authentication ===\n");

        // Load data
        List<Student> students = FileHandler.loadStudents("assets/student_list.csv");
        List<CareerCenterStaff> staff = FileHandler.loadStaff("assets/staff_list.csv");
        List<CompanyRepresentative> companyReps = FileHandler.loadCompanyReps("assets/company_representative_list.csv");

        // Debug: Print company rep details
        System.out.println("DEBUG: Company Representatives loaded:");
        for (CompanyRepresentative rep : companyReps) {
            System.out.println("  ID: " + rep.getUserId() + ", Email: " + rep.getEmail() + ", Name: " + rep.getName());
        }
        System.out.println();

        // Create auth controller
        AuthController authController = new AuthController(students, companyReps, staff);

        System.out.println("Test 1: Login with Email");
        String testEmail = "luca0010@e.ntu.edu.sg";
        System.out.println("Attempting to login with email: " + testEmail);
        System.out.println("DEBUG: Is valid email? " + util.ValidationUtil.isValidEmail(testEmail));
        boolean emailLoginResult = authController.login(testEmail, "password");
        System.out.println("Result: " + (emailLoginResult ? "SUCCESS ✓" : "FAILED ✗"));
        System.out.println();

        // Logout
        authController.logout();
        System.out.println();

        System.out.println("Test 2: Login with Company Rep ID (backward compatibility)");
        System.out.println("Attempting to login with ID: CR0001");
        boolean idLoginResult = authController.login("CR0001", "password");
        System.out.println("Result: " + (idLoginResult ? "SUCCESS ✓" : "FAILED ✗"));
        System.out.println();

        // Logout
        authController.logout();
        System.out.println();

        System.out.println("Test 3: Login with wrong email");
        System.out.println("Attempting to login with email: wrong@email.com");
        boolean wrongEmailResult = authController.login("wrong@email.com", "password");
        System.out.println("Result: " + (wrongEmailResult ? "SUCCESS ✓ (UNEXPECTED)" : "FAILED ✓ (EXPECTED)"));
        System.out.println();

        System.out.println("Test 4: Case insensitive email");
        System.out.println("Attempting to login with email: LUCA0010@E.NTU.EDU.SG");
        boolean caseInsensitiveResult = authController.login("LUCA0010@E.NTU.EDU.SG", "password");
        System.out.println("Result: " + (caseInsensitiveResult ? "SUCCESS ✓" : "FAILED ✗"));
        System.out.println();

        // Logout
        authController.logout();

        // Summary
        System.out.println("=== Test Summary ===");
        int passed = 0;
        if (emailLoginResult) passed++;
        if (idLoginResult) passed++;
        if (!wrongEmailResult) passed++; // This should fail
        if (caseInsensitiveResult) passed++;

        System.out.println("Passed: " + passed + "/4 tests");
        if (passed == 4) {
            System.out.println("✓ All tests passed!");
        } else {
            System.out.println("✗ Some tests failed.");
        }
    }
}
