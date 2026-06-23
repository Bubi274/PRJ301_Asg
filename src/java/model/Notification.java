package model;

import java.util.Date;

/**
 * Model class representing a notification/announcement.
 * Maps to the Notifications table in the database.
 *
 * Type values:
 *   "System"  - Thông báo hệ thống (cúp điện, bảo trì...)
 *   "Billing" - Thông báo hóa đơn
 *   "Request" - Thông báo liên quan yêu cầu hỗ trợ
 *   "Alert"   - Cảnh báo (nợ phí...)
 *
 * IsGlobal = true  → Broadcast tới tất cả (ReceiverId = NULL)
 * IsGlobal = false → Gửi cá nhân (ReceiverId = userId cụ thể)
 */
public class Notification {

    // -------------------------------------------------------
    // Fields mapping Notifications table columns
    // -------------------------------------------------------
    private int notificationId;
    private String title;
    private String content;
    private String type;
    private int senderId;
    private int receiverId;    // 0 if IsGlobal = true
    private int apartmentId;   // 0 if not apartment-specific
    private boolean isRead;
    private Date createdAt;
    private boolean isGlobal;

    // -------------------------------------------------------
    // Type constants
    // -------------------------------------------------------
    public static final String TYPE_SYSTEM  = "System";
    public static final String TYPE_BILLING = "Billing";
    public static final String TYPE_REQUEST = "Request";
    public static final String TYPE_ALERT   = "Alert";

    // -------------------------------------------------------
    // Join fields (populated by DAO)
    // -------------------------------------------------------
    private String senderName;    // Users.FullName of sender

    // -------------------------------------------------------
    // Constructors
    // -------------------------------------------------------

    public Notification() {
    }

    public Notification(String title, String content, String type,
                        int senderId, boolean isGlobal) {
        this.title = title;
        this.content = content;
        this.type = type;
        this.senderId = senderId;
        this.isGlobal = isGlobal;
        this.isRead = false;
    }

    // -------------------------------------------------------
    // Getters & Setters
    // -------------------------------------------------------

    public int getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(int notificationId) {
        this.notificationId = notificationId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public int getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(int receiverId) {
        this.receiverId = receiverId;
    }

    public int getApartmentId() {
        return apartmentId;
    }

    public void setApartmentId(int apartmentId) {
        this.apartmentId = apartmentId;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean isRead) {
        this.isRead = isRead;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isGlobal() {
        return isGlobal;
    }

    public void setGlobal(boolean isGlobal) {
        this.isGlobal = isGlobal;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    // -------------------------------------------------------
    // Helper methods
    // -------------------------------------------------------

    /**
     * Returns Vietnamese label for notification type.
     * Dùng được thẳng trong JSP: ${notif.typeLabel}
     */
    public String getTypeLabel() {
        switch (type) {
            case TYPE_SYSTEM:  return "Hệ thống";
            case TYPE_BILLING: return "Hóa đơn";
            case TYPE_REQUEST: return "Yêu cầu";
            case TYPE_ALERT:   return "Cảnh báo";
            default:           return type;
        }
    }

    /**
     * Returns CSS badge class for display in JSP.
     * Ví dụ: <span class="badge ${notif.badgeClass}">
     */
    public String getBadgeClass() {
        switch (type) {
            case TYPE_SYSTEM:  return "badge-info";
            case TYPE_BILLING: return "badge-warning";
            case TYPE_REQUEST: return "badge-primary";
            case TYPE_ALERT:   return "badge-danger";
            default:           return "badge-secondary";
        }
    }

    @Override
    public String toString() {
        return "Notification{notificationId=" + notificationId
                + ", title=" + title
                + ", type=" + type
                + ", isGlobal=" + isGlobal
                + ", senderId=" + senderId + "}";
    }
}
