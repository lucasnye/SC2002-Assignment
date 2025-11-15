package util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Utility class for validating user inputs and IDs
 */
public class ValidationUtil {
    
    private static final String STUDENT_ID_PATTERN = "^U\\d{7}[A-Z]$";
    private static final String STAFF_ID_PATTERN = "^[a-z]{3}\\d{3}$";
    private static final String EMAIL_PATTERN = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
    
    /**
     * Validates if a string matches Student ID format (U followed by 7 digits and a letter)
     */
    public static boolean isValidStudentId(String id) {
        if (id == null || id.isEmpty()) {
            return false;
        }
        return id.matches(STUDENT_ID_PATTERN);
    }
    
    /**
     * Validates if a string matches Staff ID format (3 lowercase letters followed by 3 digits)
     */
    public static boolean isValidStaffId(String id) {
        if (id == null || id.isEmpty()) {
            return false;
        }
        return id.matches(STAFF_ID_PATTERN);
    }
    
    /**
     * Validates if a string is a valid email address
     */
    public static boolean isValidEmail(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }
        return email.matches(EMAIL_PATTERN);
    }
    
    /**
     * Validates if a string is not null or empty
     */
    public static boolean isNotEmpty(String input) {
        return input != null && !input.trim().isEmpty();
    }
    
    /**
     * Validates if year of study is within valid range (1-4)
     */
    public static boolean isValidYearOfStudy(int year) {
        return year >= 1 && year <= 4;
    }
    
    /**
     * Validates if a date string is in the correct format (yyyy-MM-dd)
     */
    public static boolean isValidDateFormat(String dateStr) {
        if (dateStr == null || dateStr.isEmpty()) {
            return false;
        }
        try {
            LocalDate.parse(dateStr, DateTimeFormatter.ISO_LOCAL_DATE);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }
    
    /**
     * Parses a date string to LocalDate
     */
    public static LocalDate parseDate(String dateStr) {
        try {
            return LocalDate.parse(dateStr, DateTimeFormatter.ISO_LOCAL_DATE);
        } catch (DateTimeParseException e) {
            return null;
        }
    }
    
    /**
     * Validates if opening date is before closing date
     */
    public static boolean isValidDateRange(LocalDate openingDate, LocalDate closingDate) {
        if (openingDate == null || closingDate == null) {
            return false;
        }
        return openingDate.isBefore(closingDate);
    }
    
    /**
     * Validates if a number is within a valid range
     */
    public static boolean isWithinRange(int value, int min, int max) {
        return value >= min && value <= max;
    }
    
    /**
     * Sanitizes input by trimming whitespace
     */
    public static String sanitizeInput(String input) {
        if (input == null) {
            return "";
        }
        return input.trim();
    }
}