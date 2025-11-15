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
    public String getRequestId() {
        return requestId;
    }
    
    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }
    
    public String getStudentId() {
        return studentId;
    }
    
    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }
    
    public String getApplicationId() {
        return applicationId;
    }
    
    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }
    
    public RequestStatus getRequestStatus() {
        return requestStatus;
    }
    
    public void setRequestStatus(RequestStatus requestStatus) {
        this.requestStatus = requestStatus;
    }
    
    public boolean isBeforePlacement() {
        return isBeforePlacement;
    }
    
    public void setBeforePlacement(boolean beforePlacement) {
        isBeforePlacement = beforePlacement;
    }
    
    public LocalDate getRequestDate() {
        return requestDate;
    }
    
    public void setRequestDate(LocalDate requestDate) {
        this.requestDate = requestDate;
    }
    
    public LocalDate getProcessedDate() {
        return processedDate;
    }
    
    public void setProcessedDate(LocalDate processedDate) {
        this.processedDate = processedDate;
    }
    
    public String getRemarks() {
        return remarks;
    }
    
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
    
    @Override
    public String toString() {
        return "Request ID: " + requestId + 
               ", Student: " + studentId + 
               ", Application: " + applicationId + 
               ", Status: " + requestStatus + 
               ", Before Placement: " + isBeforePlacement;
    }
}