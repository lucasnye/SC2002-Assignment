package user;

public abstract class User{
    private String userId;
    protected String name;
    protected String password;
    protected Role role;
    protected boolean loggedIn = false;

    public User(String userId, String name, String password, Role role){
        this.userId = userId;
        this.name = name;
        this.password = password;
        this.role = role;
    }

    // getters
    public String getUserId() {
        return this.userId;
    }

    public boolean login(String userId,String password){
        if (this.userId.equals(userId)&&this.password.equals(password)){
            loggedIn = true;
            System.out.println(name+" logged in successfully");
            return true;
        }else{
            System.out.println("Invalid login");
            return false;
        }
    }

    public void logout(){
        if (loggedIn){
            loggedIn = false;
            System.out.println(name+" logged out successfully");
        }else{
            System.out.println("User is not logged in");
        }
    }

    public void passwordChange(String userId, String newPassword){
        if (this.userId.equals(userId)){
            this.password = newPassword;
            System.out.println(name+" has successfully updated password");
        }else{
            System.out.println("User ID not found");
        }
    }
}