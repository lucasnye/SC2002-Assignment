package util;

import entity.user.Student;
import entity.user.CareerCenterStaff;
import entity.user.CompanyRepresentative;
import enums.Major;

import java.io.*;
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
}