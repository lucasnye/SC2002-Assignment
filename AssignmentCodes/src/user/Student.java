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
        this.profileVisibility = !this.profileVisibility;
    }

    public List<Internship> viewAvailableOpportunities(List<Internship> allInternship){
        List<Internship> result = new ArrayList<>();
        for (Internship i : allInternship) {
            if (i.getVisibility() && (i.getMajor() == null || i.getMajor().equalsIgnoreCase(this.major))){
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

        //the different level of internship for y1 and y2
        if (this.year <= 2 && internshipOpportunity.getLevel() != InternshipLevel.BASIC){
            System.out.println("Y1 and Y2 students can only apply for basic internships");
            return null;
        
        Application app = new Application(this.getUserId(), this.year, AppStatus.PENDING, internshipOpportunity);
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

        if (!application.successfulApp()){
            System.out.println("Application unsuccessful.");
            return null;
        }
        
        boolean accepted = application.acceptPlacement();
        if (accepted){
            for (Application a : applications){
                if (a != application && a.getStatus() != AppStatus.WITHDRAWN){
                    a.setStatus(AppStatus.WITHDRAWN);
                }
            }
            return application.getInternship();
        }
        return null;
    }

    public void requestWithdrawal(Application application){
        if (applications.contains(application)){
            application.setStatus(AppStatus.WITHDRAWAL_REQUESTED);
        }
    }

    public int applicationCount(){
        return applications.size();
    }
}





