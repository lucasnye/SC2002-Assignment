package entity.domain;

import enums.RequestStatus;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Represents a student's request to withdraw from an internship application
 */
public class WithdrawalRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String requestId;
    private String studentId;
    private String applicationId;
    private RequestStatus requestStatus;
    private boolean isBeforePlacement;
    private LocalDate requestDate;
    private LocalDate processedDate;
    private String remarks;
    
    /**
     * Constructor for WithdrawalRequest
     */
    public WithdrawalRequest(String requestId, String studentId, String applicationId, 
                             boolean isBeforePlacement) {
        this.requestId = requestId;
        this.studentId = studentId;
        this.applicationId = applicationId;
        this.isBeforePlacement = isBeforePlacement;
        this.requestStatus = RequestStatus.PENDING;
        this.requestDate = LocalDate.now();
        this.processedDate = null;
        this.remarks = "";
    }
    
    // Getters and Setters
    /**
     * Returns withdrawal request ID
     */
    public String getRequestId() {
        return requestId;
    }
    
    /**
     * Sets withdrawal request ID
     */
    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }
    
    /**
     * Returns student ID
     */
    public String getStudentId() {
        return studentId;
    }
    
    /**
     * Sets student ID
     */
    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }
    
    /**
     * Returns application ID
     */
    public String getApplicationId() {
        return applicationId;
    }
    
    /**
     * Sets application ID
     */
    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }
    
    /**
     * Returns withdrawal request status
     */
    public RequestStatus getRequestStatus() {
        return requestStatus;
    }
    
    /**
     * Sets withdrawal request status
     */
    public void setRequestStatus(RequestStatus requestStatus) {
        this.requestStatus = requestStatus;
    }
    
    /**
     * Returns if withdrawal request is submitted before successful placement
     */
    public boolean isBeforePlacement() {
        return isBeforePlacement;
    }
    
    /**
     * Sets withdrawal request submitted before successful placement status
     */
    public void setBeforePlacement(boolean beforePlacement) {
        isBeforePlacement = beforePlacement;
    }
    
    /**
     * Returns request date
     */
    public LocalDate getRequestDate() {
        return requestDate;
    }
    
    /**
     * Sets request date
     */
    public void setRequestDate(LocalDate requestDate) {
        this.requestDate = requestDate;
    }
    
    /**
     * Returns request processed date
     */
    public LocalDate getProcessedDate() {
        return processedDate;
    }
    
    /**
     * Sets request processed date
     */
    public void setProcessedDate(LocalDate processedDate) {
        this.processedDate = processedDate;
    }
    
    /**
     * Returns request remarks
     */
    public String getRemarks() {
        return remarks;
    }
    
    /**
     * Sets request remarks
     */
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
    
    /**
     * Approves the withdrawal request
     */
    public void approve(String remarks) {
        this.requestStatus = RequestStatus.APPROVED;
        this.processedDate = LocalDate.now();
        this.remarks = remarks;
    }
    
    /**
     * Rejects the withdrawal request
     */
    public void reject(String remarks) {
        this.requestStatus = RequestStatus.REJECTED;
        this.processedDate = LocalDate.now();
        this.remarks = remarks;
    }
    
    /**
     * Prints withdrawal request information
     */
    @Override
    public String toString() {
        return "Request ID: " + requestId + 
               ", Student: " + studentId + 
               ", Application: " + applicationId + 
               ", Status: " + requestStatus + 
               ", Before Placement: " + isBeforePlacement;
    }
}