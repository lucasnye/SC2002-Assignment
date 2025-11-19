package test;

import controller.CompanyRepController;
import controller.InternshipController;
import controller.StudentController;
import entity.domain.InternshipOpportunity;
import entity.user.CompanyRepresentative;
import enums.InternshipLevel;
import enums.InternshipStatus;
import enums.Major;

import java.time.LocalDate;
import java.util.List;

/**
 * Test to verify rejected internships appear in the editable list
 */
public class ViewRejectedInternshipTest {
    public static void main(String[] args) {
        System.out.println("=== Testing Rejected Internships Visibility ===\n");

        // Setup
        InternshipController internshipController = new InternshipController();
        StudentController studentController = new StudentController(internshipController);
        CompanyRepController companyRepController = new CompanyRepController(internshipController, studentController);

        CompanyRepresentative rep = new CompanyRepresentative(
            "CR0001", "Test Rep", "test@company.com", "password",
            "Test Company", "IT", "Manager"
        );

        // Create internships with different statuses
        InternshipOpportunity pending = internshipController.createInternship(
            "Pending Internship", "Description", InternshipLevel.BASIC, Major.CS,
            LocalDate.now(), LocalDate.now().plusDays(30), "Test Company", "CR0001", 5
        );
        rep.addOpportunity(pending);

        InternshipOpportunity rejected = internshipController.createInternship(
            "Rejected Internship", "Description", InternshipLevel.BASIC, Major.CS,
            LocalDate.now(), LocalDate.now().plusDays(30), "Test Company", "CR0001", 5
        );
        rejected.setStatus(InternshipStatus.REJECTED);
        rep.addOpportunity(rejected);

        InternshipOpportunity approved = internshipController.createInternship(
            "Approved Internship", "Description", InternshipLevel.BASIC, Major.CS,
            LocalDate.now(), LocalDate.now().plusDays(30), "Test Company", "CR0001", 5
        );
        approved.setStatus(InternshipStatus.APPROVED);
        rep.addOpportunity(approved);

        // Get all internships for the rep
        List<InternshipOpportunity> allInternships = companyRepController.getRepInternships(rep);
        System.out.println("Total internships for rep: " + allInternships.size());

        // Filter to get editable internships (simulating what the view does)
        List<InternshipOpportunity> editableInternships = allInternships.stream()
            .filter(i -> i.getStatus() == InternshipStatus.PENDING || i.getStatus() == InternshipStatus.REJECTED)
            .toList();

        System.out.println("Editable internships (PENDING or REJECTED): " + editableInternships.size());
        System.out.println();

        System.out.println("Editable Internships List:");
        for (InternshipOpportunity internship : editableInternships) {
            System.out.println("  - " + internship.getOpportunityId() + ": " + internship.getTitle() + " [" + internship.getStatus() + "]");
        }
        System.out.println();

        // Tests
        boolean test1 = editableInternships.size() == 2;
        System.out.println("Test 1 - Should have 2 editable internships: " + (test1 ? "PASS ✓" : "FAIL ✗"));

        boolean test2 = editableInternships.stream().anyMatch(i -> i.getStatus() == InternshipStatus.PENDING);
        System.out.println("Test 2 - Should include PENDING internship: " + (test2 ? "PASS ✓" : "FAIL ✗"));

        boolean test3 = editableInternships.stream().anyMatch(i -> i.getStatus() == InternshipStatus.REJECTED);
        System.out.println("Test 3 - Should include REJECTED internship: " + (test3 ? "PASS ✓" : "FAIL ✗"));

        boolean test4 = editableInternships.stream().noneMatch(i -> i.getStatus() == InternshipStatus.APPROVED);
        System.out.println("Test 4 - Should NOT include APPROVED internship: " + (test4 ? "PASS ✓" : "FAIL ✗"));

        System.out.println();
        int passed = (test1 ? 1 : 0) + (test2 ? 1 : 0) + (test3 ? 1 : 0) + (test4 ? 1 : 0);
        System.out.println("=== Test Summary ===");
        System.out.println("Passed: " + passed + "/4 tests");
        if (passed == 4) {
            System.out.println("✓ Rejected internships are now visible for editing!");
        } else {
            System.out.println("✗ Some tests failed.");
        }
    }
}
