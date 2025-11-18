package boundary;

import entity.domain.InternshipOpportunity;

import java.util.List;

/**
 * Reusable view for displaying internship information
 */
public class InternshipView {
    
    /**
     * Displays a list of internships
     */
    public void displayInternshipList(List<InternshipOpportunity> internships) {
        if (internships.isEmpty()) {
            System.out.println("No internships to display.");
            return;
        }
        
        System.out.println("\nTotal: " + internships.size() + " internship(s)\n");
        
        for (InternshipOpportunity internship : internships) {
            displayInternshipSummary(internship);
            System.out.println("----------------------------------------");
        }
    }
    
    /**
     * Displays summary of a single internship
     */
    public void displayInternshipSummary(InternshipOpportunity internship) {
        System.out.println("ID: " + internship.getOpportunityId());
        System.out.println("Title: " + internship.getTitle());
        System.out.println("Company: " + internship.getCompanyName());
        System.out.println("Level: " + internship.getLevel());
        System.out.println("Preferred Major: " + internship.getPreferredMajor().getFullName());
        System.out.println("Description: " + internship.getDescription());
        System.out.println("Status: " + internship.getStatus().getDisplayName());
        System.out.println("Slots: " + internship.getFilledSlots() + "/" + internship.getTotalSlots());
        System.out.println("Opening Date: " + internship.getOpeningDate());
        System.out.println("Closing Date: " + internship.getClosingDate());
        System.out.println("Visible: " + (internship.isVisible() ? "Yes" : "No"));
    }
    
    /**
     * Displays detailed information of a single internship
     */
    public void displayInternshipDetails(InternshipOpportunity internship) {
        System.out.println("\n========== INTERNSHIP DETAILS ==========");
        displayInternshipSummary(internship);
        System.out.println("Description: " + internship.getDescription());
        System.out.println("Applications: " + internship.getApplications().size());
        System.out.println("========================================\n");
    }
}