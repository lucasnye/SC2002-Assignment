package enums;

public enum Major {
	CSC("Computer Science"),
    EEE("Electrical and Electronic Engineering"),
    MAE("Mechanical and Aerospace Engineering"),
    CEE("Civil and Environmental Engineering"),
    MSE("Materials Science and Engineering"),
    CBE("Chemical and Biomolecular Engineering"),
    BUSINESS("Business"),
    ECONOMICS("Economics"),
    SPMS("School of Physics and Math");
    
    private final String fullName;
    
    Major(String fullName) {
        this.fullName = fullName;
    }
    
    public String getFullName() {
        return fullName;
    }
}
