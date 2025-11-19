package enums;

/**
 * Represents the types of an majors
 */

public enum Major {
	CS("Computer Science"),
    DSAI("Data Science & AI"),
    CE("Computer Engineering"),
    IEM("Information Engineering & Media"),
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
