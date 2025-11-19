package controller;

import entity.domain.InternshipOpportunity;
import entity.user.Student;
import enums.InternshipLevel;
import enums.InternshipStatus;
import enums.Major;
import util.FileHandler;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Handles internship opportunity management and filtering
 */
public class InternshipController {

    private List<InternshipOpportunity> internships;
    private int nextOpportunityId;
    private static final String INTERNSHIP_FILE_PATH = "assets/internship_list.csv";

    public InternshipController() {
        this.internships = new ArrayList<>();
        this.nextOpportunityId = 1;
    }

    /**
     * Loads internships from CSV file
     */
    public void loadInternshipsFromFile() {
        internships = FileHandler.loadInternships(INTERNSHIP_FILE_PATH);
        updateNextOpportunityId();
    }

    /**
     * Saves internships to CSV file
     */
    public void saveInternshipsToFile() {
        FileHandler.saveInternships(internships, INTERNSHIP_FILE_PATH);
    }

    /**
     * Updates the nextOpportunityId based on existing internships
     */
    private void updateNextOpportunityId() {
        int maxId = 0;
        for (InternshipOpportunity internship : internships) {
            String id = internship.getOpportunityId();
            if (id.startsWith("INT")) {
                try {
                    int num = Integer.parseInt(id.substring(3));
                    maxId = Math.max(maxId, num);
                } catch (NumberFormatException e) {
                    // Skip invalid IDs
                }
            }
        }
        nextOpportunityId = maxId + 1;
    }
    
    /**
     * Creates a new internship opportunity
     */
    public InternshipOpportunity createInternship(String title, String description,
                                                   InternshipLevel level, Major preferredMajor,
                                                   LocalDate openingDate, LocalDate closingDate,
                                                   String companyName, String repId, int totalSlots) {

        String opportunityId = generateOpportunityId();

        InternshipOpportunity internship = new InternshipOpportunity(
            opportunityId, title, description, level, preferredMajor,
            openingDate, closingDate, companyName, repId, totalSlots
        );

        internships.add(internship);
        saveInternshipsToFile(); // Auto-save to CSV
        return internship;
    }
    
    /**
     * Gets all internships
     */
    public List<InternshipOpportunity> getAllInternships() {
        return new ArrayList<>(internships);
    }
    
    /**
     * Gets internships visible to a specific student
     */
    public List<InternshipOpportunity> getInternshipsForStudent(Student student) {
        return internships.stream()
            .filter(internship -> isVisibleToStudent(internship, student))
            .collect(Collectors.toList());
    }
    
    /**
     * Checks if an internship is visible to a student
     */
    private boolean isVisibleToStudent(InternshipOpportunity internship, Student student) {
        // Must be visible
        if (!internship.isVisible()) {
            return false;
        }
        
        // Must be approved
        if (internship.getStatus() != InternshipStatus.APPROVED) {
            return false;
        }
        
        // Check major match
        if (internship.getPreferredMajor() != student.getMajor()) {
            return false;
        }
        
        // Check level eligibility
        return isEligibleForLevel(student, internship.getLevel());
    }
    
    /**
     * Checks if a student is eligible for an internship level
     */
    public boolean isEligibleForLevel(Student student, InternshipLevel level) {
        int year = student.getYearOfStudy();
        
        switch (level) {
            case BASIC:
                return true; // All years can apply for basic
            case INTERMEDIATE:
            case ADVANCED:
                return year >= 3; // Only year 3 and above
            default:
                return false;
        }
    }
    
    /**
     * Filters internships by status
     */
    public List<InternshipOpportunity> filterByStatus(List<InternshipOpportunity> internships, 
                                                       InternshipStatus status) {
        return internships.stream()
            .filter(i -> i.getStatus() == status)
            .collect(Collectors.toList());
    }
    
    /**
     * Filters internships by level
     */
    public List<InternshipOpportunity> filterByLevel(List<InternshipOpportunity> internships,
                                                      InternshipLevel level) {
        return internships.stream()
            .filter(i -> i.getLevel() == level)
            .collect(Collectors.toList());
    }
    
    /**
     * Filters internships by major
     */
    public List<InternshipOpportunity> filterByMajor(List<InternshipOpportunity> internships,
                                                      Major major) {
        return internships.stream()
            .filter(i -> i.getPreferredMajor() == major)
            .collect(Collectors.toList());
    }
    
    /**
     * Filters internships by company name
     */
    public List<InternshipOpportunity> filterByCompany(List<InternshipOpportunity> internships,
                                                        String companyName) {
        return internships.stream()
            .filter(i -> i.getCompanyName().equalsIgnoreCase(companyName))
            .collect(Collectors.toList());
    }
    
    /**
     * Sorts internships alphabetically by title
     */
    public List<InternshipOpportunity> sortByTitle(List<InternshipOpportunity> internships) {
        return internships.stream()
            .sorted((i1, i2) -> i1.getTitle().compareToIgnoreCase(i2.getTitle()))
            .collect(Collectors.toList());
    }
    
    /**
     * Finds an internship by ID
     */
    public InternshipOpportunity findInternshipById(String opportunityId) {
        return internships.stream()
            .filter(i -> i.getOpportunityId().equals(opportunityId))
            .findFirst()
            .orElse(null);
    }
    
    /**
     * Gets internships created by a specific company representative
     */
    public List<InternshipOpportunity> getInternshipsByRep(String repId) {
        return internships.stream()
            .filter(i -> i.getAssignedRepresentativeId().equals(repId))
            .collect(Collectors.toList());
    }
    
    /**
     * Generates a unique opportunity ID
     */
    private String generateOpportunityId() {
        return String.format("INT%04d", nextOpportunityId++);
    }
    
    /**
     * Removes an internship
     */
    public boolean removeInternship(InternshipOpportunity internship) {
        boolean removed = internships.remove(internship);
        if (removed) {
            saveInternshipsToFile(); // Auto-save to CSV
        }
        return removed;
    }

    /**
     * Updates an internship (call this after modifying internship attributes)
     */
    public void updateInternship() {
        saveInternshipsToFile(); // Save changes to CSV
    }
}