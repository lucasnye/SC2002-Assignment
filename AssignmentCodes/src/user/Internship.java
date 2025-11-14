package user;

import java.util.ArrayList;
import java.util.List;

public class Internship {
    private String company;
    private String companyRep;
    // private String id;
    private String title;
    private String description;
    private InternshipLevel level;
    private String major;
    private String closingDate;
    private String openingDate;
    private int numberOfSlots;
    private Status status;
    private boolean visibility;
    private List<Application> studentApplicants = new ArrayList<>();

    // Basic constructor
    public Internship (String company, String companyRep, String title) {
        this.company = company;
        this.companyRep = companyRep;
        this.title = title;
    }

    // Full constructor
    public Internship(String company, String companyRep, String title, String description, InternshipLevel level, String major, String closingDate, String openingDate, int numberOfSlots, Status status, boolean visibility) {
        this.company = company;
        this.companyRep = companyRep;
        this.title = title;
        this.description = description;
        this.level = level;
        this.major = major;
        this.closingDate = closingDate;
        this.openingDate = openingDate;
        this.numberOfSlots = numberOfSlots;
        this.status = status;
        this.visibility = visibility;
    }

    // Getters
    public void addSlot(int n) {
        this.numberOfSlots += n;
    } 

    public void removeSlot(int n) {
        this.numberOfSlots -= n;
    }

    public Status getStatus() {
        return this.status;
    }

    public boolean getVisibility() {
        return this.visibility;
    }

    public String getTitle() {
        return this.title;
    }

    public int getNumberOfSlots() {
        return this.numberOfSlots;
    }

    //added this getter for Student
    public String getMajor(){
        return this.major;
    }

    // Setters
    public void setStatus(Status status) {
        this.status = status;
    }

    public void setDescription(String desc) {
        this.description = desc;
    }

    public void setLevel(InternshipLevel level) {
        this.level = level;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public void setClosingDate(String date) {
        this.closingDate = date;
    }

    public void setOpeningDate(String date) {
        this.openingDate = date;
    }

    public void setNumberOfSlots(int n) {
        this.numberOfSlots = n;
    }

    public void setVisibility(boolean vis) {
        this.visibility = vis;
    }

    // keeping track of student applicants
    public void addApplication(Application app) {
        studentApplicants.add(app);
    }

    public List<Application> getApplications() {
        return studentApplicants;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Title: " + this.title);
        sb.append("\nCompany: " + this.company);
        sb.append("\nDescription: " + this.description);
        sb.append("\nLevel: " + this.level);
        sb.append("\nPreferred Major: " + this.major);
        sb.append("\nOpening Date: " + this.openingDate);
        sb.append("\nClosing Date: " + this.closingDate);
        sb.append("\nNumber of Slots: " + this.numberOfSlots);
        sb.append("\nCompany Representative: " + this.companyRep);

        return sb.toString();
    }
}
