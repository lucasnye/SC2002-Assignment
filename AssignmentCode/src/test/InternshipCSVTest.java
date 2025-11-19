package test;

import entity.domain.InternshipOpportunity;
import enums.InternshipLevel;
import enums.InternshipStatus;
import enums.Major;
import util.FileHandler;

import java.time.LocalDate;
import java.util.List;

/**
 * Simple test class to verify Internship CSV storage functionality
 */
public class InternshipCSVTest {
    public static void main(String[] args) {
        System.out.println("=== Testing Internship CSV Storage ===\n");

        // Test 1: Create and save internships
        System.out.println("Test 1: Creating test internships...");
        List<InternshipOpportunity> testInternships = List.of(
            new InternshipOpportunity(
                "INT0001",
                "Software Engineer Intern",
                "Develop web applications using Java, Spring Boot, and React. Work with experienced engineers.",
                InternshipLevel.INTERMEDIATE,
                Major.CS,
                LocalDate.of(2025, 1, 1),
                LocalDate.of(2025, 3, 31),
                "Tech Innovators Pte Ltd",
                "CR0001",
                5
            ),
            new InternshipOpportunity(
                "INT0002",
                "Data Science Intern",
                "Analyze data, build ML models, create visualizations. Tools: Python, Pandas, TensorFlow.",
                InternshipLevel.ADVANCED,
                Major.DSAI,
                LocalDate.of(2025, 2, 1),
                LocalDate.of(2025, 4, 30),
                "Data Analytics Corp",
                "CR0002",
                3
            ),
            new InternshipOpportunity(
                "INT0003",
                "Junior Developer",
                "Learn programming basics, assist in bug fixes, participate in code reviews.",
                InternshipLevel.BASIC,
                Major.CE,
                LocalDate.of(2025, 1, 15),
                LocalDate.of(2025, 5, 15),
                "StartUp Solutions",
                "CR0001",
                10
            )
        );

        // Set some additional properties
        testInternships.get(0).setStatus(InternshipStatus.APPROVED);
        testInternships.get(0).setVisible(true);
        testInternships.get(0).setFilledSlots(2);

        testInternships.get(1).setStatus(InternshipStatus.PENDING);
        testInternships.get(1).setVisible(false);

        testInternships.get(2).setStatus(InternshipStatus.APPROVED);
        testInternships.get(2).setVisible(true);
        testInternships.get(2).setFilledSlots(5);

        System.out.println("Created " + testInternships.size() + " test internships\n");

        // Test 2: Save to CSV
        System.out.println("Test 2: Saving internships to CSV...");
        String testFilePath = "assets/internship_list.csv";
        FileHandler.saveInternships(testInternships, testFilePath);
        System.out.println();

        // Test 3: Load from CSV
        System.out.println("Test 3: Loading internships from CSV...");
        List<InternshipOpportunity> loadedInternships = FileHandler.loadInternships(testFilePath);
        System.out.println();

        // Test 4: Verify data integrity
        System.out.println("Test 4: Verifying data integrity...");
        boolean allMatch = true;

        if (loadedInternships.size() != testInternships.size()) {
            System.out.println("ERROR: Size mismatch! Expected " + testInternships.size() + " but got " + loadedInternships.size());
            allMatch = false;
        } else {
            for (int i = 0; i < testInternships.size(); i++) {
                InternshipOpportunity original = testInternships.get(i);
                InternshipOpportunity loaded = loadedInternships.get(i);

                System.out.println("Checking internship " + (i + 1) + ": " + original.getOpportunityId());

                if (!original.getOpportunityId().equals(loaded.getOpportunityId())) {
                    System.out.println("  ERROR: ID mismatch");
                    allMatch = false;
                }
                if (!original.getTitle().equals(loaded.getTitle())) {
                    System.out.println("  ERROR: Title mismatch");
                    allMatch = false;
                }
                if (!original.getDescription().equals(loaded.getDescription())) {
                    System.out.println("  ERROR: Description mismatch");
                    System.out.println("    Expected: " + original.getDescription());
                    System.out.println("    Got: " + loaded.getDescription());
                    allMatch = false;
                }
                if (original.getLevel() != loaded.getLevel()) {
                    System.out.println("  ERROR: Level mismatch");
                    allMatch = false;
                }
                if (original.getPreferredMajor() != loaded.getPreferredMajor()) {
                    System.out.println("  ERROR: Major mismatch");
                    allMatch = false;
                }
                if (!original.getOpeningDate().equals(loaded.getOpeningDate())) {
                    System.out.println("  ERROR: Opening date mismatch");
                    allMatch = false;
                }
                if (!original.getClosingDate().equals(loaded.getClosingDate())) {
                    System.out.println("  ERROR: Closing date mismatch");
                    allMatch = false;
                }
                if (original.getStatus() != loaded.getStatus()) {
                    System.out.println("  ERROR: Status mismatch");
                    allMatch = false;
                }
                if (!original.getCompanyName().equals(loaded.getCompanyName())) {
                    System.out.println("  ERROR: Company name mismatch");
                    allMatch = false;
                }
                if (!original.getAssignedRepresentativeId().equals(loaded.getAssignedRepresentativeId())) {
                    System.out.println("  ERROR: Representative ID mismatch");
                    allMatch = false;
                }
                if (original.getTotalSlots() != loaded.getTotalSlots()) {
                    System.out.println("  ERROR: Total slots mismatch");
                    allMatch = false;
                }
                if (original.getFilledSlots() != loaded.getFilledSlots()) {
                    System.out.println("  ERROR: Filled slots mismatch");
                    allMatch = false;
                }
                if (original.isVisible() != loaded.isVisible()) {
                    System.out.println("  ERROR: Visibility mismatch");
                    allMatch = false;
                }

                System.out.println("  ✓ All fields match!");
            }
        }

        System.out.println();
        if (allMatch) {
            System.out.println("=== ALL TESTS PASSED! ===");
            System.out.println("CSV storage and retrieval working correctly.");
        } else {
            System.out.println("=== TESTS FAILED! ===");
            System.out.println("There were errors in data integrity.");
        }
    }
}
