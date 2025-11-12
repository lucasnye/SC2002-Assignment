package user;

public class Application {
    private String studentID;
    private int year;
    private AppStatus status;
    private boolean accepted;

    //assumes new application made will never already be accepted
    public Application(String studentID, int year, AppStatus status){
        this.studentID = studentID;
        this.year = year;
        this.status = status;
        this.accepted = false;
    }
    public String getStudentID(){
        return studentID;
    }
    public int getYear(){
        return year;
    }
    public AppStatus getStatus(){
        return status;
    }
    public boolean getAccepted(){
        return accepted;
    }
    //only used to check if they can accept, if want to check status use getStatus()
    public boolean successfulApp(){
        if (status == AppStatus.SUCCESSFUL){
            return true;
        }else{
            return false;
        }
    }
    public boolean acceptPlacement(){
        if (this.successfulApp()){
            this.accepted = true;
            return true;
        }else{
            System.out.println("Application cannot be accepted.");
            return false;
        }
    }

    // need this for CompanyRep class
    // code sets status of application
    public void setStatus(AppStatus status) {
        this.status = status;
    }


    public String print() {
        return String.format(
            "Application Details:\n" +
            "Student ID: %s\n" +
            "Year: %d\n" +
            "Status: %s\n" +
            "Accepted: %b",
            studentID, year, status, accepted
        );
    }


}
