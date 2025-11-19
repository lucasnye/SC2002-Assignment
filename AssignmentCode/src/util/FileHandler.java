package util;

import entity.user.Student;
import entity.user.CareerCenterStaff;
import entity.user.CompanyRepresentative;
import entity.domain.InternshipOpportunity;
import enums.Major;
import enums.InternshipLevel;
import enums.InternshipStatus;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles reading and writing data to CSV files
 */
public class FileHandler {
    
    private static final String DEFAULT_PASSWORD = "password";
    private static final String COMPANY_REP_ID_PREFIX = "CR";
    
    /**
     * Loads students from CSV file
     * CSV Format: StudentID,Name,Major,Year,Email
     */
    public static List<Student> loadStudents(String filePath) {
        List<Student> students = new ArrayList<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean isFirstLine = true;
            
            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }
                
                if (line.trim().isEmpty()) {
                    continue;
                }
                
                String[] data = line.split(",");
                if (data.length >= 5) {
                    String studentId = data[0].trim();
                    String name = data[1].trim();
                    String majorStr = data[2].trim();
                    int year = Integer.parseInt(data[3].trim());
                    String email = data[4].trim();
                    
                    Major major = parseMajor(majorStr);
                    
                    Student student = new Student(studentId, name, email, DEFAULT_PASSWORD, year, major);
                    students.add(student);
                }
            }
            
            System.out.println("Loaded " + students.size() + " students from " + filePath);
            
        } catch (FileNotFoundException e) {
            System.err.println("Student file not found: " + filePath);
        } catch (IOException e) {
            System.err.println("Error reading student file: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Error parsing student data: " + e.getMessage());
        }
        
        return students;
    }
    
    /**
     * Loads career center staff from CSV file
     * CSV Format: StaffID,Name,Role,Department,Email
     */
    public static List<CareerCenterStaff> loadStaff(String filePath) {
        List<CareerCenterStaff> staffList = new ArrayList<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean isFirstLine = true;
            
            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }
                
                if (line.trim().isEmpty()) {
                    continue;
                }
                
                String[] data = line.split(",");
                if (data.length >= 5) {
                    String staffId = data[0].trim();
                    String name = data[1].trim();
                    String department = data[3].trim();
                    String email = data[4].trim();
                    
                    CareerCenterStaff staff = new CareerCenterStaff(staffId, name, email, DEFAULT_PASSWORD, department);
                    staffList.add(staff);
                }
            }
            
            System.out.println("Loaded " + staffList.size() + " staff members from " + filePath);
            
        } catch (FileNotFoundException e) {
            System.err.println("Staff file not found: " + filePath);
        } catch (IOException e) {
            System.err.println("Error reading staff file: " + e.getMessage());
        }
        
        return staffList;
    }
    
    /**
     * Loads company representatives from CSV file
     * CSV Format: CompanyRepID,Name,CompanyName,Department,Position,Email,Status
     */
    public static List<CompanyRepresentative> loadCompanyReps(String filePath) {
        List<CompanyRepresentative> repList = new ArrayList<>();
        
        File file = new File(filePath);
        if (!file.exists()) {
            System.out.println("Company representative file not found. Starting with empty list.");
            createCompanyRepFile(filePath);
            return repList;
        }
        
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean isFirstLine = true;
            
            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }
                
                if (line.trim().isEmpty()) {
                    continue;
                }
                
                String[] data = line.split(",");
                if (data.length >= 7) {
                    String repId = data[0].trim();
                    String name = data[1].trim();
                    String companyName = data[2].trim();
                    String department = data[3].trim();
                    String position = data[4].trim();
                    String email = data[5].trim();
                    String status = data[6].trim();
                    
                    CompanyRepresentative rep = new CompanyRepresentative(
                        repId, name, email, DEFAULT_PASSWORD, companyName, department, position
                    );
                    
                    rep.setRegistrationApproved(status.equalsIgnoreCase("Approved"));
                    
                    repList.add(rep);
                }
            }
            
            System.out.println("Loaded " + repList.size() + " company representatives from " + filePath);
            
        } catch (IOException e) {
            System.err.println("Error reading company representative file: " + e.getMessage());
        }
        
        return repList;
    }
    
    /**
     * Saves company representatives to CSV file
     */
    public static void saveCompanyReps(List<CompanyRepresentative> reps, String filePath) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(filePath))) {
            pw.println("CompanyRepID,Name,CompanyName,Department,Position,Email,Status");
            
            for (CompanyRepresentative rep : reps) {
                String status = rep.isRegistrationApproved() ? "Approved" : "Pending";
                pw.println(String.format("%s,%s,%s,%s,%s,%s,%s",
                    rep.getUserId(),
                    rep.getName(),
                    rep.getCompanyName(),
                    rep.getDepartment(),
                    rep.getPosition(),
                    rep.getEmail(),
                    status
                ));
            }
            
            System.out.println("Saved " + reps.size() + " company representatives to " + filePath);
            
        } catch (IOException e) {
            System.err.println("Error writing company representative file: " + e.getMessage());
        }
    }
    
    /**
     * Creates an empty company representative file with header
     */
    private static void createCompanyRepFile(String filePath) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(filePath))) {
            pw.println("CompanyRepID,Name,CompanyName,Department,Position,Email,Status");
            System.out.println("Created new company representative file: " + filePath);
        } catch (IOException e) {
            System.err.println("Error creating company representative file: " + e.getMessage());
        }
    }
    
    /**
     * Helper method to parse major string to Major enum
     */
    private static Major parseMajor(String majorStr) {
        try {
            return Major.valueOf(majorStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            for (Major major : Major.values()) {
                if (major.getFullName().equalsIgnoreCase(majorStr)) {
                    return major;
                }
            }
            System.err.println("Unknown major: " + majorStr + ", defaulting to CS");
            return Major.CS;
        }
    }
    
    /**
     * Generates a unique Company Representative ID
     */
    public static String generateCompanyRepId(List<CompanyRepresentative> existingReps) {
        int maxId = 0;

        for (CompanyRepresentative rep : existingReps) {
            String id = rep.getUserId();
            if (id.startsWith(COMPANY_REP_ID_PREFIX)) {
                try {
                    int num = Integer.parseInt(id.substring(2));
                    maxId = Math.max(maxId, num);
                } catch (NumberFormatException e) {
                    // Skip invalid IDs
                }
            }
        }

        return String.format("%s%04d", COMPANY_REP_ID_PREFIX, maxId + 1);
    }

    /**
     * Loads internship opportunities from CSV file
     * CSV Format: OpportunityID,Title,Description,Level,PreferredMajor,OpeningDate,ClosingDate,Status,CompanyName,AssignedRepresentativeId,TotalSlots,FilledSlots,IsVisible
     */
    public static List<InternshipOpportunity> loadInternships(String filePath) {
        List<InternshipOpportunity> internships = new ArrayList<>();

        File file = new File(filePath);
        if (!file.exists()) {
            System.out.println("Internship file not found. Starting with empty list.");
            createInternshipFile(filePath);
            return internships;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean isFirstLine = true;

            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }

                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] data = parseCSVLine(line);
                if (data.length >= 13) {
                    try {
                        String opportunityId = data[0].trim();
                        String title = data[1].trim();
                        String description = data[2].trim();
                        InternshipLevel level = InternshipLevel.valueOf(data[3].trim().toUpperCase());
                        Major preferredMajor = parseMajor(data[4].trim());
                        LocalDate openingDate = LocalDate.parse(data[5].trim());
                        LocalDate closingDate = LocalDate.parse(data[6].trim());
                        InternshipStatus status = InternshipStatus.valueOf(data[7].trim().toUpperCase());
                        String companyName = data[8].trim();
                        String assignedRepresentativeId = data[9].trim();
                        int totalSlots = Integer.parseInt(data[10].trim());
                        int filledSlots = Integer.parseInt(data[11].trim());
                        boolean isVisible = Boolean.parseBoolean(data[12].trim());

                        InternshipOpportunity internship = new InternshipOpportunity(
                            opportunityId, title, description, level, preferredMajor,
                            openingDate, closingDate, companyName, assignedRepresentativeId, totalSlots
                        );

                        internship.setStatus(status);
                        internship.setFilledSlots(filledSlots);
                        internship.setVisible(isVisible);

                        internships.add(internship);
                    } catch (IllegalArgumentException | DateTimeParseException e) {
                        System.err.println("Error parsing internship data: " + e.getMessage());
                    }
                }
            }

            System.out.println("Loaded " + internships.size() + " internships from " + filePath);

        } catch (IOException e) {
            System.err.println("Error reading internship file: " + e.getMessage());
        }

        return internships;
    }

    /**
     * Saves internship opportunities to CSV file
     */
    public static void saveInternships(List<InternshipOpportunity> internships, String filePath) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(filePath))) {
            pw.println("OpportunityID,Title,Description,Level,PreferredMajor,OpeningDate,ClosingDate,Status,CompanyName,AssignedRepresentativeId,TotalSlots,FilledSlots,IsVisible");

            for (InternshipOpportunity internship : internships) {
                String line = String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%d,%d,%b",
                    internship.getOpportunityId(),
                    internship.getTitle(),
                    escapeCSV(internship.getDescription()),
                    internship.getLevel(),
                    internship.getPreferredMajor(),
                    internship.getOpeningDate(),
                    internship.getClosingDate(),
                    internship.getStatus(),
                    internship.getCompanyName(),
                    internship.getAssignedRepresentativeId(),
                    internship.getTotalSlots(),
                    internship.getFilledSlots(),
                    internship.isVisible()
                );
                pw.println(line);
            }

            System.out.println("Saved " + internships.size() + " internships to " + filePath);

        } catch (IOException e) {
            System.err.println("Error writing internship file: " + e.getMessage());
        }
    }

    /**
     * Creates an empty internship file with header
     */
    private static void createInternshipFile(String filePath) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(filePath))) {
            pw.println("OpportunityID,Title,Description,Level,PreferredMajor,OpeningDate,ClosingDate,Status,CompanyName,AssignedRepresentativeId,TotalSlots,FilledSlots,IsVisible");
            System.out.println("Created new internship file: " + filePath);
        } catch (IOException e) {
            System.err.println("Error creating internship file: " + e.getMessage());
        }
    }

    /**
     * Helper method to escape CSV values (handles commas and quotes in descriptions)
     */
    private static String escapeCSV(String value) {
        if (value == null) {
            return "";
        }

        // If the value contains comma, quote, or newline, wrap it in quotes and escape internal quotes
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }

        return value;
    }

    /**
     * Helper method to parse a CSV line handling quoted values
     */
    private static String[] parseCSVLine(String line) {
        List<String> result = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);

            if (c == '"') {
                // Check for escaped quote (two consecutive quotes)
                if (i + 1 < line.length() && line.charAt(i + 1) == '"') {
                    current.append('"');
                    i++; // Skip next quote
                } else {
                    inQuotes = !inQuotes;
                }
            } else if (c == ',' && !inQuotes) {
                result.add(current.toString());
                current = new StringBuilder();
            } else {
                current.append(c);
            }
        }

        result.add(current.toString());
        return result.toArray(new String[0]);
    }
}