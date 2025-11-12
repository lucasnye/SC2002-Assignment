package user;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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

    // edit internship details as long as not filled
    public void editInternship(Internship internship, String newDescription,
                           String newMajor, InternshipLevel newLevel, int newSlots) {
    if (internship.getStatus() == Status.FILLED) {
        System.out.println("You cannot edit a filled internship.");
        return;
        }
        internship.setDescription(newDescription);
        internship.setMajor(newMajor);
        internship.setLevel(newLevel);
        internship.setNumberOfSlots(newSlots);
        System.out.println("Internship details updated successfully.");
    }

    // remove internship if not filled
    public void removeInternship(Internship internship) {
        if (internship.getStatus() == Status.FILLED) {
            System.out.println("You cannot remove a filled internship.");
            return;
        }
        opportunities.remove(internship);
        System.out.println("Internship removed successfully.");
    }


    // Toggle visibility of internship in student listings
    public void toggleVisibility(Internship internship) {
        // Flip the visibility boolean --> if it was true, now false and vice versa
        internship.setVisibility(!internship.getVisibility());
    }

    // check visibility for all internships
    public void checkVisibility() {
    if (opportunities.isEmpty()) {
        System.out.println("No internship postings yet.");
        return;
    }

    for (int i = 0; i < opportunities.size(); i++) {
        Internship internship = opportunities.get(i);

        String status;
        if (internship.getVisibility()) {
            status = "Visible";
        } else {
            status = "Hidden";
        }

        System.out.println((i + 1) + ". " + internship.getTitle() + " : " + status);
        }
    }

    // helper methods to view applications, accept or reject students
    public void viewApplications(Internship internship) {
        List<Application> apps = internship.getApplications();

        if (apps.isEmpty()) {
            System.out.println("No students have applied for this internship yet.");
            return;
        }

        System.out.println("=== Applications for " + internship.getTitle() + " ===");
        for (int i = 0; i < apps.size(); i++) {
            Application app = apps.get(i);
            System.out.println((i + 1) + ". Student ID: " + app.getStudentID());
            System.out.println("   Year: " + app.getYear());
            System.out.println("   Status: " + app.getStatus());
            System.out.println();
        }
    }

    public void approveApplication(Internship internship, Application app) {
        app.setStatus(AppStatus.SUCCESSFUL);
        internship.removeSlot(1);  // reduce available slots

        System.out.println("Approved application for Student ID: " + app.getStudentID());

        if (internship.getNumberOfSlots() <= 0) {
            internship.setStatus(Status.FILLED);
            System.out.println("Internship " + internship.getTitle() + " is now FILLED!");
        }
    }

    public void rejectApplication(Application app) {
        app.setStatus(AppStatus.UNSUCCESSFUL);
        System.out.println("Rejected application for Student ID: " + app.getStudentID());
    }
    
    // menu for company rep to view and manage student applications
    public void manageApplications(Internship internship) {
        List<Application> apps = internship.getApplications();
        Scanner sc = new Scanner(System.in);

        if (apps.isEmpty()) {
            System.out.println("No students have applied for this internship yet.");
            return;
        }
    
        viewApplications(internship);

        System.out.print("Enter the number of the student applicant to review: ");
        int choice = sc.nextInt() - 1;

        if (choice < 0 || choice >= apps.size()) {
            System.out.println("Invalid choice!");
            return;
        }

        Application selectedApp = apps.get(choice);

        System.out.print("Approve (1) or Reject (0)? ");
        int decision = sc.nextInt();

        if (decision == 1) {
            approveApplication(internship, selectedApp);
        } else if (decision == 0) {
            rejectApplication(selectedApp);
        } else {
            System.out.println("Invalid option.");
        }
    }

    // if there are multiple internships, choose which one to manage
    public void chooseInternshipToManage(Scanner sc) {
        if (opportunities.isEmpty()) {
            System.out.println("You have no internships posted yet.");
            return;
        }

        System.out.println("=== Your Internships ===");
        for (int i = 0; i < opportunities.size(); i++) {
            Internship in = opportunities.get(i);
            System.out.println((i + 1) + ". " + in.getTitle() + " (" + in.getStatus() + ")");
        }

        System.out.print("Select an internship to manage applications: ");
        int choice = sc.nextInt() - 1;

        if (choice >= 0 && choice < opportunities.size()) {
            manageApplications(opportunities.get(choice));
        } else {
            System.out.println("Invalid choice!");
        }
    }

}
