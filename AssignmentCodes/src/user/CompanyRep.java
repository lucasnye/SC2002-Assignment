package user;

import java.util.ArrayList;
import java.util.List;

public class CompanyRep extends User {
    private String companyName;
    private String department;
    private String position;
    private List<Internship> opportunities = new ArrayList<>();

    public CompanyRep(String userId, String name, String password, String companyName, String department, String position) {
        super(userId, name, password, Role.COMPANYREP);
        this.companyName = companyName;
        this.department = department;
        this.position = position;
    }

    // getters
    public String getCompanyName() {
        return companyName;
    }

    public List<Internship> getOpportunities() {
        return opportunities;
    }

    /**
     * Create and register a new internship posting.
     * Status ALWAYS starts as PENDING.
     * Visibility ALWAYS starts OFF.
     * This method is for the variables to be passed to the Internship constructor.
     */

    public Internship createInternship(String title, String description, InternshipLevel level,
                                       String major, String openingDate, String closingDate, int numberOfSlots) {
        // Rule: Max 5 internships per rep
        if (opportunities.size() >= 5) {
            System.out.println("You cannot create more than 5 internship opportunities.");
            return null;
        }

        Internship internship = new Internship(
                this.companyName,     // auto: from rep
                this.getUserId(),     // store rep's ID (your Internship uses String here)
                title,
                description,
                level,
                major,
                closingDate,
                openingDate,
                numberOfSlots,
                Status.PENDING,       // always start pending
                false     // always invisible until staff approves
        );
        
        opportunities.add(internship); // add to this rep's postings
        return internship;
    }

    // Toggle visibility of internship in student listings
    public void toggleVisibility(Internship internship) {
        // Flip the visibility boolean --> if it was true, now false and vice versa
        internship.setVisibility(!internship.getVisibility());
    }

    // potenial things to add (either here or elsewhere):
    // editInternship, removeInternship
    // see student applications, approve or reject
    // view visibility of internships
}
