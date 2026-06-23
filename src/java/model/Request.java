package model;

import java.util.Date;

/**
 * Model class representing a support request in the ApartmentManagement system.
 * Maps to the Requests table in the database.
 *
 * Status values (phải dùng đúng chuỗi này, thỏa thuận toàn nhóm):
 * "Pending" - Mới tạo, chờ Manager duyệt
 * "Approved" - Manager đã duyệt và phân công
 * "Processing" - Staff đang xử lý
 * "Completed" - Staff đã hoàn thành
 * "Cancelled" - Resident đã hủy
 *
 * Shared across SF3 (assignment), SF4 (resident create/cancel, staff process).
 */
public class Request {

    // -------------------------------------------------------
    // Fields mapping Requests table columns
    // -------------------------------------------------------
    private int requestId;
    private int residentId;
    private int apartmentId;
    private int requestTypeId;
    private String title;
    private String description;
    private Date transferDate;
    private String status;
    private Date createdAt;
    private int approvedBy; // UserId of Manager who approved (0 if not yet)
    private int assignedTo; // UserId of Staff assigned (0 if not yet)

    // -------------------------------------------------------
    // Join fields (populated by DAO queries, not stored in DB)
    // -------------------------------------------------------
    private String residentName; // Users.FullName of resident
    private String apartmentNumber; // Apartments.ApartmentNumber
    private String requestTypeName; // RequestTypes.TypeName
    private String assignedToName; // Users.FullName of assigned staff
    private String approvedByName; // Users.FullName of approving manager

    // -------------------------------------------------------
    // Status constants (dùng chung toàn nhóm, không tự đặt chuỗi khác)
    // -------------------------------------------------------
    public static final String STATUS_PENDING = "Pending";
    public static final String STATUS_APPROVED = "Approved";
    public static final String STATUS_PROCESSING = "Processing";
    public static final String STATUS_COMPLETED = "Completed";
    public static final String STATUS_CANCELLED = "Cancelled";

    // -------------------------------------------------------
    // Constructors
    // -------------------------------------------------------

    public Request() {
    }

    public Request(int requestId, int residentId, int apartmentId,
            int requestTypeId, String title, String description,
            Date transferDate, String status, Date createdAt,
            int approvedBy, int assignedTo) {
        this.requestId = requestId;
        this.residentId = residentId;
        this.apartmentId = apartmentId;
        this.requestTypeId = requestTypeId;
        this.title = title;
        this.description = description;
        this.transferDate = transferDate;
        this.status = status;
        this.createdAt = createdAt;
        this.approvedBy = approvedBy;
        this.assignedTo = assignedTo;
    }

    // -------------------------------------------------------
    // Getters & Setters — table columns
    // -------------------------------------------------------

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public int getResidentId() {
        return residentId;
    }

    public void setResidentId(int residentId) {
        this.residentId = residentId;
    }

    public int getApartmentId() {
        return apartmentId;
    }

    public void setApartmentId(int apartmentId) {
        this.apartmentId = apartmentId;
    }

    public int getRequestTypeId() {
        return requestTypeId;
    }

    public void setRequestTypeId(int requestTypeId) {
        this.requestTypeId = requestTypeId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getTransferDate() {
        return transferDate;
    }

    public void setTransferDate(Date transferDate) {
        this.transferDate = transferDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public int getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(int approvedBy) {
        this.approvedBy = approvedBy;
    }

    public int getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(int assignedTo) {
        this.assignedTo = assignedTo;
    }

    // -------------------------------------------------------
    // Getters & Setters — join fields
    // -------------------------------------------------------

    public String getResidentName() {
        return residentName;
    }

    public void setResidentName(String residentName) {
        this.residentName = residentName;
    }

    public String getApartmentNumber() {
        return apartmentNumber;
    }

    public void setApartmentNumber(String apartmentNumber) {
        this.apartmentNumber = apartmentNumber;
    }

    public String getRequestTypeName() {
        return requestTypeName;
    }

    public void setRequestTypeName(String requestTypeName) {
        this.requestTypeName = requestTypeName;
    }

    public String getAssignedToName() {
        return assignedToName;
    }

    public void setAssignedToName(String assignedToName) {
        this.assignedToName = assignedToName;
    }

    public String getApprovedByName() {
        return approvedByName;
    }

    public void setApprovedByName(String approvedByName) {
        this.approvedByName = approvedByName;
    }

    // -------------------------------------------------------
    // Helper methods
    // -------------------------------------------------------

    /**
     * Returns true if this request can be assigned by Manager.
     * Only Pending requests can be assigned.
     */
    public boolean isPending() {
        return STATUS_PENDING.equals(status);
    }

    /**
     * Returns true if this request is being processed by Staff.
     */
    public boolean isProcessing() {
        return STATUS_PROCESSING.equals(status);
    }

    /**
     * Returns true if this request has been completed.
     */
    public boolean isCompleted() {
        return STATUS_COMPLETED.equals(status);
    }

    /**
     * Returns true if this request has been cancelled by Resident.
     */
    public boolean isCancelled() {
        return STATUS_CANCELLED.equals(status);
    }

    /**
     * Returns true if request has been approved and assigned to staff.
     */
    public boolean isApproved() {
        return STATUS_APPROVED.equals(status);
    }

    @Override
    public String toString() {
        return "Request{requestId=" + requestId
                + ", title=" + title
                + ", status=" + status
                + ", residentId=" + residentId
                + ", assignedTo=" + assignedTo + "}";
    }
}
