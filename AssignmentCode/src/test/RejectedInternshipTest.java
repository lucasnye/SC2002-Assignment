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

/**
 * Test to verify rejected internships can be deleted and edited
 */
public class RejectedInternshipTest {
    public static void main(String[] args) {
        System.out.println("=== Testing Rejected Internship Fix ===\n");

        // Setup
        InternshipController internshipController = new InternshipController();
        StudentController studentController = new StudentController(internshipController);
        CompanyRepController companyRepController = new CompanyRepController(internshipController, studentController);

        CompanyRepresentative rep = new CompanyRepresentative(
            "CR0001", "Test Rep", "test@company.com", "password",
            "Test Company", "IT", "Manager"
        );

        // Test 1: Delete a PENDING internship
        System.out.println("Test 1: Delete PENDING internship");
        InternshipOpportunity pendingInternship = internshipController.createInternship(
            "Pending Internship", "Description", InternshipLevel.BASIC, Major.CS,
            LocalDate.now(), LocalDate.now().plusDays(30), "Test Company", "CR0001", 5
        );
        rep.addOpportunity(pendingInternship);

        boolean deletePending = companyRepController.deleteInternship(rep, pendingInternship);
        System.out.println("Result: " + (deletePending ? "SUCCESS ✓" : "FAILED ✗"));
        System.out.println();

        // Test 2: Delete a REJECTED internship
        System.out.println("Test 2: Delete REJECTED internship");
        InternshipOpportunity rejectedInternship = internshipController.createInternship(
            "Rejected Internship", "Description", InternshipLevel.BASIC, Major.CS,
            LocalDate.now(), LocalDate.now().plusDays(30), "Test Company", "CR0001", 5
        );
        rep.addOpportunity(rejectedInternship);
        rejectedInternship.setStatus(InternshipStatus.REJECTED);

        boolean deleteRejected = companyRepController.deleteInternship(rep, rejectedInternship);
        System.out.println("Result: " + (deleteRejected ? "SUCCESS ✓" : "FAILED ✗"));
        System.out.println();

        // Test 3: Cannot delete APPROVED internship
        System.out.println("Test 3: Cannot delete APPROVED internship (should fail)");
        InternshipOpportunity approvedInternship = internshipController.createInternship(
            "Approved Internship", "Description", InternshipLevel.BASIC, Major.CS,
            LocalDate.now(), LocalDate.now().plusDays(30), "Test Company", "CR0001", 5
        );
        rep.addOpportunity(approvedInternship);
        approvedInternship.setStatus(InternshipStatus.APPROVED);

        boolean deleteApproved = companyRepController.deleteInternship(rep, approvedInternship);
        System.out.println("Result: " + (!deleteApproved ? "BLOCKED ✓ (EXPECTED)" : "ALLOWED ✗ (UNEXPECTED)"));
        System.out.println();

        // Test 4: Edit REJECTED internship resets status to PENDING
        System.out.println("Test 4: Edit REJECTED internship - should reset to PENDING");
        InternshipOpportunity editRejected = internshipController.createInternship(
            "Edit Rejected", "Old Description", InternshipLevel.BASIC, Major.CS,
            LocalDate.now(), LocalDate.now().plusDays(30), "Test Company", "CR0001", 5
        );
        rep.addOpportunity(editRejected);
        editRejected.setStatus(InternshipStatus.REJECTED);

        System.out.println("Before edit - Status: " + editRejected.getStatus());
        boolean editResult = companyRepController.editInternship(rep, editRejected,
            "New Title", "New Description", 3);
        System.out.println("After edit - Status: " + editRejected.getStatus());

        boolean statusResetCorrect = (editResult && editRejected.getStatus() == InternshipStatus.PENDING);
        System.out.println("Result: " + (statusResetCorrect ? "SUCCESS ✓" : "FAILED ✗"));
        System.out.println();

        // Test 5: Edit PENDING internship keeps PENDING status
        System.out.println("Test 5: Edit PENDING internship - should remain PENDING");
        InternshipOpportunity editPending = internshipController.createInternship(
            "Edit Pending", "Old Description", InternshipLevel.BASIC, Major.CS,
            LocalDate.now(), LocalDate.now().plusDays(30), "Test Company", "CR0001", 5
        );
        rep.addOpportunity(editPending);

        System.out.println("Before edit - Status: " + editPending.getStatus());
        boolean editPendingResult = companyRepController.editInternship(rep, editPending,
            "New Title", "New Description", 3);
        System.out.println("After edit - Status: " + editPending.getStatus());

        boolean statusStillPending = (editPendingResult && editPending.getStatus() == InternshipStatus.PENDING);
        System.out.println("Result: " + (statusStillPending ? "SUCCESS ✓" : "FAILED ✗"));
        System.out.println();

        // Summary
        System.out.println("=== Test Summary ===");
        int passed = 0;
        if (deletePending) passed++;
        if (deleteRejected) passed++;
        if (!deleteApproved) passed++; // Should NOT be able to delete
        if (statusResetCorrect) passed++;
        if (statusStillPending) passed++;

        System.out.println("Passed: " + passed + "/5 tests");
        if (passed == 5) {
            System.out.println("✓ All tests passed! Bug is fixed.");
        } else {
            System.out.println("✗ Some tests failed.");
        }
    }
}
