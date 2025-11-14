package user;

import java.util.ArrayList;
import java.util.List;

public class Student extends User {

    private int year;
    private String major;
    private boolean profileVisibility = true;
    private List<Application> applications = new ArrayList<>();

    public Student (String userId, String name, String password, int year, String major){
        super(userId, name, password, Role.STUDENT);
        this.year = year;
        this.major = major;
    }

    public int getYear(){
        return year;
    }

    public String getMajor(){
        return major;
    }

    public boolean profileVisibility (){
        return profileVisibility;
    }

    public void toggleProfileVisibility(){
        this.profileVisibility =! this.profileVisibility;
    }

    public List<Internship> viewAvailableOpportunities(List<Internship> allInternship){
        List<Internship> result = new ArrayList<>();
        for (Internship i : allInternship) {
            if (i.visibility() && (i.getMajor() == null || i.getMajor().equalsIgnoreCase(this.major))){
                if (this.year <= 2 && i.getLevel() != InternshipLevel.BASIC){
                    continue;
                }
                result.add(i);
            }
        }
        return result;
    }

    public List<Internship> viewOpportunities(List<Internship> allInternship){
        return new ArrayList<>(allInternship);
    }

    public Application applyToInternship(Internship internshipOpportunity){
        if (applicationCount() >= 3){
            System.out.println("You have reached the maximum of 3 active application");
            return null;
        }

        if (this.year <= 2 && internshipOpportunity.getLevel() != InternshipLevel.BASIC){
            System.out.println("Y1 and Y2 students only allowed to apply for basic internship");
            return null;
        }

        if (intershipOpportunity.getMajor() != null && !internshipOpportunity.getMajor().equalsIgnoreCase(this.major)){
            System.out.println("Major does not match preferred major")
                return null;
        }

        Application app = new Application(this.userID, this.year, internshipOpportunity);
        applications.add(app);
        internshipOpportunity.addApplication(app);
        System.out.println("Application submitted");
        return app;
    }

    public List<Application> viewAppliedInternship(){
        return new ArrayList<>(applications);
    }

    public Internship acceptOpportunity(Application application){
        if (!applications.contains(application)){
            return null;
        }

        if (!application.successfulApplication()){
            System.out.println("Application unscuccessful.");
            return null;
        }
        
        boolean accepted = application.acceptPlacement();
        if (accepted){
            for (Application a : applications){
                if (a != application && a.getStatus() != ApplicationStatus.WITHDRAWN){
                    a.setStatus(ApplicationStatus.WITHDRAWN);
                }
            }
            return application.getInternship();
        }
        return null;
    }

    public void requestWithdrawal(Application application, CarrerCenterStaff staff){
        if (applications.contains(application)){
            application.setStatus(ApplicationStatus.WITHDRAWAL_REQUESTED);
            staff.approveWithdrawal(application, false);       //define in CareerCentreStaff
        }
    }

    public int applicationCount(){
        return applications.size();
    }
    


}
